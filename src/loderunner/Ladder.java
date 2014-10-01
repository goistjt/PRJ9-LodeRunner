package loderunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Creates a ladder terrain object.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Ladder extends Terrain {

	/**
	 * Constructs a ladder at the given x and y coordinates
	 *
	 * @param x
	 * @param y
	 */
	public Ladder(int x, int y) {
		super(x, y);
	}

	// Terrain abstract methods that need overridden
	@Override
	protected boolean isClimbable() {
		// Hero can only climb up/down ladders
		return true;
	}

	// Drawable Interface
	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = COLUMN_WIDTH;
		int row_h = ROW_HEIGHT;
		
		Image ladder = Toolkit.getDefaultToolkit().getImage(
				"src/images/ladder.jpg");
		g2.drawImage(ladder, this.location.getColumn() * col_w,
				this.location.getRow() * row_h, col_w, row_h, null);
		
		g2.dispose();

	}

	@Override
	protected boolean isRunnable() {
		// Returns whether or not hero can run through the space this object occupies
		return true;
	}
	
	@Override
	protected boolean isHangable() {
		return true; 
	}
}
