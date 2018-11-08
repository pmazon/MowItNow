package eu.scrat.pascal.mowitnow;

import java.io.FileNotFoundException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  private static final String DEFAULT_YARD =
      App.class.getClassLoader().getResource("instructions.txt").getFile();

  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(App.class);
    Scanner scanner = new Scanner(System.in);
    String filename = null;
    boolean quit = false;
    String input = null;
    while (!quit) {
      try {
        System.out.print("Yard filename to load [defaults to instructions.txt], or 'q' to quit: ");
        input = scanner.nextLine();
        if (input.toUpperCase().equals("Q")) {
          quit = true;
          System.out.println("Exiting.");
          break;
        } else if (input.equals("")) {
          filename = DEFAULT_YARD;
        } else {
          filename = input;
        }
        Yard yard = Parser.parseFile(filename);
        System.out.println("---- Initial yard state -----");
        System.out.println(yard);
        yard.mow();
        System.out.println("-- Yard state after mowing --");
        System.out.println(yard + "\n");
      } catch (FileNotFoundException e) {
        System.out.println("File \"" + filename + "\" not found. Try again.");
        logger.error("File {} not found. Can't simulate the yard", filename, e);
      } catch (ParseException e) {
        System.out.println("File \"" + filename + "\" is not formatted properly.");
        logger.error("File {} is not formatted properly", filename, e);
      }
    }
    scanner.close();
  }
}
