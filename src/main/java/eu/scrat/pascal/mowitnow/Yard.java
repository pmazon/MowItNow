package eu.scrat.pascal.mowitnow;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.List;
import com.google.common.base.Strings;

public class Yard {
  private static final String UL = Character.toString((char) 0x250f); // ┏
  private static final String UR = Character.toString((char) 0x2513); // ┓
  private static final String BL = Character.toString((char) 0x2517); // ┗
  private static final String BR = Character.toString((char) 0x251b); // ┛
  private static final String V = Character.toString((char) 0x2503); // ┃
  private static final String H = Character.toString((char) 0x2501); // ━
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";

  private List<String> colors =
      List.of(ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN);
  private List<Mower> mowers;
  private Lawn lawn;

  public Yard(Lawn lawn, List<Mower> mowers) {
    checkNotNull(mowers, "A yard needs a list of mowers");
    if (overlapping(mowers)) {
      throw new IllegalArgumentException("Invalid mowers list, some of them are overlapping");
    }
    this.lawn = checkNotNull(lawn, "A yard needs a lawn");
    this.mowers = mowers;
  }

  public Lawn getLawn() {
    return lawn;
  }

  public List<Mower> getMowers() {
    return mowers;
  }

  public void mow() {
    for (Mower mower : mowers) {
      mower.mow(this);
    }
  }

  public static boolean overlapping(List<Mower> mowers) {
    for (int i = 0; i < mowers.size(); i++) {
      for (int j = i + 1; j < mowers.size(); j++) {
        if (mowers.get(i).getLocalisation().getX() == mowers.get(j).getLocalisation().getX()
            && mowers.get(i).getLocalisation().getY() == mowers.get(j).getLocalisation().getY()) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean canMoveThere(Coordinate coordinate) {
    for (Mower mower : mowers) {
      if (mower.getLocalisation().equals(coordinate)) {
        return false;
      }
    }
    if (coordinate.getX() > lawn.getUpperRightCorner().getX()
        || coordinate.getX() < lawn.getBottomLeftCorner().getX()
        || coordinate.getY() > lawn.getUpperRightCorner().getY()
        || coordinate.getY() < lawn.getBottomLeftCorner().getY()) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    int width = Math.abs(lawn.getUpperRightCorner().getX() - lawn.getBottomLeftCorner().getX()) + 1;
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(UL + Strings.repeat(H, width) + UR + "\n");
    Iterator<String> colorIterator = colors.iterator();
    for (int j = lawn.getUpperRightCorner().getY(); j >= lawn.getBottomLeftCorner().getY(); j--) {
      stringBuilder.append(V);
      for (int i = lawn.getBottomLeftCorner().getX(); i <= lawn.getUpperRightCorner().getX(); i++) {
        boolean mowerFound = false;
        for (Mower mower : mowers) {
          if (mower.getLocalisation().getX() == i && mower.getLocalisation().getY() == j) {
            String color = null;
            if (!colorIterator.hasNext()) {
              colorIterator = colors.iterator();
            }
            color = colorIterator.next();
            switch (mower.getOrientation()) {
              case E:
                stringBuilder.append(color + ">" + ANSI_RESET);
                break;
              case W:
                stringBuilder.append(color + "<" + ANSI_RESET);
                break;
              case N:
                stringBuilder.append(color + "^" + ANSI_RESET);
                break;
              case S:
                stringBuilder.append(color + "v" + ANSI_RESET);
                break;
            }
            mowerFound = true;
          }
        }
        if (!mowerFound) {
          stringBuilder.append(" ");
        }
      }
      stringBuilder.append(V + "\n");
    }
    stringBuilder.append(BL + Strings.repeat(H, width) + BR);
    return stringBuilder.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != Yard.class) {
      return false;
    }
    Yard otherYard = (Yard) obj;
    if (this.lawn.equals(otherYard.getLawn()) && this.mowers.equals(otherYard.getMowers())) {
      return true;
    }
    return false;
  }
}
