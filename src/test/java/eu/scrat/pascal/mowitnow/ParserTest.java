package eu.scrat.pascal.mowitnow;

import static eu.scrat.pascal.mowitnow.Direction.A;
import static eu.scrat.pascal.mowitnow.Direction.D;
import static eu.scrat.pascal.mowitnow.Direction.G;
import static eu.scrat.pascal.mowitnow.Orientation.E;
import static eu.scrat.pascal.mowitnow.Orientation.N;
import static eu.scrat.pascal.mowitnow.Orientation.S;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.FileNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class ParserTest {
  @Test
  public void returnDirections() {
    String instructions = "GAADDADGGGADAAAAA";
    List<Direction> directions = List.of(G, A, A, D, D, A, D, G, G, G, A, D, A, A, A, A, A);
    assertEquals(directions, Parser.parseDirections(instructions));
  }

  @Test
  public void wrongDirectionsThrowsIllegalArgumentException() {
    String instructions = "AAADDDZZZZZZ";
    assertThrows(IllegalArgumentException.class, () -> Parser.parseDirections(instructions));
  }

  @Test
  public void nullDirectionsThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> Parser.parseDirections(null));
  }

  @Test
  public void returnLawn() {
    assertEquals(new Lawn(new Coordinate(0, 0), new Coordinate(5, 5)), Parser.buildLawn("5 5"));
  }

  @Test
  public void wrongLawnThrowsIllegalArgumentException() {
    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> Parser.buildLawn("1 3 N"));
    assertEquals("Provided string does not match a Lawn's coordinates, e.g: '-1 3'",
        exception.getMessage());
  }

  @Test
  public void nullStringLawnThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> Parser.buildLawn(null));
  }

  @Test
  public void returnMower() {
    assertEquals(new Mower(1, 3, S), Parser.parseMower("1 3 S"));
  }

  @Test
  public void wrongMowerThrowsIllegalArgumentException() {
    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> Parser.parseMower("1 3"));
    assertEquals("Provided string does not match a Mower's info, e.g: '3 4 N'",
        exception.getMessage());
  }

  @Test
  public void nullStringMowerThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> Parser.parseMower(null));
  }

  @Test
  public void parsingFileWithNullFilenameThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> Parser.parseFile(null));
  }

  @Test
  public void parsingFileWithWrongFilenameThrowsFileNotFoundException() {
    assertThrows(FileNotFoundException.class,
        () -> Parser.parseFile("/Supercali/fragi/li/sti/cexpi/ali/doci/ous"));
  }

  @Test
  public void correctFileParsing() throws FileNotFoundException, ParseException {
    String testInstructions =
        getClass().getClassLoader().getResource("test_instructions.txt").getFile();
    Yard yard = Parser.parseFile(testInstructions);
    assertEquals(new Lawn(new Coordinate(0, 0), new Coordinate(5, 5)), yard.getLawn());
    assertEquals(2, yard.getMowers().size());
    assertEquals(new Mower(new Coordinate(1, 2), N, List.of(G, A, G, A, G, A, G, A, A)),
        yard.getMowers().get(0));
    assertEquals(new Mower(new Coordinate(3, 3), E, List.of(A, A, D, A, A, D, A, D, D, A)),
        yard.getMowers().get(1));
  }

  @Test
  public void ignoreEmptyLinesWhenParsingFile() throws FileNotFoundException, ParseException {
    String testInstructions =
        getClass().getClassLoader().getResource("test_instructions_emptyLines.txt").getFile();
    Yard yard = Parser.parseFile(testInstructions);
    assertEquals(new Lawn(new Coordinate(0, 0), new Coordinate(5, 5)), yard.getLawn());
    assertEquals(2, yard.getMowers().size());
    assertEquals(new Mower(new Coordinate(1, 2), N, List.of(G, A, G, A, G, A, G, A, A)),
        yard.getMowers().get(0));
    assertEquals(new Mower(new Coordinate(3, 3), E, List.of(A, A, D, A, A, D, A, D, D, A)),
        yard.getMowers().get(1));
  }

  @Test
  public void emptyFileThrowsParseException() {
    String filename =
        getClass().getClassLoader().getResource("test_instructions_empty.txt").getFile();
    Throwable exception = assertThrows(ParseException.class, () -> Parser.parseFile(filename));
    assertEquals("Scanner has no line for lawn parsing", exception.getMessage());
  }

  @Test
  public void wrongLawnLineThrowsParseException() {
    String filename =
        getClass().getClassLoader().getResource("test_instructions_wrongLawn.txt").getFile();
    Throwable exception = assertThrows(ParseException.class, () -> Parser.parseFile(filename));
    assertEquals("First line should be a Lawn's coordinates (e.g.: '4 7'), got \"AAAAAA\"",
        exception.getMessage());
  }

  @Test
  public void wrongMowerLineThrowsParseException() {
    String filename =
        getClass().getClassLoader().getResource("test_instructions_wrongMower.txt").getFile();
    Throwable exception = assertThrows(ParseException.class, () -> Parser.parseFile(filename));
    assertEquals("Line should be a Mower specification (e.g.: '1 3 W'), got \"zzzzz\"",
        exception.getMessage());
  }

  @Test
  public void wrongDirectionsLineThrowsParseException() {
    String filename =
        getClass().getClassLoader().getResource("test_instructions_wrongDirections.txt").getFile();
    Throwable exception = assertThrows(ParseException.class, () -> Parser.parseFile(filename));
    assertEquals("Line should be a list of directions (e.g.: 'AADGGDA'), got \"ZZZZZZZZ\"",
        exception.getMessage());
  }

  @Test
  public void overlappingMowersInFile() throws FileNotFoundException, ParseException {
    String filename = getClass().getClassLoader()
        .getResource("test_instructions_overlappingMowers.txt").getFile();

    Throwable exception = assertThrows(ParseException.class, () -> Parser.parseFile(filename));
    assertEquals("Some mowers have identical initial position", exception.getMessage());
  }
}
