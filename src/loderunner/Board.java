package loderunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Creates a board terrain object
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Board extends Terrain {

	/**
	 * Constructs a board at the given x and y coordinates
	 *
	 * @param x
	 * @param y
	 */
	public Board(int x, int y) {
		super(x, y);
	}

	// Drawable Interface
	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = COLUMN_WIDTH;
		int row_h = ROW_HEIGHT;
		
		Image board = Toolkit.getDefaultToolkit().getImage(
				"src/images/board.jpg");
		g2.drawImage(board, this.location.getColumn() * col_w,
				this.location.getRow() * row_h, col_w, row_h, null);
		
		g2.dispose();

	}

	@Override
	protected boolean isRunnable() {
		// Returns whether or not hero can run through space this object occupies
		return false;
	}

}
