package cz.mauriku.ascisim.server.services;

import cz.mauriku.ascisim.server.objects.PaxImpMetaObject;
import cz.mauriku.ascisim.server.objects.PaxImpObject;
import cz.mauriku.ascisim.server.objects.PaxImpObjectType;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;

public class MetaObjectService {
  private final Ignite ignite;
  private final IgniteCache<String, PaxImpMetaObject> cache;

  public MetaObjectService(Ignite ignite) {
    this.ignite = ignite;

    CacheConfiguration<String, PaxImpMetaObject> cfg = new CacheConfiguration<>();
    cfg.setCacheMode(CacheMode.REPLICATED);
    cfg.setName("MetaObject");
    cfg.setBackups(1);
    cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
    cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
    cfg.setIndexedTypes(String.class, PaxImpMetaObject.class);

    this.cache = ignite.getOrCreateCache(cfg);
  }

  public PaxImpMetaObject createMetaObject(PlayerAccount author, PaxImpObjectType type, String name) {
    PaxImpMetaObject metaobj = new PaxImpMetaObject();
    metaobj.setType(type);
    metaobj.setMetaObjectProperty(PaxImpObject.NAME, name);
    metaobj.setAuthorAccount(author);

    this.cache.put(metaobj.getId(), metaobj);

    return metaobj;
  }
}
