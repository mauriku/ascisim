package cz.mauriku.ascisim.server.objects.client;

import cz.mauriku.ascisim.server.objects.WorldObject;

import java.util.UUID;

public class PlayerClient implements WorldObject {

  private String id;
  private int authenticationToken;

  public PlayerClient() {
    id = UUID.randomUUID().toString();
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getAuthenticationToken() {
    return authenticationToken;
  }

  public void setAuthenticationToken(int authenticationToken) {
    this.authenticationToken = authenticationToken;
  }
}
