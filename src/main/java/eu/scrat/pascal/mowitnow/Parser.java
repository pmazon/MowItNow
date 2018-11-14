package eu.scrat.pascal.mowitnow;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
  private static final Pattern lawnPattern =
      Pattern.compile("(?<signX>[-+]?)(?<x>\\d+) (?<signY>[-+]?)(?<y>\\d+)");
  private static final Pattern mowerPattern =
      Pattern.compile("(?<signX>[-+]?)(?<x>\\d+) (?<signY>[-+]?)(?<y>\\d+) (?<orientation>[NESW])");
  private static final Pattern directionsPattern = Pattern.compile("(?<directions>[AGD]+)");

  public static boolean isLawnLine(String line) {
    if (line == null) {
      return false;
    }
    Matcher matcher = lawnPattern.matcher(line);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isMowerLine(String line) {
    if (line == null) {
      return false;
    }
    Matcher matcher = mowerPattern.matcher(line);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isDirectionsLine(String line) {
    if (line == null) {
      return false;
    }
    Matcher matcher = directionsPattern.matcher(line);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }

  public static Lawn parseLawn(String lawnLine) {
    Matcher matcher = lawnPattern.matcher(lawnLine);
    if (!matcher.matches()) {
      throw new IllegalArgumentException(
          "Provided string does not match a Lawn's coordinates, e.g: '-1 3'");
    }
    return new Lawn(new Coordinate(0, 0),
        new Coordinate(Integer.parseInt(matcher.group("signX") + matcher.group("x")),
            Integer.parseInt(matcher.group("signY") + matcher.group("y"))));
  }

  public static Mower parseMower(String mowerLine) {
    checkNotNull(mowerLine, "The mower line is required");
    Matcher matcher = mowerPattern.matcher(mowerLine);
    if (!matcher.matches()) {
      throw new IllegalArgumentException(
          "Provided string does not match a Mower's info, e.g: '3 4 N'");
    }
    int x = Integer.parseInt(matcher.group("signX") + matcher.group("x"));
    int y = Integer.parseInt(matcher.group("signY") + matcher.group("y"));
    Orientation orientation = Orientation.valueOf(matcher.group("orientation"));
    return new Mower(x, y, orientation);
  }

  public static List<Direction> parseDirections(String directionLine) {
    return Stream.of(directionLine.split("")).map(dir -> Direction.valueOf(dir))
        .collect(Collectors.toList());
  }

  public static Yard parseFile(String filename) throws FileNotFoundException, ParseException {
    checkNotNull(filename, "The file name is required");
    List<Mower> mowers = new ArrayList<>();
    Lawn lawn = null;
    Yard yard = null;

    try (Scanner scanner = new Scanner(new FileReader(filename))) {
      if (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (!isLawnLine(line)) {
          throw new ParseException(
              "First line should be a Lawn's coordinates (e.g.: '4 7'), got \"" + line + "\"");
        }
        lawn = parseLawn(line);
      } else {
        throw new ParseException("File is empty");
      }

      boolean expectedMowerLine = true;
      Mower mower = null;
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("")) {
          // Silently ignore empty lines
          continue;
        }
        if (expectedMowerLine) {
          try {
            mower = parseMower(line);
            mowers.add(mower);
            expectedMowerLine = false;
          } catch (IllegalArgumentException exception) {
            throw new ParseException(
                "Line should be a Mower specification (e.g.: '1 3 W'), got \"" + line + "\"",
                exception);
          }
        } else {
          // expecting a list of directions
          try {
            List<Direction> directions = parseDirections(line);
            mower.setDirections(directions);
            expectedMowerLine = true;
          } catch (IllegalArgumentException exception) {
            throw new ParseException(
                "Line should be a list of directions (e.g.: 'AADGGDA'), got \"" + line + "\"",
                exception);
          }
        }
      }
    }
    try {
      yard = new Yard(lawn, mowers);
    } catch (IllegalArgumentException exception) {
      throw new ParseException("Some mowers have identical initial position", exception);
    }
    return yard;
  }
}
