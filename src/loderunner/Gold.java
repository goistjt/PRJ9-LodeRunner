package loderunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Creates a gold object.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Gold implements Drawable {
	private Coordinates location;
	private static final int COLUMN_WIDTH = LodeRunnerPanel.COLUMN_WIDTH;
	private static final int ROW_HEIGHT = LodeRunnerPanel.ROW_HEIGHT;

	/**
	 * Constructs a gold object at the given x & y coordinates.
	 *
	 * @param x
	 * @param y
	 */
	public Gold(int x, int y) {
		this.location = new Coordinates(x, y);
	}
	
	/**
	 * Gets the location of the gold piece.
	 *
	 * @return coordinats of the gold
	 */
	public Coordinates getCoordinates() {
		return this.location;
	}

	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = COLUMN_WIDTH;
		int row_h = ROW_HEIGHT;

		Image gold = Toolkit.getDefaultToolkit().getImage(
				"src/images/Retro-Coin-icon.png");
		g2.drawImage(gold, this.location.getColumn() * col_w,
				this.location.getRow() * row_h, col_w, row_h, null);
		
		g2.dispose();
	}

}
