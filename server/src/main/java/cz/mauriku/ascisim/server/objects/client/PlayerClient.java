package cz.mauriku.ascisim.server.objects.client;

import java.util.UUID;

public class PlayerClient{

  private String id;
  private long authenticationToken;
  private PlayerAccount account;

  public PlayerClient() {
    id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getAuthenticationToken() {
    return authenticationToken;
  }

  public void setAuthenticationToken(long authenticationToken) {
    this.authenticationToken = authenticationToken;
  }

  public PlayerAccount getAccount() {
    return account;
  }

  public void setAccount(PlayerAccount account) {
    this.account = account;
  }
}
