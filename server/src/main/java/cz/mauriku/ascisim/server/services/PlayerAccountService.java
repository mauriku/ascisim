package cz.mauriku.ascisim.server.services;

import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.security.PasswordHasher;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;

public class PlayerAccountService {

  private final Ignite ignite;
  private final IgniteCache<String, PlayerAccount> cache;

  public PlayerAccountService(Ignite ignite) {
    CacheConfiguration<String, PlayerAccount> cfg = new CacheConfiguration<>();
    cfg.setCacheMode(CacheMode.REPLICATED);
    cfg.setName("PlayerAccount");
    cfg.setBackups(1);
    cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
    cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
    cfg.setIndexedTypes(String.class, PlayerAccount.class);

    this.ignite = ignite;
    this.cache = ignite.getOrCreateCache(cfg);
  }

  public PlayerAccount createAccount(String email, String password) {
    if (this.cache.containsKey(email))
      throw new IllegalStateException("Account [" + email + "] already exists. Not creating.");

    try {
      PlayerAccount acc = new PlayerAccount();
      acc.setPassword(PasswordHasher.createHash(password));
      acc.setEmail(email);
      acc.setEmailVerified(false);
      acc.setBanned(false);
      acc.setCreatedDate(Instant.now());

      this.cache.put(acc.getEmail(), acc);
      return acc;
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalStateException("Cannot create player account for [" + email + "].", e);
    }
  }

  public PlayerAccount findAccount(String email) {
    return this.cache.get(email);
  }
}
