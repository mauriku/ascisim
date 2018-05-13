package cz.mauriku.ascisim.server.objects;

public class PaxImpProperty<T> {
  String name;
  Class<T> type;
  boolean readOnly;
  boolean persistent;

  public PaxImpProperty(String name, boolean readOnly, Class<T> type) {
    this.name = name;
    this.type = type;
    this.readOnly = readOnly;
    this.persistent = true;
  }

  public PaxImpProperty(String name, Class<T> type, boolean readOnly, boolean persistent) {
    this.name = name;
    this.type = type;
    this.readOnly = readOnly;
    this.persistent = persistent;
  }

  public String getName() {
    return name;
  }

  public Class<T> getType() {
    return type;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PaxImpProperty<?> that = (PaxImpProperty<?>) o;

    if (isReadOnly() != that.isReadOnly()) return false;
    if (!getName().equals(that.getName())) return false;
    return getType().equals(that.getType());
  }

  @Override
  public int hashCode() {
    int result = getName().hashCode();
    result = 31 * result + getType().hashCode();
    result = 31 * result + (isReadOnly() ? 1 : 0);
    return result;
  }
}
