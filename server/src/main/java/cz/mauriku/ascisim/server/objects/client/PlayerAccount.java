package cz.mauriku.ascisim.server.objects.client;

import cz.mauriku.ascisim.server.objects.PaxImpCharacter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PlayerAccount {

  private String password;
  private String email;
  private boolean emailVerified;
  private boolean banned;
  private Instant createdDate;
  private Instant lastLoginDate;
  private String authenticationToken;
  private PlayerAccountLevel level;
  private List<String> characterIds;

  public PlayerAccount() {
    characterIds = new ArrayList<>();
  }

  public List<String> getCharacterIds() {
    return characterIds;
  }

  public void setCharacterIds(List<String> characterIds) {
    this.characterIds = characterIds;
  }

  public String getId() {
    return getEmail();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public boolean isBanned() {
    return banned;
  }

  public void setBanned(boolean banned) {
    this.banned = banned;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Instant getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }

  public Instant getLastLoginDate() {
    return lastLoginDate;
  }

  public void setLastLoginDate(Instant lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  public PlayerAccountLevel getLevel() {
    return level;
  }

  public void setLevel(PlayerAccountLevel level) {
    this.level = level;
  }

  public String getAuthenticationToken() {
    return authenticationToken;
  }

  public void setAuthenticationToken(String authenticationToken) {
    this.authenticationToken = authenticationToken;
  }
}
