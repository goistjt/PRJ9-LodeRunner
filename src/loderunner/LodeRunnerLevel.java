package loderunner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Timer;

/**
 * Level class that can construct a level from a given file and contains methods to handle objects that level has
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class LodeRunnerLevel implements DrawableLevel {

	private ArrayList<Terrain> terrainInitial = new ArrayList<Terrain>();
	private ArrayList<Terrain> terrainFinal = new ArrayList<Terrain>();
	private ArrayList<Guard> guards = new ArrayList<Guard>();
	private ArrayList<Gold> goldInitial = new ArrayList<Gold>();	
	private ArrayList<Gold> gold = new ArrayList<Gold>();	
	private Hero hero;
	private int fiveSeconds = 5000;
	private int guardNumber = 0;
	private int guardCount = 0;

	/**
	 * Initial constructor starts the game at level 1
	 * 
	 */
	public LodeRunnerLevel() {
		File levelFile = new File("src/text/Level1LodeRunner.txt");
		File levelEndFile = new File("src/text/Level1LodeRunnerExit.txt");
		scanFile(levelFile);
		scanEndFile(levelEndFile);
	}
	
	/**
	 * Constructs a new level from the given files
	 *
	 * @param levelFile Initial locations
	 * @param levelEndFile Final terrain locations w/ escape ladders
	 */
	public LodeRunnerLevel(File levelFile, File levelEndFile) {
		// If the file DNE, loads level 1
		if (!levelFile.exists()) {
			new LodeRunnerLevel();
		}
		scanFile(levelFile);
		scanEndFile(levelEndFile);
	}

	/**
	 * Reads a text file and parses characters to generate the Terrain, Gold, and Players
	 * 
	 * @param levelFile
	 */
	private void scanFile(File levelFile) {
		// Method that scans the given file line by line to get the block type and location
		Scanner in = null;
		try {
			in = new Scanner(levelFile).useDelimiter("\\s*");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int column = 0;
		int row = 0;
		while (in.hasNext()) {
			char c = in.next().charAt(0);
			if (column <= LodeRunnerPanel.COLUMNS) {
				if (column == LodeRunnerPanel.COLUMNS) {
					if (row < LodeRunnerPanel.ROWS) {
						row++;
					}
					column = 0;
				}
			}
			// Add getBlock to the level stuff
			addBlock(c, column, row);
			column++;
		}
	}
	
	private void scanEndFile(File levelEndFile) {
		// Method that scans the given file line by line to get the block type and location
		Scanner in = null;
		try {
			in = new Scanner(levelEndFile).useDelimiter("\\s*");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int column = 0;
		int row = 0;
		while (in.hasNext()) {
			char c = in.next().charAt(0);
			if (column <= LodeRunnerPanel.COLUMNS) {
				if (column == LodeRunnerPanel.COLUMNS) {
					if (row < LodeRunnerPanel.ROWS) {
						row++;
					}
					column = 0;
				}
			}
			// Add getBlock to the level stuff
			addEndBlock(c, column, row);
			column++;
		}
	}


	/**
	 * Switch Case takes the character read from the file and adds the respective object to the level
	 * 
	 * @param c
	 * @param column
	 * @param row
	 */
	private void addBlock(char block_char, int column, int row) {
		switch (block_char) {
		case 'B':

			this.terrainInitial.add(new Brick(column, row));
			break;

		case 'R':

			this.terrainInitial.add(new Rope(column, row));
			break;

		case 'L':

			this.terrainInitial.add(new Ladder(column, row));
			break;

		case 'S':

			this.terrainInitial.add(new Board(column, row));
			break;

		case 'E':
			// Make sure to have default block in same location
			this.terrainInitial.add(new OpenBlock(column, row));
			this.guards.add(new Guard(column, row, this.guardCount, this));
			this.guardCount++;
			break;

		case 'H':
			// Make sure to have default block in same location
			this.terrainInitial.add(new OpenBlock(column, row));
			this.hero = new Hero(column, row, this);
			break;

		case 'G':
			// Make sure to have default block in same location
			this.terrainInitial.add(new OpenBlock(column, row));
			Gold piece = new Gold(column, row);
			this.gold.add(piece);
			this.goldInitial.add(piece);
			break;
		
		case 'O':
			// Creates empty block
			this.terrainInitial.add(new OpenBlock(column, row));
			break; 

		default:

			break;
		}
	}
	/**
	 * Switch Case takes the character read from the file and adds the respective object to the level
	 * 
	 * @param c
	 * @param column
	 * @param row
	 */
	private void addEndBlock(char block_char, int column, int row) {
		switch (block_char) {
		case 'B':

			this.terrainFinal.add(new Brick(column, row));
			break;

		case 'R':

			this.terrainFinal.add(new Rope(column, row));
			break;

		case 'L':

			this.terrainFinal.add(new Ladder(column, row));
			break;

		case 'S':

			this.terrainFinal.add(new Board(column, row));
			break;
		
		case 'O':
			// Creates empty block
			this.terrainFinal.add(new OpenBlock(column, row));
			break; 

		default:

			break;
		}
	}
	/**
	 * Places an OpenBlock in black of the given block that gets removed
	 *
	 * @param block
	 */
	public void removeTerrain(Terrain block) {  
		final Terrain toRemove = block;
		final Terrain filler = new OpenBlock(block.location.getColumn(),block.location.getRow());
		this.terrainInitial.add(filler);
		this.terrainInitial.remove(toRemove);
		Timer respawnTimer = new Timer(this.fiveSeconds, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getTerrainInitial().add(toRemove);
				getTerrainInitial().remove(filler);
				
			}
		});		
		respawnTimer.start();
	}
	
	/**
	 * Removes the given guard from the list of guards.
	 *
	 * @param guard
	 */
	public void removeGuard(Guard guard) {
		for (int i=0; i<this.guards.size(); i++) {
			if (this.guards.get(i) == guard) {
				this.guardNumber = i;
				break;
			}
		}
		this.guards.remove(guard);
	}
	
	/**
	 * Adds a new guard to the list of guards at a 
	 * point at the top of the screen.
	 *
	 */
	public void addGuard() {
		this.guards.add(this.guardNumber, new Guard(new Random().nextInt(32), 0, this.guardNumber, this));
	}
	
	/**
	 * Sets terrain to final arrangement to allow for level completion
	 *
	 */
	public void setFinalTerrain() {
		this.terrainInitial = this.terrainFinal;
	}

	/**
	 * Returns the value of the field called 'terrainInitial'.
	 * 
	 * @return Returns the terrainInitial.
	 */
	public ArrayList<Terrain> getTerrainInitial() {
		return this.terrainInitial;
	}

	/**
	 * Returns the TerrainBlock at the given location. 
	 * Simply returns an open block at the location if loop somehow doesn't return a block (shouldn't happen)
	 * 
	 * @param location
	 * @return Terrain at given location
	 */
	public Terrain getTerrainAtLocation(Coordinates location) {
		for (Terrain block : this.terrainInitial) {
			if (block.location.getColumn() == location.getColumn()
					&& block.location.getRow() == location.getRow()) {
				return block;
			}
		}
		return new OpenBlock(location.getColumn(),location.getRow());
	}
	
	/**
	 * Returns the value of the field called 'gold'.
	 * 
	 * @return Returns the gold.
	 */
	public ArrayList<Gold> getGold() {
		return this.gold;
	}
	
	/**
	 * Returns the value of the field called 'goldInitial'.
	 * @return Returns the goldInitial.
	 */
	public ArrayList<Gold> getGoldInitial() {
		return this.goldInitial;
	}

	/**
	 * Returns the value of the field called 'hero'.
	 * 
	 * @return Returns the hero.
	 */
	public Hero getHero() {
		return this.hero;
	}

	/**
	 * Returns the value of the field called 'guards'.
	 * 
	 * @return Returns the guards.
	 */
	public ArrayList<Guard> getGuards() {
		return this.guards;
	}
	
	/**
	 * Checks if there is a guard at the given location.
	 *
	 * @param location
	 * @return true if a guard exists at location
	 */
	public boolean guardAtLocation(Coordinates location) {
		for(Guard g : this.guards){
			if (g.location.getColumn() == location.getColumn() && 
					g.location.getRow() == location.getRow()) {
				return true; 
			}
		}
		return false;
	}

	@Override
	public ArrayList<Drawable> getDrawableObjects() {
		ArrayList<Drawable> objects = new ArrayList<Drawable>();
		objects.addAll(this.terrainInitial);
		objects.addAll(this.gold);
		objects.addAll(this.guards);
		objects.add(this.hero);
		return objects;
	}
}
