package loderunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Creates a brick terrain object.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Brick extends Terrain {

	/**
	 * Constructs a brick at the given x and y coordinates
	 *
	 * @param x
	 * @param y
	 */
	public Brick(int x, int y) {
		super(x, y);
	}

	// Terrain abstract methods that need overridden
	@Override
	protected boolean isDiggable() {
		// Only bricks are diggable by Hero
		return true;
	}

	// Drawable Interface
	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = COLUMN_WIDTH;
		int row_h = ROW_HEIGHT;

		Image brick = Toolkit.getDefaultToolkit().getImage(
				"src/images/brick.jpg");
		g2.drawImage(brick, this.location.getColumn() * col_w,
				this.location.getRow() * row_h, col_w, row_h, null);
		
		g2.dispose();

	}

	@Override
	protected boolean isRunnable() {
		// Returns whether or not hero can run through the space this object occupies
		return false;
	}

}
