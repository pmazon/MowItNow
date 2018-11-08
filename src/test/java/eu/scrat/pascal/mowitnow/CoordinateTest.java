package eu.scrat.pascal.mowitnow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class CoordinateTest {

  private Coordinate coordinate;

  @BeforeEach
  public void setStartingCoordinate() {
    coordinate = new Coordinate(-3, 7);
  }

  @Test
  public void coordinatesAreEquals() {
    Coordinate coordinate = new Coordinate(3, 4);
    assertNotEquals(new Coordinate(2, 2), coordinate);
    assertEquals(new Coordinate(3, 4), coordinate);
  }

  @Test
  public void coordinatesNotEqualsToNull() {
    assertFalse(Objects.equals(null, new Coordinate(3, 4)));
    assertFalse(Objects.equals(new Coordinate(3, 4), null));
  }

  @Test
  public void displayWithSpace() {
    assertEquals("-3 7", coordinate.toString());
  }

  @Test
  public void YShouldIncreaseWhenMovingUp() {
    coordinate.goUp();
    assertEquals(-3, coordinate.getX());
    assertEquals(8, coordinate.getY());
  }

  @Test
  public void YShouldDecreaseWhenMovingDown() {
    coordinate.goDown();
    assertEquals(-3, coordinate.getX());
    assertEquals(6, coordinate.getY());
  }

  @Test
  public void XShouldIncreaseWhenMovingRight() {
    coordinate.goRight();
    assertEquals(-2, coordinate.getX());
    assertEquals(7, coordinate.getY());
  }

  @Test
  public void XShouldDecreaseWhenMovingLeft() {
    coordinate.goLeft();
    assertEquals(-4, coordinate.getX());
    assertEquals(7, coordinate.getY());
  }
}
