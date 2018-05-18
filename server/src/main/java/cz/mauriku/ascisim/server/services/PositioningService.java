package cz.mauriku.ascisim.server.services;

import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
import cz.mauriku.ascisim.server.objects.PaxImpObject;
import cz.mauriku.ascisim.server.objects.PaxImpPositioning;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;

public class PositioningService {
  private final Ignite ignite;
  private final IgniteCache<String, PaxImpPositioning> positioningCache;

  public PositioningService(Ignite ignite) {
    this.ignite = ignite;

    CacheConfiguration<String, PaxImpPositioning> cfg = new CacheConfiguration<>("Positioning");
    cfg.setCacheMode(CacheMode.REPLICATED);
    cfg.setBackups(1);
    cfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
    cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
    cfg.setIndexedTypes(String.class, PaxImpPositioning.class);
    this.positioningCache = ignite.getOrCreateCache(cfg);
  }

  public PaxImpPositioning updatePosition(PaxImpObject object, int x, int y) {
    PaxImpPositioning pos = new PaxImpPositioning(object, x, y);
    positioningCache.putAsync(object.getId(), pos);
    return pos;
  }
}
