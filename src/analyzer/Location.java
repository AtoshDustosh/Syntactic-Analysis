package analyzer;

public class Location {

  private int row = 0;
  private int column = 0;

  public Location(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public void changeLocation(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public void changeRow(int row) {
    this.row = row;
  }

  public void changeColumn(int column) {
    this.column = column;
  }

  public int getRow() {
    return this.row;
  }

  public int getColumn() {
    return this.column;
  }

  @Override
  public String toString() {
    return "(" + this.row + ", " + this.column + ")";
  }
}
