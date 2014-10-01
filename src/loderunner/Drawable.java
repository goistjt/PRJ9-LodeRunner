package loderunner;

import java.awt.Graphics;

/**
 * Creates the draw on function called by all drawable functions.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 8, 2014.
 */
public interface Drawable {
	/**
	 * Draws the object calling this function on the given graphics object.
	 *
	 * @param g
	 */
	public void drawOn(Graphics g);
}
