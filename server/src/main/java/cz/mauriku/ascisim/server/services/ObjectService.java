package cz.mauriku.ascisim.server.services;


import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
import cz.mauriku.ascisim.server.objects.PaxImpMetaObject;
import cz.mauriku.ascisim.server.objects.PaxImpObject;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.List;

public class ObjectService {
  private final Ignite ignite;
  private final IgniteCache<String, PaxImpObject> objectCache;
  private final MetaObjectService metaObjectService;

  public ObjectService(Ignite ignite, MetaObjectService metaObjectService) {
    this.metaObjectService = metaObjectService;
    this.ignite = ignite;

    CacheConfiguration<String, PaxImpObject> cfg = new CacheConfiguration<>("Object");
    cfg.setCacheMode(CacheMode.REPLICATED);
    cfg.setBackups(1);
    cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
    cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
    cfg.setIndexedTypes(String.class, PaxImpObject.class);
    this.objectCache = ignite.getOrCreateCache(cfg);
  }

  public PaxImpObject storeNewObject(PaxImpObject object) {
    if (objectCache.containsKey(object.getId()))
      throw new IllegalStateException("Cache already contains object with id " + object.getId() + ".");

    this.objectCache.put(object.getId(), object);
    return object;
  }

  public List<PaxImpObject> findByMeta(String metaObjectId) {
    ScanQuery<String, PaxImpObject> q = new ScanQuery<>((k, p) -> p.getMetaObjectId().equals(metaObjectId));
    try (QueryCursor<Cache.Entry<String, PaxImpObject>> cursor = this.objectCache.query(q)) {
      List<Cache.Entry<String, PaxImpObject>> result = cursor.getAll();
      List<PaxImpObject> objects = new ArrayList<>(result.size());

      for (Cache.Entry<String, PaxImpObject> obj : result)
        objects.add(extendWithMetaObject(obj.getValue()));

      return objects;
    }
  }

  public PaxImpObject findById(String id) {
    PaxImpObject obj = objectCache.get(id);
    if (obj != null)
      extendWithMetaObject(obj);

    return obj;
  }

  private PaxImpObject extendWithMetaObject(PaxImpObject object) {
    PaxImpMetaObject metaObject = metaObjectService.getMetaObject(object.getMetaObjectId(), false);
    object.setMetaObject(metaObject);
    return object;
  }
}
