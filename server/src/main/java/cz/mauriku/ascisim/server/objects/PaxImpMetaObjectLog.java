package cz.mauriku.ascisim.server.objects;

import java.time.Instant;

public class PaxImpMetaObjectLog {
  
  private Instant logDate;
  private String metaObjectId;
  private String accountId;
  private PaxImpMetaObjectLogType type;

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
