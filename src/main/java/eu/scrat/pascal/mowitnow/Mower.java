package eu.scrat.pascal.mowitnow;

import static com.google.common.base.Preconditions.checkNotNull;
import static eu.scrat.pascal.mowitnow.Direction.D;
import static eu.scrat.pascal.mowitnow.Direction.G;
import static eu.scrat.pascal.mowitnow.Orientation.E;
import static eu.scrat.pascal.mowitnow.Orientation.N;
import static eu.scrat.pascal.mowitnow.Orientation.S;
import static eu.scrat.pascal.mowitnow.Orientation.W;
import java.util.ArrayList;
import java.util.List;

public class Mower {
  private Coordinate localisation;
  private Orientation orientation;
  private List<Direction> directions;

  public Mower(int x, int y, Orientation orientation) {
    this(new Coordinate(x, y), orientation);
  }

  public Mower(Coordinate coordinate, Orientation orientation) {
    this(coordinate, orientation, new ArrayList<Direction>());
  }

  public Mower(Coordinate coordinate, Orientation orientation, List<Direction> directions) {
    this.localisation = checkNotNull(coordinate, "A mower must have a coordinate");
    this.orientation = checkNotNull(orientation, "A mower must have an orientation");
    this.directions = checkNotNull(directions,
        "Use Mower(coordinate, orientation) if no directions are configured");
  }

  public void mow(Yard yard) {
    if (directions == null || directions.isEmpty()) {
      return;
    }
    for (Direction direction : directions) {
      move(yard, direction);
    }
  }

  public void move(Yard yard, Direction direction) {
    checkNotNull(yard, "Moving requires a yard to mow");
    checkNotNull(direction, "Moving requires a direction");
    switch (this.orientation) {
      case N:
        if (direction == G) {
          this.orientation = W;
        } else if (direction == D) {
          this.orientation = E;
        } else if (yard
            .canMoveThere(new Coordinate(localisation.getX(), localisation.getY() + 1))) {
          this.localisation.goUp();
        }
        break;
      case S:
        if (direction == G) {
          this.orientation = E;
        } else if (direction == D) {
          this.orientation = W;
        } else if (yard
            .canMoveThere(new Coordinate(localisation.getX(), localisation.getY() - 1))) {
          this.localisation.goDown();
        }
        break;
      case E:
        if (direction == G) {
          this.orientation = N;
        } else if (direction == D) {
          this.orientation = S;
        } else if (yard
            .canMoveThere(new Coordinate(localisation.getX() + 1, localisation.getY()))) {
          this.localisation.goRight();
        }
        break;
      case W:
        if (direction == G) {
          this.orientation = S;
        } else if (direction == D) {
          this.orientation = N;
        } else if (yard
            .canMoveThere(new Coordinate(localisation.getX() - 1, localisation.getY()))) {
          this.localisation.goLeft();
        }
        break;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != Mower.class) {
      return false;
    }
    Mower otherMower = (Mower) obj;
    if (this.getLocalisation().equals(otherMower.getLocalisation())
        && this.getOrientation() == otherMower.getOrientation()) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return localisation + " " + orientation;
  }

  public Coordinate getLocalisation() {
    return localisation;
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public List<Direction> getDirections() {
    return directions;
  }

  public void setDirections(List<Direction> directions) {
    this.directions = directions;
  }
}
