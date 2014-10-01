package loderunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Creates a rope terrain object.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Rope extends Terrain {

	/**
	 * Constructs a rope at the given x and y coordinates
	 *
	 * @param x
	 * @param y
	 */
	public Rope(int x, int y) {
		super(x, y);
	}

	// Terrain abstract methods that need overridden
	@Override
	protected boolean isHangable() {
		// Hero can only fall from ropes
		return true;
	}

	// Drawable Interface
	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = COLUMN_WIDTH;
		int row_h = ROW_HEIGHT;

		Image rope = Toolkit.getDefaultToolkit().getImage(
				"src/images/rope.jpg");
		g2.drawImage(rope, this.location.getColumn() * col_w,
				this.location.getRow() * row_h, col_w, row_h, null);
		
		g2.dispose();

	}

	@Override
	protected boolean isRunnable() {
		// Returns whether or not hero can run through the space this object occupies
		return true;
	}
}
