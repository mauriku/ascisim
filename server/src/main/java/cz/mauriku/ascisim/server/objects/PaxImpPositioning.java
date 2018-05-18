package cz.mauriku.ascisim.server.objects;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class PaxImpPositioning {
  
  @QuerySqlField(index = true, notNull = true)
  private Point point;
  private String objectId;
  private PaxImpObjectType type;

  public PaxImpPositioning() {
  }

  public PaxImpPositioning(PaxImpObject object, int x, int y) {
    GeometryFactory fac = new GeometryFactory();
    point = fac.createPoint(new Coordinate(x, y));
    objectId = object.getId();
    type = object.getMetaObject().getType();
  }

  public Point getPoint() {
    return point;
  }

  public void setPoint(Point point) {
    this.point = point;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public PaxImpObjectType getType() {
    return type;
  }

  public void setType(PaxImpObjectType type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PaxImpPositioning that = (PaxImpPositioning) o;

    if (!getPoint().equals(that.getPoint())) return false;
    if (!getObjectId().equals(that.getObjectId())) return false;
    return getType() == that.getType();
  }

  @Override
  public int hashCode() {
    int result = getPoint().hashCode();
    result = 31 * result + getObjectId().hashCode();
    result = 31 * result + getType().hashCode();
    return result;
  }
}
