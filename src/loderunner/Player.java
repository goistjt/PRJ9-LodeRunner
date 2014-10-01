package loderunner;

/**
 * Player class implements Drawable. Basic functions implemented for all players
 * in the game.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public abstract class Player implements Drawable {
	/**
	 * The location of the player.
	 */
	protected Coordinates location;

	/**
	 * The refresh speed of the player.
	 */
	protected long refreshSpeed;
	/**
	 * Boolean of if the player is falling.
	 */
	protected boolean isFalling;
	/**
	 * A character representing the player's current direction.
	 */
	protected char currentDirection = ' '; 

	/**
	 * The game level.
	 */
	protected LodeRunnerLevel level;

	/**
	 * Player constructor constructs a new player object at the given x & y
	 * coordinates.
	 * 
	 * @param x
	 *            Column location
	 * @param y
	 *            Row location
	 * @param level
	 *            in which the Player exists
	 */
	public Player(int x, int y, LodeRunnerLevel level) {
		this.location = new Coordinates(x, y);
		this.level = level;
		this.isFalling = false;
	}

	/**
	 * Returns the value of the field called 'refreshSpeed'.
	 * 
	 * @return Returns the refreshSpeed.
	 */
	public long getRefreshSpeed() {
		return this.refreshSpeed;
	}

	/**
	 * 
	 * @param player
	 */
	protected void collectGold(Coordinates player) {
		// Override in hero and guard
	}

	/**
	 * returns true if the player has died, false otherwise.
	 * 
	 * @return
	 */
	protected abstract boolean die();

	/**
	 * Implements the players ability to move left as long as they aren't
	 * falling and movement isn't inhibited by Bricks, Boards, or the left side
	 * of the screen.
	 * 
	 */
	protected void runLeft() {
		// set current direction of movement to l for left 
		this.currentDirection = 'l'; 
		Coordinates locationLeft = new Coordinates(
				this.location.getColumn() - 1, this.location.getRow());
		// checks to see if hero is not falling, in which case allows user to
		// control hero
		if (!this.isFalling) {
			// Hero cannot run off of the screen
			if (this.location.getColumn() == 0) {
				return;
			}

			// This case allows hero to Run on ropes
			else if (this.level.getTerrainAtLocation(locationLeft).isRunnable()) {
				this.location.setColumn(this.location.getColumn() - 1);
				// this.x-=this.speed;
				// updateGridLocation();
				this.collectGold(this.location);
			}
		}
	}

	/**
	 * Implements the players ability to move right as long as they aren't
	 * falling and movement isn't inhibited by Bricks, Boards, or the right side
	 * of the screen.
	 * 
	 */
	protected void runRight() {
		// set current direction of movement to r for right 
		this.currentDirection = 'r'; 
		Coordinates locationRight = new Coordinates(
				this.location.getColumn() + 1, this.location.getRow());
		// checks to see if hero is not falling, in which case allows user to
		// control hero
		if (!this.isFalling) {
			// DONE hero can run to the right (but not off of the screen)
			if (this.location.getColumn() == LodeRunnerPanel.COLUMNS - 1) {
				return;
			}
			// Check if terrain to the right of the character is a brick/board
			else if (this.level.getTerrainAtLocation(locationRight)
					.isRunnable()) {
				// Run right
				this.location.setColumn(this.location.getColumn() + 1);
				// this.x+=this.speed;
				// updateGridLocation();
				this.collectGold(this.location);
			}
		}
	}

	/**
	 * Allows the Hero to climb up ladders, but not through bricks/boards/open
	 * space
	 * 
	 */
	protected void climbUp() {
		// set current direction of movement to u for up 
		this.currentDirection = 'u'; 
		// Check to see if at top of screen
		if (this.location.getRow() == 0) {
			return;
		}
		// Climbing up
		Coordinates locationAbove = new Coordinates(this.location.getColumn(),
				this.location.getRow() - 1);
		// Check if terrain above is a brick or board
		if (this.level.getTerrainAtLocation(locationAbove).isClimbable()
				&& this.level.getTerrainAtLocation(this.location).isClimbable()) {

			this.location.setRow(this.location.getRow() - 1);
			// this.y-=this.speed;
			// updateGridLocation();
			this.collectGold(this.location);

		} else if (this.level.getTerrainAtLocation(this.location).isClimbable()
				&& this.level.getTerrainAtLocation(locationAbove).isRunnable()) {

			this.location.setRow(this.location.getRow() - 1);
			// this.y-=this.speed;
			// updateGridLocation();
			this.collectGold(this.location);

		}

	}

	/**
	 * Allows Hero to climb down ladders, but not through bricks/boards/open
	 * space
	 * 
	 */
	protected void climbDown() {
		// set current direction of movement to d for down
		this.currentDirection = 'd'; 
		if (this.location.getRow() == LodeRunnerPanel.ROWS - 1) {
			return;
		}
		// Climbing down
		Coordinates locationBelow = new Coordinates(this.location.getColumn(),
				this.location.getRow() + 1);
		// Check if terrain directly below Hero is a board or brick
		if (this.level.getTerrainAtLocation(locationBelow).isClimbable()) {

			this.location.setRow(this.location.getRow() + 1);
			// this.y+=this.speed;
			// updateGridLocation();
			this.collectGold(this.location);
		} else if (this.level.getTerrainAtLocation(this.location).isClimbable()
				&& this.level.getTerrainAtLocation(locationBelow).isRunnable()
				|| this.level.getTerrainAtLocation(this.location).isHangable()
				&& this.level.getTerrainAtLocation(locationBelow).isRunnable()) {

			this.location.setRow(this.location.getRow() + 1);
			// this.y+=this.speed;
			// updateGridLocation();
			this.collectGold(this.location);
		}

	}

	/**
	 * Implement the player automatically falling when terrain below isn't
	 * climbable/runnable Does not allow the player to fall through the bottom
	 * of the screen.
	 * 
	 */
	protected void autoFall() {
		// First make sure player can't fall through bottom of window
		if (this.location.getRow() == LodeRunnerPanel.ROWS - 1) {
			return;
		}
		Terrain current = this.level.getTerrainAtLocation(this.location);
		Terrain below = this.level.getTerrainAtLocation(new Coordinates(
				this.location.getColumn(), this.location.getRow() + 1));
		if (below.isRunnable()
				&& !below.isClimbable()
				&& !current.isHangable()
				&& !this.level.guardAtLocation(new Coordinates(this.location
						.getColumn(), this.location.getRow() + 1))) {

			this.isFalling = true;
			this.location.setRow(this.location.getRow() + 1);
			// this.y+=this.speed;
			// updateGridLocation();
			this.collectGold(this.location);
			current = this.level.getTerrainAtLocation(this.location);
			below = this.level.getTerrainAtLocation(new Coordinates(
					this.location.getColumn(), this.location.getRow() + 1));
		} else {
			this.isFalling = false;
		}
	}
}
