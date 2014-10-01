package loderunner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import sun.audio.*;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * Creates the hero object with its movement, die, and collect gold methods.
 * 
 * @author Team10 - brownba1, thomasaz, goistjt Created May 7, 2014.
 */
public class Hero extends Player {
	private int moveCommand;
	private int moveCounter;

	/**
	 * Constructs a new hero object using a call to the player constructor.
	 * 
	 * @param x
	 * @param y
	 * @param level
	 */
	public Hero(int x, int y, LodeRunnerLevel level) {
		super(x, y, level);
		this.refreshSpeed = 1000/5;
	}

	// Player abstract methods that need overridden
	@Override
	protected void collectGold(Coordinates player) {
		// Collecting gold pieces
		ArrayList<Gold> gold = this.level.getGold();
		for (Gold piece : gold) {
			if (piece.getCoordinates().getColumn() == player.getColumn()
					&& piece.getCoordinates().getRow() == player.getRow()) {
				gold.remove(piece);
				PlayCollectGoldSound();
				return;
			}
		}
	}

	@Override
	public boolean die() {
		if (this.level.getTerrainAtLocation(this.location).isDiggable()) {
			PlayDeathSound();
			return true;
		} else {
			for (int i = 0; i < this.level.getGuards().size(); i++) {
				if (this.location.getColumn() == this.level.getGuards().get(i).location
						.getColumn()
						&& this.location.getRow() == this.level.getGuards()
								.get(i).location.getRow()) {
					PlayDeathSound();
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Calls the different hero movements in one function 
	 * based on the current command.
	 *
	 */
	public void move() {
		this.moveCounter++;
		switch (this.moveCommand) {
		
		case KeyEvent.VK_LEFT:
			//PlayMoveSound();
			this.runLeft();
			break;
			
		case KeyEvent.VK_RIGHT:
			//PlayMoveSound();
			this.runRight();
			break;
			
		case KeyEvent.VK_UP:
			//PlayMoveSound();
			this.climbUp();
			break;
			
		case KeyEvent.VK_DOWN:
			//PlayMoveSound();
			this.climbDown();
			break;
			
		default:
			break;
		}
		setMoveCommand(0);
	}

	/**
	 * Hero can dig through the terrain block below and to the left of him, so
	 * long as it is diggable.
	 * 
	 */
	public void digLeft() {
		Coordinates digCoordLeft = new Coordinates(
				this.location.getColumn() - 1, this.location.getRow() + 1);
		Terrain bottomLeft = this.level.getTerrainAtLocation(digCoordLeft);
		// Check lower left Terrain to see if brick; if so, dig
		if (bottomLeft.isDiggable()) {
			this.level.removeTerrain(bottomLeft);
			PlayDigBrickSound();
		}
	}

	/**
	 * Hero can dig through the terrain block below and to the right of him, so
	 * long as it is diggable.
	 * 
	 */
	public void digRight() {
		Coordinates digCoordRight = new Coordinates(
				this.location.getColumn() + 1, this.location.getRow() + 1);
		Terrain bottomRight = this.level.getTerrainAtLocation(digCoordRight);
		// Check lower right Terrain to see if brick; is so, dig
		if (bottomRight.isDiggable()) {
			this.level.removeTerrain(bottomRight);
			PlayDigBrickSound();
		}
	}

	/**
	 * Checks to see if the hero has reached the final position of the current
	 * level Returns true if so, false if not.
	 * 
	 * @return true if the hero has reached the spaced to advance to next level
	 */
	public boolean checkForNextLevel() {
		// this methods checks for the hero's position;
		// if position is the very top of the screen, change to next level
		if (this.location.getRow() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sets the move command to the given key press.
	 *
	 * @param keyPress
	 */
	public void setMoveCommand(int keyPress) {
		this.moveCommand = keyPress;
	}
	
	// Create method to play audio
	
	private static void PlayDeathSound() {
		InputStream in;
		AudioStream audios; 
		try {
			in = new FileInputStream(new File("src/audio/pacman_death.wav"));
			audios = new AudioStream(in);
			AudioPlayer.player.start(audios);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace(); 
		} catch (IOException exception) {
			exception.printStackTrace();
		} 
	}
	
	private static void PlayCollectGoldSound() {
		InputStream in;
		AudioStream audios; 
		try {
			in = new FileInputStream(new File("src/audio/Mario Coin.wav"));
			audios = new AudioStream(in);
			AudioPlayer.player.start(audios);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace(); 
		} catch (IOException exception) {
			exception.printStackTrace();
		} 
	}
	
	private static void PlayDigBrickSound() {
		InputStream in;
		AudioStream audios; 
		try {
			in = new FileInputStream(new File("src/audio/BrickCrumble.wav"));
			audios = new AudioStream(in);
			AudioPlayer.player.start(audios);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace(); 
		} catch (IOException exception) {
			exception.printStackTrace();
		} 
	}

	// Drawable Interface
	@Override
	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int col_w = LodeRunnerPanel.COLUMN_WIDTH;
		int row_h = LodeRunnerPanel.ROW_HEIGHT;

		if (this.moveCounter % 2 == 0) {
			Image heroOpen = Toolkit.getDefaultToolkit().getImage(
					"src/images/PacManOpen.png");
			g2.drawImage(heroOpen, this.location.getColumn() * col_w,
					this.location.getRow() * row_h, col_w, row_h, null);
		} else {
			Image heroClosed = Toolkit.getDefaultToolkit().getImage(
					"src/images/PacManClosed.png");
			g2.drawImage(heroClosed, this.location.getColumn() * col_w,
					this.location.getRow() * row_h, col_w, row_h, null);
		}
		
		g2.dispose();
	}

}
