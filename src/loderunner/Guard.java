package loderunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 * Creates a guard object with its movement, die, and collect/drop gold methods.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Guard extends Player {
	/**
	 * variable for if guard has gold
	 */
	protected boolean hasGold = false;
	private Gold heldGold;
	private int guardNumber;

	/**
	 * Guard constructor constructs a new guard object at the given x & y
	 * coordinates.
	 * 
	 * @param x
	 *            Column location
	 * @param y
	 *            Row location
	 * @param guardNumber 
	 * @param level
	 *            in which the Guard exists
	 */
	public Guard(int x, int y, int guardNumber, LodeRunnerLevel level) {
		super(x, y, level);
		this.refreshSpeed = 1000 / 3;
		this.guardNumber = guardNumber;
	}

	// Player abstract methods that need overridden
	@Override
	protected void collectGold(Coordinates player) {
		// Collecting gold pieces
		if (!this.hasGold) {
			ArrayList<Gold> gold = this.level.getGold();
			for (Gold piece : gold) {
				if (piece.getCoordinates().getColumn() == player.getColumn()
						&& piece.getCoordinates().getRow() == player.getRow()) {
					piece.getCoordinates().setColumn(-1);
					piece.getCoordinates().setRow(0);
					this.hasGold = true;
					this.heldGold = piece;
					return;
				}
			}
		}
	}

	@Override
	public boolean die() {
		if (this.level.getTerrainAtLocation(this.location).isDiggable()) {
			return true;
		}
		return false;
	}

	/**
	 *Returns true if Guard is stuck between two bricks after 
	 *falling in a hole the player digs
	 *
	 * @return true or false 
	 */
	public boolean isTrapped() {
		if (this.level.getTerrainAtLocation(new Coordinates(this.location.getColumn() - 1, this.location.getRow())).isDiggable()
				&& this.level.getTerrainAtLocation(new Coordinates(this.location.getColumn() + 1,
								this.location.getRow())).isDiggable()
				&& !this.level.getTerrainAtLocation(this.location).isClimbable()
				) {
			return true; 
		} else if (this.level.getTerrainAtLocation(new Coordinates(this.location.getColumn() - 1, this.location.getRow())).isDiggable()
				&& this.level.getTerrainAtLocation(new Coordinates(this.location.getColumn() + 1,
								this.location.getRow())).isClimbable()){
			return true; 
		} else if (this.level.getTerrainAtLocation(new Coordinates(this.location.getColumn() + 1, this.location.getRow())).isDiggable()
				&& this.level.getTerrainAtLocation(new Coordinates(this.location.getColumn() - 1,
						this.location.getRow())).isClimbable()) {
			return true; 
		}
		return false;		
	}

	// Individual methods
	/**
	 * Implements the AI movement of the guards
	 * 
	 */
	public void move() {
		// TODO Eventually calculate fastest route to hero IF POSSIBLE
		if (this.isTrapped()) {
			this.dropGold();
		}
		Coordinates heroLocation = this.level.getHero().location;

		int heroCol = heroLocation.getColumn();
		int heroRow = heroLocation.getRow();
		int selfCol = this.location.getColumn();
		int selfRow = this.location.getRow();

		// check guard locations and don't allow to run into each other
		
		// this handles case when guards fall on top of each other
		if (!this.isFalling){
			if (this.level.guardAtLocation(new Coordinates(selfCol, selfRow - 1)) && this.currentDirection == 'l'){
				runRight();
				runRight();
				return;
			} 
			else if (this.level.guardAtLocation(new Coordinates(selfCol, selfRow - 1)) && this.currentDirection == 'r') {
				runLeft();
				runLeft();
				return; 
			}
		}
		
		// handles all climbing and horizontal movement 
		if (this.currentDirection == 'l') {
			// guard is moving left, so check to see if needs changed to right
			if (this.level
					.guardAtLocation(new Coordinates(selfCol - 1, selfRow))) {
				this.runRight();
				return;
			}
		} else if (this.currentDirection == 'r') {
			// guard is moving right, so check to see if needs changed to left
			if (this.level
					.guardAtLocation(new Coordinates(selfCol + 1, selfRow))) {
				this.runLeft();
				return;
			}
		} else if (this.currentDirection == 'u') {
			// guard is climbing up, so check to see if need toclimb down
			if (this.level
					.guardAtLocation(new Coordinates(selfCol, selfRow - 1))) {
				this.climbDown();
				return;
			}
		} else if (this.currentDirection == 'd') {
			// guard is climbing down, so check to see if need to climb up
			if (this.level
					.guardAtLocation(new Coordinates(selfCol, selfRow + 1))) {
				this.climbUp();
				return;
			}
		}
		
			
			// case of the hero is below the guard
			if (heroRow > selfRow) {
				// this statement will keep AI from derping in a corner
				if (heroCol < selfCol && this.level.getTerrainAtLocation(new Coordinates(selfCol, selfRow + 1)).isDiggable() 
						&& this.level.getTerrainAtLocation(new Coordinates(selfCol - 1, selfRow)).isDiggable()) {
					runRight(); 
				} else if (heroCol > selfCol && this.level.getTerrainAtLocation(new Coordinates(selfCol, selfRow + 1)).isDiggable() 
						&& this.level.getTerrainAtLocation(new Coordinates(selfCol + 1, selfRow)).isDiggable()){
					runLeft(); 
				}
				// the guard looks for the shortest path to a ladder, until it finds one to climb down
				if (this.level.getTerrainAtLocation(
						new Coordinates(selfCol, selfRow + 1)).isRunnable()) {
					this.climbDown();
				}
				else if (calculateRightPathBelow() >= calculateLeftPathBelow()){
					runLeft(); 
				} else {
					runRight(); 
				}
				return; 
			} 
			//case of the hero is above the guard
			else if (heroRow < selfRow) {
				// this statement will keep AI from derping in a corner
				if (heroCol < selfCol && this.level.getTerrainAtLocation(new Coordinates(selfCol, selfRow - 1)).isDiggable() 
						&& this.level.getTerrainAtLocation(new Coordinates(selfCol + 1, selfRow)).isDiggable()) {
					runRight(); 
				} else if (heroCol > selfCol && this.level.getTerrainAtLocation(new Coordinates(selfCol, selfRow - 1)).isDiggable() 
						&& this.level.getTerrainAtLocation(new Coordinates(selfCol - 1, selfRow)).isDiggable()){
					runLeft(); 
				}
				// the guard looks for the shortest path to a ladder, until it finds one to climb up 
				if (this.level.getTerrainAtLocation(this.location).isClimbable()) {
					this.climbUp();
				} else if (calculateRightPathAbove() >= calculateLeftPathAbove()) {
					runLeft(); 
				} else {
					runRight(); 
				}
				return; 
			}
			// case of if hero and guard on same row
			else if (heroRow == selfRow) {
				if (heroCol < selfCol && !this.level.getTerrainAtLocation(new Coordinates(selfCol - 1, selfRow)).isDiggable()) {
					runLeft(); 
				} else if (heroCol < selfCol && this.level.getTerrainAtLocation(new Coordinates(selfCol - 1, selfRow)).isDiggable()) {
					// the guards should climb up or down, or look for a better path
				}
				else if (heroCol > selfCol && !this.level.getTerrainAtLocation(new Coordinates(selfCol + 1, selfRow)).isDiggable()){
					runRight(); 
				} else if (heroCol > selfCol && this.level.getTerrainAtLocation(new Coordinates(selfCol + 1, selfRow)).isDiggable()) {
					// the guards should climb up or down, or look for a better path 
				}
				return; 
			}
	}

	// finds optimal path to hero if the hero is below and to the left of the guard 
	private int calculateRightPathBelow() {
		int sizeRight = LodeRunnerPanel.COLUMNS - this.location.getColumn(); 
		int counter = 0; 
		for (int i=0; i<sizeRight; i++){
			Coordinates checkBelow = new Coordinates(this.location.getColumn() + i, this.location.getRow() + 1);
			if (this.level.getTerrainAtLocation(checkBelow).isRunnable()) {
				return counter; 
				// guard found path to ladder
			}
			counter ++; 
		}
		if (counter == sizeRight) {
			return 50; 
		}
		return counter; 
	}
	
	// finds optimal path to hero if the hero is above and to the right of the guard
	private int calculateRightPathAbove() {
		int sizeRight = LodeRunnerPanel.COLUMNS - this.location.getColumn(); 
		int counter = 0; 
		for (int i=0; i<sizeRight; i++){
			Coordinates checkRow = new Coordinates(this.location.getColumn() + i, this.location.getRow());
			if (this.level.getTerrainAtLocation(checkRow).isClimbable()) {
				return counter; 
				// guard found path to ladder
			}
			counter ++; 
		}
		if (counter == sizeRight) {
			return 50; 
		}
		return counter; 
	}
	
	// finds optimal path to hero if the hero is below and to the left of the guard
	private int calculateLeftPathBelow() {
		int sizeLeft = this.location.getColumn(); 
		int counter = 0; 
		for(int i=0; i<sizeLeft; i++) {
			Coordinates checkBelow = new Coordinates(this.location.getColumn() - i, this.location.getRow() + 1);
			if(this.level.getTerrainAtLocation(checkBelow).isRunnable()){
				return counter; 
				// guard found path to ladder
			}
			counter ++; 
		}
		if (counter == sizeLeft){
			return 50; 
		}
		
		return counter; 
	}
	
	// finds optimal path to hero if the hero is above and to the left of the guard
	private int calculateLeftPathAbove() {
		int sizeLeft = this.location.getColumn(); 
		int counter = 0; 
		for(int i=0; i<sizeLeft; i++) {
			Coordinates checkRow = new Coordinates(this.location.getColumn() - i, this.location.getRow());
			if(this.level.getTerrainAtLocation(checkRow).isClimbable()){
				return counter; 
				// guard found path to ladder
			}
			counter ++; 
		}
		if (counter == sizeLeft){
			return 50; 
		}
		
		return counter; 
	}

	private void dropGold() {
		// Drop gold when trapped
		if (this.hasGold) {
			this.heldGold.getCoordinates().setColumn(this.location.getColumn());
			this.heldGold.getCoordinates().setRow(this.location.getRow() - 1);
			this.hasGold = false;
			this.heldGold = null;
		}

	}

	// Drawable Interface
	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = LodeRunnerPanel.COLUMN_WIDTH;
		int row_h = LodeRunnerPanel.ROW_HEIGHT;
		
		Image guard = Toolkit.getDefaultToolkit().getImage(
				"src/images/Ghost" + (this.guardNumber+1) + ".png");
		g2.drawImage(guard, this.location.getColumn() * col_w,
				this.location.getRow() * row_h, col_w, row_h, null);
		
		g2.dispose();
	}

}
