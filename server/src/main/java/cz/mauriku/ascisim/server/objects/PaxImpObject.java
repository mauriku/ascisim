package cz.mauriku.ascisim.server.objects;

import cz.mauriku.ascisim.server.objects.world.Position;

import java.util.HashMap;
import java.util.Map;

public class PaxImpObject {

  public static final PaxImpProperty<String> NAME = new PaxImpProperty<>("name", true, String.class);
  public static final PaxImpProperty<String> DESCRIPTION = new PaxImpProperty<>("description", true, String.class);
  public static final PaxImpProperty<Position> POSITION = new PaxImpProperty<>("position", false, Position.class);

  public static final PaxImpProperty<String> UNIVERSE_ID = new PaxImpProperty<>("universeId", true, String.class);
  public static final PaxImpProperty<String> SECTOR_ID = new PaxImpProperty<>("sectorId", true, String.class);
  public static final PaxImpProperty<String> CORPUSCULE_ID = new PaxImpProperty<>("corpusculeId", true, String.class);
  public static final PaxImpProperty<String> LOCATION_ID = new PaxImpProperty<>("locationId", true, String.class);
  public static final PaxImpProperty<Character> CHAR = new PaxImpProperty<>("char", true, Character.class);

  public static final PaxImpProperty<Integer> LEVEL = new PaxImpProperty<>("level", false, Integer.class);

  // persistent
  protected String id;
  protected String metaObjectId;
  protected Map<String, Object> propertyOverride;
  protected Map<String, String> actionCodeOverride;

  protected transient PaxImpMetaObject metaObject;

  public PaxImpObject() {
    
  }

  public PaxImpObject(PaxImpMetaObject metaObject) {
    this.metaObject = metaObject;
    this.metaObjectId = metaObject.getId();
  }

  public <T> T getObjectProperty(PaxImpProperty<T> property) {
    if (propertyOverride != null && propertyOverride.containsKey(property.getName()))
      return property.getType().cast(propertyOverride.get(property.getName()));
    else
      return metaObject.getMetaObjectProperty(property);
  }

  public <T> void setObjectProperty(PaxImpProperty<T> property, T value) {
    if (property.isReadOnly())
      throw new IllegalStateException("Cannot override read-only property");
    if (propertyOverride == null)
      propertyOverride = new HashMap<>();
    
    this.propertyOverride.put(property.getName(), value);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMetaObjectId() {
    return metaObjectId;
  }

  public void setMetaObjectId(String metaObjectId) {
    this.metaObjectId = metaObjectId;
  }

  public Map<String, Object> getPropertyOverride() {
    return propertyOverride;
  }

  public void setPropertyOverride(Map<String, Object> propertyOverride) {
    this.propertyOverride = propertyOverride;
  }

  public Map<String, String> getActionCodeOverride() {
    return actionCodeOverride;
  }

  public void setActionCodeOverride(Map<String, String> actionCodeOverride) {
    this.actionCodeOverride = actionCodeOverride;
  }

  public PaxImpMetaObject getMetaObject() {
    return metaObject;
  }

  public void setMetaObject(PaxImpMetaObject metaObject) {
    this.metaObject = metaObject;
  }
}
