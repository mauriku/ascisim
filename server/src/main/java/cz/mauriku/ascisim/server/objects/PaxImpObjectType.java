package cz.mauriku.ascisim.server.objects;

public enum PaxImpObjectType {
  UNIVERSE("UNV"),
  SECTOR ("SEC"),
  CORPUSCULE("CRP"),
  LOCATION ("LOC"),
  CHARACTER ("PCH");

  String idPrefix;

  PaxImpObjectType(String idPrefix) {
    this.idPrefix = idPrefix;
  }

  public String getIdPrefix() {
    return idPrefix;
  }
}
