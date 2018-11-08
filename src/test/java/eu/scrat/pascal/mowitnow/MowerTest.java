package eu.scrat.pascal.mowitnow;

import static eu.scrat.pascal.mowitnow.Direction.A;
import static eu.scrat.pascal.mowitnow.Direction.D;
import static eu.scrat.pascal.mowitnow.Direction.G;
import static eu.scrat.pascal.mowitnow.Orientation.E;
import static eu.scrat.pascal.mowitnow.Orientation.N;
import static eu.scrat.pascal.mowitnow.Orientation.S;
import static eu.scrat.pascal.mowitnow.Orientation.W;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class MowerTest {
  @Test
  public void mowersAreEquals() {
    Mower mower = new Mower(1, 3, N);
    assertEquals(new Mower(1, 3, N), mower);
  }

  @Test
  public void mowersAreNotEqual() {
    Mower mower1 = new Mower(1, 3, N);
    Mower mower2 = new Mower(2, 2, E);
    assertNotEquals(mower1, mower2);
  }

  @Test
  public void mowerNotEqualsToNull() {
    assertFalse(Objects.equals(null, new Mower(1, 3, N)));
    assertFalse(Objects.equals(new Mower(1, 3, N), null));
  }

  @Test
  public void mowerThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> new Mower(1, 3, null));
  }

  @Test
  public void checkConstructor() {
    Mower mower = new Mower(1, 3, N);
    assertEquals(new Coordinate(1, 3), mower.getLocalisation());
    assertEquals(N, mower.getOrientation());
  }

  @Test
  public void displayMowerWithSpaces() {
    assertEquals("1 3 N", (new Mower(1, 3, N)).toString());
  }

  @Test
  public void movingWithoutYardThrowsNullPointerException() {
    Throwable exception =
        assertThrows(NullPointerException.class, () -> new Mower(1, 3, E).move(null, A));
    assertEquals("Moving requires a yard to mow", exception.getMessage());
  }

  @Test
  public void movingWithNullDirectionThrowsNullPointerException() {
    Mower mower = new Mower(1, 3, E);
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Yard yard = new Yard(lawn, List.of(mower));
    Throwable exception =
        assertThrows(NullPointerException.class, () -> new Mower(1, 3, E).move(yard, null));
    assertEquals("Moving requires a direction", exception.getMessage());
  }

  @Test
  public void stayStationaryWhenMowingWithEmptyDirections() {
    Mower mower = new Mower(1, 3, E);
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Yard yard = new Yard(lawn, List.of(mower));
    mower.mow(yard);
    assertEquals(new Mower(1, 3, E), mower);
  }

  @Test
  public void increaseXWhenMovingEast() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, E);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(2, 3, E), mower);
  }

  @Test
  public void decreaseYWhenMovingSouth() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, S);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(1, 2, S), mower);
  }

  @Test
  public void decreaseXWhenMovingWest() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, W);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(0, 3, W), mower);
  }

  @Test
  public void increaseYWhenMovingNorth() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, N);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(1, 4, N), mower);
  }

  @Test
  public void stayStationaryOnNorthBorder() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 7, N);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(1, 7, N), mower);
  }

  @Test
  public void stayStationaryOnSouthBorder() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, -3, S);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(1, -3, S), mower);
  }

  @Test
  public void stayStationaryOnEastBorder() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(7, 3, E);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(7, 3, E), mower);
  }

  @Test
  public void stayStationaryOnWestBorder() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(-3, 3, W);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, A);
    assertEquals(new Mower(-3, 3, W), mower);
  }

  @Test
  public void movingLeftFromEastGivesNorth() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, E);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, G);
    assertEquals(new Mower(1, 3, N), mower);
  }

  @Test
  public void movingLeftFromNorthGivesWest() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, N);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, G);
    assertEquals(new Mower(1, 3, W), mower);
  }

  @Test
  public void movingLeftFromWestGivesSouth() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, W);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, G);
    assertEquals(new Mower(1, 3, S), mower);
  }

  @Test
  public void movingLeftFromSouthGivesEast() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, S);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, G);
    assertEquals(new Mower(1, 3, E), mower);
  }

  @Test
  public void movingRightFromEastGivesSouth() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, E);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, D);
    assertEquals(new Mower(1, 3, S), mower);
  }

  @Test
  public void movingRightFromSouthGivesWest() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, S);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, D);
    assertEquals(new Mower(1, 3, W), mower);
  }

  @Test
  public void movingRightFromWestGivesNorth() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, W);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, D);
    assertEquals(new Mower(1, 3, N), mower);
  }

  @Test
  public void movingRightFromNorthGivesEast() {
    Lawn lawn = new Lawn(new Coordinate(-3, -3), new Coordinate(7, 7));
    Mower mower = new Mower(1, 3, N);
    Yard yard = new Yard(lawn, List.of(mower));
    mower.move(yard, D);
    assertEquals(new Mower(1, 3, E), mower);
  }
}
