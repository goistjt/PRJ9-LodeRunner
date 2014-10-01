package loderunner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * Panel for the LodeRunner that paints all of the drawable objects on the frame
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class LodeRunnerPanel extends JPanel {

	private static final Dimension SIZE = new Dimension(1200, 800);
	/**
	 * Number of rows on the screen.
	 */
	protected static final int ROWS = 22;
	/**
	 * Number of columns on the screen.
	 */
	protected static final int COLUMNS = 32;
	/**
	 * Height of each row.
	 */
	protected static final int ROW_HEIGHT = (SIZE.height) / ROWS;
	/**
	 * Width of each column.
	 */
	protected static final int COLUMN_WIDTH = (SIZE.width) / COLUMNS;

	private ArrayList<Drawable> objects;
	
	/**
	 * Basic constructor for the panel.
	 *
	 */
	public LodeRunnerPanel() {
		this.setPreferredSize(SIZE);
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
	}
	
	/**
	 * paints all of the drawable objects from the drawable level on the panel.
	 *
	 * @param level
	 */
	public void paint(DrawableLevel level) {
		this.objects = level.getDrawableObjects();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// Draws grid
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				Rectangle2D.Double grid = new Rectangle2D.Double(COLUMN_WIDTH
						* j, ROW_HEIGHT * i, COLUMN_WIDTH, ROW_HEIGHT);
				g2.draw(grid);
			}
		}

		// Draws terrain
		for(Drawable object : this.objects) {
			object.drawOn(g2);
		}
	}

}
