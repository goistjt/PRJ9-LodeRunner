package loderunner;

import java.util.ArrayList;

/**
 * Creates the drawable level of the game.
 *
 * @author goistjt.
 *         Created May 12, 2014.
 */
public interface DrawableLevel {
	/**
	 * Gets a list of all drawable objects in the game.
	 *
	 * @return an array list of drawable objects.
	 */
	public ArrayList<Drawable> getDrawableObjects();

}
