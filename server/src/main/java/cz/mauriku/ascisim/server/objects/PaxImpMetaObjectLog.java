package cz.mauriku.ascisim.server.objects;

import cz.mauriku.ascisim.server.objects.client.PlayerAccount;

import java.time.Instant;
import java.util.UUID;

public class PaxImpMetaObjectLog {

  private String id;
  private Instant logDate;
  private String metaObjectId;
  private String accountId;
  private PaxImpMetaObjectLogType type;

  private transient PlayerAccount account;

  public PaxImpMetaObjectLog() {
    this.id = UUID.randomUUID().toString();
  }

  public PaxImpMetaObjectLog(String metaObjectId, PaxImpMetaObjectLogType type, PlayerAccount account) {
    this.id = UUID.randomUUID().toString();
    this.metaObjectId = metaObjectId;
    this.type = type;
    this.account = account;
    this.accountId = account.getId();
    this.logDate = Instant.now();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PlayerAccount getAccount() {
    return account;
  }

  public void setAccount(PlayerAccount account) {
    this.account = account;
    this.accountId = account.getId();
  }

  public Instant getLogDate() {
    return logDate;
  }

  public void setLogDate(Instant logDate) {
    this.logDate = logDate;
  }

  public String getMetaObjectId() {
    return metaObjectId;
  }

  public void setMetaObjectId(String metaObjectId) {
    this.metaObjectId = metaObjectId;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public PaxImpMetaObjectLogType getType() {
    return type;
  }

  public void setType(PaxImpMetaObjectLogType type) {
    this.type = type;
  }
}
