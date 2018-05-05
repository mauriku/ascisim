package cz.mauriku.ascisim.server.services;

import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.security.PasswordHasher;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.Query;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;

public class PlayerAccountService {

  private final Ignite ignite;
  private final IgniteCache<String, PlayerAccount> cache;

  public PlayerAccountService(Ignite ignite) {
    this.ignite = ignite;

    CacheConfiguration<String, PlayerAccount> cfg = new CacheConfiguration<>();
    cfg.setCacheMode(CacheMode.REPLICATED);
    cfg.setName("PlayerAccount");
    cfg.setBackups(1);
    cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
    cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
    cfg.setIndexedTypes(String.class, PlayerAccount.class);

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

  public boolean authenticateAccount(String email, String password) {
    PlayerAccount acc = findAccount(email);
    if (acc == null)
      return false;

    try {
      return PasswordHasher.validatePassword(password, acc.getPassword());
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalStateException("Cannot authenticate account [" + email + "].", e);
    }
  }

  public boolean changePassword(String email, String oldPassword, String newPassword) {
    boolean authenticated = authenticateAccount(email, oldPassword);
    if (authenticated) {
      try (Transaction tx = ignite.transactions().txStart()) {
        PlayerAccount acc = findAccount(email);
        acc.setPassword(newPassword);
        cache.replace(acc.getEmail(), acc);
        tx.commit();
        return true;
      }
    } else
      return false;
  }
}
