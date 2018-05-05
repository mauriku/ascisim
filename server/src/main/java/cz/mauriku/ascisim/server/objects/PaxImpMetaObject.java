package cz.mauriku.ascisim.server.objects;

import cz.mauriku.ascisim.server.objects.client.PlayerAccount;

import java.time.Instant;
import java.util.*;

public class PaxImpMetaObject {
  
  private String id;
  private Instant createdDate;
  private PaxImpObjectType type;
  private Map<String, Object> property;
  private Map<String, String> actionCode;
  private String authorAccountId;
  private transient List<PaxImpMetaObjectLog> log;

  private transient PlayerAccount authorAccount;

  public PaxImpMetaObject() {
    id = "META_" + UUID.randomUUID().toString();
    property = new HashMap<>();
    actionCode = new HashMap<>();
    log = new ArrayList<>();
  }

  public PaxImpObject createNewObject() {
    return null;
  }

  public <T> T getMetaObjectProperty(PaxImpProperty<T> property) {
    if (this.property.containsKey(property.getName()))
      return property.getType().cast(this.property.get(property.getName()));

    return null;
  }

  public <T> void setMetaObjectProperty(PaxImpProperty<T> property, T value) {
    if (property.isReadOnly() && this.property.containsKey(property.getName()))
      throw new IllegalStateException("Cannot override read-only property");

    this.property.put(property.getName(), value);
  }

  public String getAuthorAccountId() {
    return authorAccountId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Instant getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }

  public PaxImpObjectType getType() {
    return type;
  }

  public void setType(PaxImpObjectType type) {
    this.type = type;
  }

  public Map<String, Object> getProperty() {
    return property;
  }

  public void setProperty(Map<String, Object> property) {
    this.property = property;
  }

  public Map<String, String> getActionCode() {
    return actionCode;
  }

  public void setActionCode(Map<String, String> actionCode) {
    this.actionCode = actionCode;
  }

  public List<PaxImpMetaObjectLog> getLog() {
    return log;
  }

  public void setLog(List<PaxImpMetaObjectLog> log) {
    this.log = log;
  }

  public PlayerAccount getAuthorAccount() {
    return authorAccount;
  }

  public void setAuthorAccount(PlayerAccount authorAccount) {
    this.authorAccount = authorAccount;
    this.authorAccountId = authorAccount.getId();
  }
}
