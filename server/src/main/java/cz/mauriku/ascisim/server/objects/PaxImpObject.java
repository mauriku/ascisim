package cz.mauriku.ascisim.server.objects;

import java.util.Map;

public class PaxImpObject {

  public static final PaxImpProperty<String> NAME = new PaxImpProperty<>("name", true, String.class);

  // persistent
  protected String id;
  protected String metaObjectId;
  protected Map<String, Object> propertyOverride;
  protected Map<String, String> actionCodeOverride;

  protected transient PaxImpMetaObject metaObject;

  public <T> T getObjectProperty(PaxImpProperty<T> property) {
    if (propertyOverride.containsKey(property.getName()))
      return property.getType().cast(propertyOverride.get(property.getName()));
    else
      return metaObject.getMetaObjectProperty(property);
  }

  public <T> void setObjectProperty(PaxImpProperty<T> property, T value) {
    if (property.isReadOnly())
      throw new IllegalStateException("Cannot override read-only property");

    this.propertyOverride.put(property.getName(), value);
  }
}
