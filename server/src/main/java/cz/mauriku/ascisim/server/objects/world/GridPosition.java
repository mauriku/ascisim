package cz.mauriku.ascisim.server.objects.world;

public class GridPosition {
  long x;
  long y;

  public GridPosition() {
  }

  public GridPosition(long x, long y) {
    this.x = x;
    this.y = y;
  }

  public long getX() {
    return x;
  }

  public long getY() {
    return y;
  }

  public void setX(long x) {
    this.x = x;
  }

  public void setY(long y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GridPosition that = (GridPosition) o;

    if (getX() != that.getX()) return false;
    return getY() == that.getY();
  }

  @Override
  public int hashCode() {
    int result = (int) (getX() ^ (getX() >>> 32));
    result = 31 * result + (int) (getY() ^ (getY() >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "[" + x + "," + y + "]";
  }
}
