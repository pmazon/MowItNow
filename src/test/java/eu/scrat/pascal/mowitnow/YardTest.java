package eu.scrat.pascal.mowitnow;

import static eu.scrat.pascal.mowitnow.Direction.A;
import static eu.scrat.pascal.mowitnow.Direction.D;
import static eu.scrat.pascal.mowitnow.Direction.G;
import static eu.scrat.pascal.mowitnow.Orientation.E;
import static eu.scrat.pascal.mowitnow.Orientation.N;
import static eu.scrat.pascal.mowitnow.Orientation.S;
import static eu.scrat.pascal.mowitnow.Orientation.W;
import static eu.scrat.pascal.mowitnow.Yard.ANSI_BLACK;
import static eu.scrat.pascal.mowitnow.Yard.ANSI_GREEN;
import static eu.scrat.pascal.mowitnow.Yard.ANSI_RED;
import static eu.scrat.pascal.mowitnow.Yard.ANSI_RESET;
import static eu.scrat.pascal.mowitnow.Yard.ANSI_YELLOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class YardTest {
  private Lawn lawn;

  @BeforeEach
  public void initLawn() {
    this.lawn = new Lawn(new Coordinate(0, 0), new Coordinate(8, 8));
  }

  @Test
  public void yardWithOverlappingMowersThrowsIllegalArgumentException() {
    List<Mower> mowers =
        List.of(new Mower(1, 1, N), new Mower(1, 1, E), new Mower(2, 2, S), new Mower(3, 3, W));
    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> new Yard(lawn, mowers));
    assertEquals("Invalid mowers list, some of them are overlapping", exception.getMessage());
  }

  @Test
  public void yardWithoutLawnThrowsNullPointerException() {
    List<Mower> mowers =
        List.of(new Mower(1, 1, N), new Mower(2, 2, E), new Mower(3, 3, S), new Mower(4, 4, W));
    Throwable exception = assertThrows(NullPointerException.class, () -> new Yard(null, mowers));
    assertEquals("A yard needs a lawn", exception.getMessage());
  }

  @Test
  public void yardWithoutMowersThrowsNullPointerException() {
    Throwable exception = assertThrows(NullPointerException.class, () -> new Yard(this.lawn, null));
    assertEquals("A yard needs a list of mowers", exception.getMessage());
  }

  @Test
  public void yardsAreEqual() {
    Lawn lawn1 = new Lawn(new Coordinate(0, 0), new Coordinate(8, 8));
    Lawn lawn2 = new Lawn(new Coordinate(0, 0), new Coordinate(8, 8));
    List<Mower> mowers1 =
        List.of(new Mower(1, 1, N), new Mower(2, 2, E), new Mower(3, 3, S), new Mower(4, 4, W));
    List<Mower> mowers2 =
        List.of(new Mower(1, 1, N), new Mower(2, 2, E), new Mower(3, 3, S), new Mower(4, 4, W));
    assertEquals(new Yard(lawn1, mowers1), new Yard(lawn2, mowers2));
  }

  @Test
  public void yardsAreNotEqual() {
    Lawn lawn1 = new Lawn(new Coordinate(0, 0), new Coordinate(8, 8));
    Lawn lawn2 = new Lawn(new Coordinate(0, 0), new Coordinate(3, 3));
    List<Mower> mowers1 =
        List.of(new Mower(1, 1, N), new Mower(2, 2, E), new Mower(3, 3, S), new Mower(4, 4, W));
    List<Mower> mowers2 = List.of(new Mower(1, 2, S));
    assertNotEquals(new Yard(lawn1, mowers1), new Yard(lawn2, mowers2));
  }

  @Test
  public void yardNotEqualsToNull() {
    List<Mower> mowers = List.of(new Mower(1, 1, N));
    assertFalse(Objects.equals(null, new Yard(this.lawn, mowers)));
    assertFalse(Objects.equals(new Yard(this.lawn, mowers), null));
  }

  @Test
  public void cannotMoveMowerOnAnotherOne() {
    List<Mower> mowers = List.of(new Mower(1, 1, N), new Mower(2, 2, E));
    Yard yard = new Yard(this.lawn, mowers);
    assertFalse(yard.canMoveThere(new Coordinate(2, 2)));
  }

  @Test
  public void canMoveMowerOnEmptySpace() {
    List<Mower> mowers = List.of(new Mower(1, 1, N), new Mower(2, 2, E));
    Yard yard = new Yard(this.lawn, mowers);
    assertTrue(yard.canMoveThere(new Coordinate(1, 2)));
  }

  @Test
  public void cannotMoveMowerOutsideLawnGrid() {
    List<Mower> mowers = List.of(new Mower(1, 1, N), new Mower(2, 2, E));
    Yard yard = new Yard(this.lawn, mowers);
    assertFalse(yard.canMoveThere(new Coordinate(-1, 3)));
    assertFalse(yard.canMoveThere(new Coordinate(3, -1)));
    assertFalse(yard.canMoveThere(new Coordinate(9, 3)));
    assertFalse(yard.canMoveThere(new Coordinate(3, 9)));
  }

  @Test
  public void yardDisplay() {
    List<Mower> mowers =
        List.of(new Mower(0, 0, E), new Mower(3, 4, S), new Mower(2, 2, N), new Mower(1, 2, W));
    Lawn lawn = new Lawn(new Coordinate(0, 0), new Coordinate(5, 5));
    Yard yard = new Yard(lawn, mowers);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("┏━━━━━━┓\n");
    stringBuilder.append("┃      ┃\n");
    stringBuilder.append("┃   " + ANSI_BLACK + "v" + ANSI_RESET + "  ┃\n");
    stringBuilder.append("┃      ┃\n");
    stringBuilder
        .append("┃ " + ANSI_RED + "<" + ANSI_RESET + ANSI_GREEN + "^" + ANSI_RESET + "   ┃\n");
    stringBuilder.append("┃      ┃\n");
    stringBuilder.append("┃" + ANSI_YELLOW + ">" + ANSI_RESET + "     ┃\n");
    stringBuilder.append("┗━━━━━━┛");
    assertEquals(stringBuilder.toString(), yard.toString());
  }

  @Test
  public void mowYard() {
    List<Direction> directions1 = List.of(A, G, A, D, A);
    Mower mower1 = new Mower(new Coordinate(0, 0), N, directions1);
    List<Direction> directions2 = List.of(A, A, D);
    Mower mower2 = new Mower(new Coordinate(1, 1), E, directions2);
    Lawn lawn = new Lawn(new Coordinate(0, 0), new Coordinate(4, 4));
    Yard yard = new Yard(lawn, List.of(mower1, mower2));
    yard.mow();
    Mower mower1AfterMowing = new Mower(new Coordinate(0, 2), N, List.of(A, G, A, D, A));
    Mower mower2AfterMowing = new Mower(new Coordinate(3, 1), S, List.of(A, A, D));
    assertEquals(new Lawn(new Coordinate(0, 0), new Coordinate(4, 4)), yard.getLawn());
    assertEquals(List.of(mower1AfterMowing, mower2AfterMowing), yard.getMowers());
  }
}
