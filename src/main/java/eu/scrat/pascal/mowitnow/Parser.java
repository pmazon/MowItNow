package eu.scrat.pascal.mowitnow;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.annotations.VisibleForTesting;

public class Parser {
  private static final Pattern lawnPattern =
      Pattern.compile("(?<signX>[-+]?)(?<x>\\d+) (?<signY>[-+]?)(?<y>\\d+)");
  private static final Pattern mowerPattern =
      Pattern.compile("(?<signX>[-+]?)(?<x>\\d+) (?<signY>[-+]?)(?<y>\\d+) (?<orientation>[NESW])");

  public static Lawn buildLawn(String lawnLine) {
    Matcher matcher = lawnPattern.matcher(lawnLine);
    if (!matcher.matches()) {
      throw new IllegalArgumentException(
          "Provided string does not match a Lawn's coordinates, e.g: '-1 3'");
    }
    return new Lawn(new Coordinate(0, 0),
        new Coordinate(Integer.parseInt(matcher.group("signX") + matcher.group("x")),
            Integer.parseInt(matcher.group("signY") + matcher.group("y"))));
  }

  @VisibleForTesting
  static Lawn parseLawn(Scanner scanner) throws ParseException, IllegalArgumentException {
    if (!scanner.hasNextLine()) {
      throw new ParseException("Scanner has no line for lawn parsing");
    }
    String line = scanner.nextLine();
    try {
      return buildLawn(line);
    } catch (IllegalArgumentException exception) {
      throw new ParseException(
          "First line should be a Lawn's coordinates (e.g.: '4 7'), got \"AAAAAA\"", exception);
    }
  }

  public static Mower buildMower(String mowerLine) {
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

  @VisibleForTesting
  static Mower parseMower(String line) throws ParseException {
    try {
      return buildMower(line);
    } catch (IllegalArgumentException exception) {
      throw new ParseException(
          "Line should be a Mower specification (e.g.: '1 3 W'), got \"" + line + "\"", exception);
    }
  }

  public static List<Direction> buildDirections(String directionLine) {
    return Stream.of(directionLine.split("")).map(dir -> Direction.valueOf(dir))
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  static List<Direction> parseDirections(String line) throws ParseException {
    try {
      return buildDirections(line);
    } catch (IllegalArgumentException exception) {
      throw new ParseException(
          "Line should be a list of directions (e.g.: 'AADGGDA'), got \"" + line + "\"", exception);
    }
  }

  public static Yard parseFile(String filename) throws FileNotFoundException, ParseException {
    checkNotNull(filename, "The file name is required");
    List<Mower> mowers = new ArrayList<>();
    Lawn lawn;

    try (Scanner scanner = new Scanner(new FileReader(filename))) {
      lawn = parseLawn(scanner);

      Optional<Mower> optionalMower = Optional.empty();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("")) {
          continue; // Silently ignore empty lines
        }
        if (!optionalMower.isPresent()) {
          Mower mower = parseMower(line);
          mowers.add(mower);
          optionalMower = Optional.of(mower);
        } else {
          List<Direction> directions = parseDirections(line);
          optionalMower.get().setDirections(directions);
          optionalMower = Optional.empty();
        }
      }
    }
    try {
      return new Yard(lawn, mowers);
    } catch (IllegalArgumentException exception) {
      throw new ParseException("Some mowers have identical initial position", exception);
    }
  }
}
