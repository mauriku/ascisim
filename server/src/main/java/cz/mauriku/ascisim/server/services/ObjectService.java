package cz.mauriku.ascisim.server.services;


import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
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
import java.util.List;

public class ObjectService {
  private final Ignite ignite;
  private final IgniteCache<String, PaxImpObject> objectCache;

  public ObjectService(Ignite ignite) {
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

  public List<Cache.Entry<String, PaxImpObject>> findByMeta(String metaObjectId) {
    ScanQuery<String, PaxImpObject> q = new ScanQuery<>((k, p) -> p.getMetaObjectId().equals(metaObjectId));
    try (QueryCursor<Cache.Entry<String, PaxImpObject>> cursor = this.objectCache.query(q)) {
      return cursor.getAll();
    }
  }
}
