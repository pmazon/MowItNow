package eu.scrat.pascal.mowitnow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class LawnTest {
  private static final Coordinate BottomLeft = new Coordinate(-2, -3);
  private static final Coordinate BottomRight = new Coordinate(8, -3);
  private static final Coordinate UpperLeft = new Coordinate(-2, 7);
  private static final Coordinate UpperRight = new Coordinate(8, 7);

  @Test
  public void lawnsAreEquals() {
    Lawn lawn = new Lawn(new Coordinate(0, 0), new Coordinate(5, 7));
    assertNotEquals(new Lawn(new Coordinate(0, 0), new Coordinate(2, 2)), lawn);
    assertEquals(new Lawn(new Coordinate(0, 0), new Coordinate(5, 7)), lawn);
  }

  @Test
  public void lawnNotEqualsToNull() {
    assertFalse(Objects.equals(null, new Lawn(new Coordinate(0, 0), new Coordinate(7, 4))));
    assertFalse(Objects.equals(new Lawn(new Coordinate(0, 0), new Coordinate(7, 4)), null));
  }

  @Test
  public void verticalLineThrowsIllegalArgumentException() {
    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> new Lawn(BottomLeft, UpperLeft));
    assertEquals("Coordinates form a vertical line, not a grid", exception.getMessage());
  }

  @Test
  public void horizontalLineThrowsIllegalArgumentException() {
    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> new Lawn(UpperRight, UpperLeft));
    assertEquals("Coordinates form a horizontal line, not a grid", exception.getMessage());
  }

  @Test
  public void invertedPointsShouldWork() {
    Lawn lawn = new Lawn(UpperLeft, BottomRight);
    assertEquals(lawn.getBottomLeftCorner(), BottomLeft);
    assertEquals(lawn.getUpperRightCorner(), UpperRight);
  }

  @Test
  public void standardPointsShouldWork() {
    Lawn lawn = new Lawn(BottomLeft, UpperRight);
    assertEquals(lawn.getBottomLeftCorner(), BottomLeft);
    assertEquals(lawn.getUpperRightCorner(), UpperRight);
  }
}
