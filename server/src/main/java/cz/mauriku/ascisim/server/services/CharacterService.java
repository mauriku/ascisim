package cz.mauriku.ascisim.server.services;

import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
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

public class CharacterService {
  private final Ignite ignite;
  private final IgniteCache<String, PaxImpCharacter> characterCache;

  public CharacterService(Ignite ignite) {
    this.ignite = ignite;

    CacheConfiguration<String, PaxImpCharacter> cfg = new CacheConfiguration<>("Character");
    cfg.setCacheMode(CacheMode.REPLICATED);
    cfg.setBackups(1);
    cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
    cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
    cfg.setIndexedTypes(String.class, PaxImpCharacter.class);
    this.characterCache = ignite.getOrCreateCache(cfg);
  }

  public PaxImpCharacter storeNewCharacter(PaxImpCharacter character) {
    if (characterCache.containsKey(character.getId()))
      throw new IllegalStateException("Cache already contains character with id " + character.getId() + ".");

    this.characterCache.put(character.getId(), character);
    return character;
  }

  public List<Cache.Entry<String, PaxImpCharacter>> findByMeta(String metaObjectId) {
    ScanQuery<String, PaxImpCharacter> q = new ScanQuery<>((k, p) -> p.getMetaObjectId().equals(metaObjectId));
    try (QueryCursor<Cache.Entry<String, PaxImpCharacter>> cursor = this.characterCache.query(q)) {
      return cursor.getAll();
    }
  }
}
