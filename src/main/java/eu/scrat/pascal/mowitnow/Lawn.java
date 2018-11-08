package eu.scrat.pascal.mowitnow;

import static com.google.common.base.Preconditions.checkNotNull;

public class Lawn {
  private Coordinate bottomLeftCorner;
  private Coordinate upperRightCorner;

  public Lawn(Coordinate point1, Coordinate point2) throws IllegalArgumentException {
    checkNotNull(point1, "A lawn requires a first coordinate to specify a rectangle");
    checkNotNull(point2, "A lawn requires a second coordinate to specify a rectangle");
    if (point1.getX() == point2.getX()) {
      throw new IllegalArgumentException("Coordinates form a vertical line, not a grid");
    } else if (point1.getY() == point2.getY()) {
      throw new IllegalArgumentException("Coordinates form a horizontal line, not a grid");

    } else {
      bottomLeftCorner = new Coordinate(Math.min(point1.getX(), point2.getX()),
          Math.min(point1.getY(), point2.getY()));
      upperRightCorner = new Coordinate(Math.max(point1.getX(), point2.getX()),
          Math.max(point1.getY(), point2.getY()));
    }
  }

  public Coordinate getBottomLeftCorner() {
    return bottomLeftCorner;
  }

  public Coordinate getUpperRightCorner() {
    return upperRightCorner;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != Lawn.class) {
      return false;
    }
    Lawn otherLawn = (Lawn) obj;
    if (this.getBottomLeftCorner().getX() == otherLawn.getBottomLeftCorner().getX()
        && this.getBottomLeftCorner().getY() == otherLawn.getBottomLeftCorner().getY()
        && this.getUpperRightCorner().getX() == otherLawn.getUpperRightCorner().getX()
        && this.getUpperRightCorner().getY() == otherLawn.getUpperRightCorner().getY()) {
      return true;
    } else {
      return false;
    }
  }
}
