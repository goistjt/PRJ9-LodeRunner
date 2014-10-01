package loderunner;


/**
 * Implements Drawable class. Creates a terrain object.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public abstract class Terrain implements Drawable {
	/**
	 * The location of the terrain object.
	 */
	protected Coordinates location;
	/**
	 * Width of each column.
	 */
	protected static final int COLUMN_WIDTH = LodeRunnerPanel.COLUMN_WIDTH;
	/**
	 * Height of each row.
	 */
	protected static final int ROW_HEIGHT = LodeRunnerPanel.ROW_HEIGHT;
	

	/**
	 * Constructs a terrain object at the given x & y coordinates.
	 *
	 * @param x
	 * @param y
	 */
	public Terrain(int x, int y) {
		this.location = new Coordinates(x, y);
	}

	/**
	 * Checks if the terrain object is diggable.
	 *
	 * @return
	 */
	protected boolean isDiggable() {
		return false;
	}

	/**
	 * Checks if the terrain object is hangable.
	 *
	 * @return
	 */
	protected boolean isHangable() {
		return false;
	}

	/**
	 * Checks if the terrain object is climbable.
	 *
	 * @return
	 */
	protected boolean isClimbable() {
		return false;
	}
	
	/**
	 * Checks if the terrain object is runnable.
	 *
	 * @return
	 */
	protected abstract boolean isRunnable();

}
