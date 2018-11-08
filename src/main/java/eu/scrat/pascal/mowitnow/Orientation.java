package eu.scrat.pascal.mowitnow;

public enum Orientation {
  N("N"), E("E"), S("S"), W("W");
  private final String name;

  Orientation(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
