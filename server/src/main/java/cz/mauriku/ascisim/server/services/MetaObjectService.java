package cz.mauriku.ascisim.server.services;

import cz.mauriku.ascisim.server.objects.*;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;

import javax.cache.Cache;
import java.util.ArrayList;

public class MetaObjectService {
  private final Ignite ignite;
  private final IgniteCache<String, PaxImpMetaObject> objectCache;
  private final IgniteCache<String, PaxImpMetaObjectLog> logCache;

  public MetaObjectService(Ignite ignite) {
    this.ignite = ignite;

    CacheConfiguration<String, PaxImpMetaObject> cfg1 = new CacheConfiguration<>("MetaObject");
    cfg1.setCacheMode(CacheMode.REPLICATED);
    cfg1.setBackups(1);
    cfg1.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
    cfg1.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
    cfg1.setIndexedTypes(String.class, PaxImpMetaObject.class);
    this.objectCache = ignite.getOrCreateCache(cfg1);

    CacheConfiguration<String, PaxImpMetaObjectLog> cfg2 = new CacheConfiguration<>("MetaObjectLog");
    cfg2.setCacheMode(CacheMode.REPLICATED);
    cfg2.setBackups(1);
    cfg2.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
    cfg2.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
    cfg2.setIndexedTypes(String.class, PaxImpMetaObjectLog.class);
    this.logCache = ignite.getOrCreateCache(cfg2);
  }

  public PaxImpMetaObject createMetaObject(PlayerAccount author, PaxImpObjectType type, String name) {
    PaxImpMetaObject metaObject = new PaxImpMetaObject();
    metaObject.setType(type);
    metaObject.setMetaObjectProperty(PaxImpObject.NAME, name);
    metaObject.setAuthorAccount(author);

    PaxImpMetaObjectLog log = new PaxImpMetaObjectLog(metaObject.getId(), PaxImpMetaObjectLogType.CREATED, author);
    metaObject.getLog().add(log);

    try (Transaction tx = ignite.transactions().txStart()) {
      this.logCache.put(log.getId(), log);
      this.objectCache.put(metaObject.getId(), metaObject);
      tx.commit();
    }

    return metaObject;
  }

  public PaxImpMetaObject updateMetaObject(PlayerAccount author, PaxImpMetaObject metaObject) {
    PaxImpMetaObjectLog log = new PaxImpMetaObjectLog(metaObject.getId(), PaxImpMetaObjectLogType.UPDATED, author);
    metaObject.getLog().add(log);

    try (Transaction tx = ignite.transactions().txStart()) {
      this.logCache.put(log.getId(), log);
      this.objectCache.replace(metaObject.getId(), metaObject);
      tx.commit();
    }

    return metaObject;
  }

  public PaxImpMetaObject getMetaObject(String id, boolean includeLog) {
    PaxImpMetaObject metaObject = this.objectCache.get(id);

    if (metaObject != null && includeLog) {
      if (metaObject.getLog() == null)
        metaObject.setLog(new ArrayList<>());

      ScanQuery<String, PaxImpMetaObjectLog> q = new ScanQuery<>((k, p) -> p.getMetaObjectId().equals(metaObject.getId()));
      try (QueryCursor<Cache.Entry<String, PaxImpMetaObjectLog>> cursor = this.logCache.query(q)) {
        for (Cache.Entry<String, PaxImpMetaObjectLog> entry : cursor)
          metaObject.getLog().add(entry.getValue());
      }
    }

    return metaObject;
  }
}
