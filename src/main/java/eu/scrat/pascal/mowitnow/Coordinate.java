package eu.scrat.pascal.mowitnow;

public class Coordinate {
  private int x;
  private int y;

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void goUp() {
    this.y++;
  }

  public void goDown() {
    this.y--;
  }

  public void goRight() {
    this.x++;
  }

  public void goLeft() {
    this.x--;
  }

  @Override
  public String toString() {
    return x + " " + y;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Coordinate)) {
      return false;
    }
    Coordinate coordinate = (Coordinate) obj;
    if (coordinate.getX() != this.getX() || coordinate.getY() != this.getY()) {
      return false;
    }
    return true;
  }
}
