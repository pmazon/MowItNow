package eu.scrat.pascal.mowitnow;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class AppTest {
  private final InputStream systemIn = System.in;
  private final PrintStream systemOut = System.out;

  private ByteArrayInputStream testIn;
  private ByteArrayOutputStream testOut = new ByteArrayOutputStream();

  private void provideInput(String data) {
    testIn = new ByteArrayInputStream(data.getBytes());
    System.setIn(testIn);
  }

  @BeforeEach
  public void setUpOutput() {
    System.setOut(new PrintStream(testOut));
  }

  @After
  public void restoreSystemInputOutput() {
    System.setIn(systemIn);
    System.setOut(systemOut);
  }

  @Test
  public void checkPromptAndExit() {
    String prompt = "Yard filename to load [defaults to instructions.txt], or 'q' to quit: ";
    String exit = "Exiting.\n";
    provideInput("q");
    App.main(new String[0]);
    assertEquals(prompt + exit, testOut.toString());
  }

  @Test
  public void loadDefaultFile() throws ParseException, FileNotFoundException {
    String instructionsFile = App.class.getClassLoader().getResource("instructions.txt").getFile();
    String prompt = "Yard filename to load [defaults to instructions.txt], or 'q' to quit: ";
    String exit = "Exiting.\n";
    String initial = "---- Initial yard state -----\n";
    String after = "-- Yard state after mowing --\n";

    Yard yard = Parser.parseFile(instructionsFile);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(prompt);
    stringBuilder.append(initial);
    stringBuilder.append(yard.toString());
    stringBuilder.append("\n");
    stringBuilder.append(after);
    yard.mow();
    stringBuilder.append(yard.toString());
    stringBuilder.append("\n\n");
    stringBuilder.append(prompt);
    stringBuilder.append(exit);

    provideInput("\nq");
    App.main(new String[0]);
    assertEquals(stringBuilder.toString(), testOut.toString());
  }
}
