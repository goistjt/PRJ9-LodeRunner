package loderunner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Creates an open block terrain object.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 8, 2014.
 */
public class OpenBlock extends Terrain {

	/**
	 * Constructs an open block at the given x and y coordinates
	 *
	 * @param x
	 * @param y
	 */
	public OpenBlock(int x, int y) {
		super(x, y);

	}

	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = COLUMN_WIDTH;
		int row_h = ROW_HEIGHT;
		Rectangle2D open = new Rectangle2D.Double(this.location.getColumn()
				* col_w, this.location.getRow() * row_h, col_w, row_h);
		g2.setColor(Color.BLACK);
		g2.fill(open);
		g2.draw(open);
		g2.dispose();

	}

	@Override
	protected boolean isRunnable() {
		// Returns whether or not hero can run through the space this object occupies
		return true;
	}

}
