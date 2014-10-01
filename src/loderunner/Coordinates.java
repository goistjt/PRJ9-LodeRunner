package loderunner;

/**
 * This class represents a pair of coordinates.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Coordinates {
	private int row;
	private int column;

	/**
	 * Constructs a coordinate pair.
	 * 
	 * @param row
	 * @param column
	 */
	public Coordinates(int column, int row) {
		this.row = row;
		this.column = column;
	}

	/**
	 * @return the row of this pair
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * @return the column of this pair
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * Sets the field called 'column' to the given value.
	 * 
	 * @param column
	 *            The column to set.
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * Sets the field called 'row' to the given value.
	 * 
	 * @param row
	 *            The row to set.
	 */
	public void setRow(int row) {
		this.row = row;
	}

}
