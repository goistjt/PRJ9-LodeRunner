package loderunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;

/**
 * This manager class creates every necessary component for our game to function;
 * It holds the frame, panel 
 * Reads appropriate files 
 * it also contains all four runnable threads to implement multi-threading
 * contains getter and setter methods to be used in other classes within this package
 * contains methods to change levels appropriately 
 * contains methods to pause game within thread
 * 
 * 
 * @author goistjt. Created May 12, 2014.
 */
public class LodeRunnerManager {
	private int goldInitial;
	private int goldCurrent;
	private int levelScore;
	private int previousScore = 0;
	private int highScore;
	private int currentHighScore = 0;
	private int levelScoreBeforeReset = 0;

	private MainMenu startMenu;
	private LodeRunnerPanel panel;
	private OptionsPanel optionsPanel;
	private JLabel optionsLabel;
	private JLabel levelLabel;
	private JLabel scoreLabel;
	private LodeRunnerLevel level;

	private final int FRAMES_PER_SECOND = 30;
	private final long REPAINT_INTERVAL_MS = 1000 / this.FRAMES_PER_SECOND;

	private int currentLevel;
	private boolean paused = false;

	/**
	 * Constructor for the manager
	 * 
	 */
	public LodeRunnerManager() {
		// Reads the file containing the high score for the game
		// and sets the high score field equal to it.
		File highScoreFile = new File("HighScore.txt");
		scanFile(highScoreFile);

		this.startMenu = new MainMenu(this);

		this.panel = this.startMenu.getPanel();
		this.optionsPanel = this.startMenu.getOptionsPanel();
		this.level = this.startMenu.getLevel();

		KeyPressHandler heroHandler = new KeyPressHandler(this);
		this.panel.addKeyListener(heroHandler);

		this.levelLabel = new JLabel("Level: " + this.currentLevel);
		this.optionsLabel = new JLabel("P - Pause/Resume,"
				+ "    F1 - Main Menu, " + "    F3 - Load, " + "    F5 - Save"
				+ "    F7 - Help, " + "    F9 - Quit");
		
		this.scoreLabel = new JLabel("High Score: " + this.highScore + "    Current Score: " 
				+ this.currentHighScore + "      Chests: " + 0 + " : " 
				+ this.level.getGoldInitial().size());
		
		this.optionsPanel.add(this.levelLabel);
		this.optionsPanel.add(this.optionsLabel);
		this.optionsPanel.add(this.scoreLabel);

		Runnable repainter = new Runnable() {

			@Override
			public synchronized void run() {
				while (true) {
					getPanel().setFocusable(true);
					if (!isPaused()) {
						try {
							getPanel().paint(getLevel());
							Thread.sleep(LodeRunnerManager.this.REPAINT_INTERVAL_MS);
						} catch (InterruptedException exception) {
							exception.printStackTrace();
						}
					}
				}
			}
		};

		Runnable heroMove = new Runnable() {

			@Override
			public void run() {
				while (true) {
					getPanel().setFocusable(true);
					if (!isPaused()) {
						try {
							getLevel().getHero().autoFall();
							getLevel().getHero().move();
							Thread.sleep(getLevel().getHero().getRefreshSpeed());
						} catch (InterruptedException exception) {
							exception.printStackTrace();
						}
					}
				}
			}

		};

		Runnable enemyMove = new Runnable() {

			@Override
			public void run() {
				ArrayList<Guard> guardsToRemove = new ArrayList<Guard>();
				while (true) {
					getPanel().setFocusable(true);
					if (!isPaused()) {
						try {
							for (Guard enemy : getLevel().getGuards()) {
								if (!enemy.isTrapped()) {
									enemy.autoFall();
									enemy.move();
								}
								if (enemy.die()) {
									guardsToRemove.add(enemy);
								}
							}
							for (Guard enemy : guardsToRemove) {
								getLevel().removeGuard(enemy);
								getLevel().addGuard();
							}
							guardsToRemove.removeAll(guardsToRemove);
							if (getLevel().getHero().die()) {
								resetLevel();
							}
							Thread.sleep(getLevel().getGuards().get(0)
									.getRefreshSpeed());
						} catch (InterruptedException exception) {
							exception.printStackTrace();
						}
					}
				}
			}

		};

		Runnable levelFinishedCheck = new Runnable() {

			@Override
			public synchronized void run() {
				while (true) {
					getPanel().setFocusable(true);
					setGoldCurrent(getLevel().getGold().size());
					setGoldInitial(getLevel().getGoldInitial().size());
					setLevelScore();
					if (LodeRunnerManager.this.goldInitial == LodeRunnerManager.this.levelScore) {
						getLevel().setFinalTerrain();
					}
					if (getLevel().getHero().checkForNextLevel()) {
						nextLevel();
					}
				}

			}
		};

		Thread t1 = new Thread(repainter);
		Thread t2 = new Thread(heroMove);
		Thread t3 = new Thread(levelFinishedCheck);
		Thread t4 = new Thread(enemyMove);
		t1.start();
		t2.start();
		t3.start();
		t4.start();

	}

	/**
	 * Returns true if the game is paused, false if not
	 * 
	 * @return
	 */
	protected boolean isPaused() {
		return this.paused;
	}

	/**
	 * Sets the field called 'paused' to the given value.
	 * 
	 * @param paused
	 *            The paused to set.
	 */
	public void setPaused() {
		this.paused = !this.paused;
	}

	/**
	 * Sets the field called 'goldCurrent' to the given value.
	 * 
	 * @param goldCurrent
	 *            The goldCurrent to set.
	 */
	public void setGoldCurrent(int goldCurrent) {
		this.goldCurrent = goldCurrent;
	}

	/**
	 * Sets the field called 'goldInitial' to the given value.
	 * 
	 * @param goldInitial
	 */
	public void setGoldInitial(int goldInitial) {
		this.goldInitial = goldInitial;
	}

	/**
	 * Sets the field called 'levelScore' to the given value.
	 * 
	 * @param levelScore
	 *            The levelScore to set.
	 */
	public void setLevelScore() {
		this.levelScore = this.goldInitial - this.goldCurrent;
		if (this.previousScore < this.levelScore) {
			this.previousScore++;
			if (this.levelScore > this.levelScoreBeforeReset) {
				this.currentHighScore++;
			}
			if (this.currentHighScore > this.highScore) {
				this.highScore++;
			}
		}
		this.scoreLabel.setText("High Score: " + this.highScore + "     Current Score: "
				+ this.currentHighScore + "     Chests: " 
				+ this.levelScore + " : " + this.level.getGoldInitial().size());
	}

	/**
	 * Returns the value of the field called 'menuPanel'.
	 * 
	 * @return Returns the menuPanel.
	 */
	public MainMenu getStartMenu() {
		return this.startMenu;
	}

	/**
	 * Returns the value of the field called 'panel'.
	 * 
	 * @return Returns the panel.
	 */
	public LodeRunnerPanel getPanel() {
		return this.panel;
	}

	/**
	 * Returns the value of the field called 'optionsPanel'.
	 * 
	 * @return Returns the optionsPanel.
	 */
	public OptionsPanel getOptionsPanel() {
		return this.optionsPanel;
	}

	/**
	 * Returns the value of the field called 'level'.
	 * 
	 * @return Returns the level.
	 */
	public LodeRunnerLevel getLevel() {
		return this.level;
	}

	/**
	 * Returns the value of the field called 'highScore'.
	 * 
	 * @return Returns the highScore.
	 */
	public int getHighScore() {
		return this.highScore;
	}
	
	/**
	 * Returns the value of the field called 'currentLevel'.
	 * @return Returns the currentLevel.
	 */
	public int getCurrentLevel() {
		return this.currentLevel;
	}
	
	/**
	 * Able to manually set the current level
	 * Always follow w/ resetLevel()
	 *
	 * @param level
	 */
	public void setCurrentLevel(int level) {
		this.currentLevel = level;
	}

	/**
	 * Changes the level to the next level
	 * 
	 */
	public void nextLevel() {
		this.previousScore = 0;
		this.levelScoreBeforeReset = 0;
		this.currentLevel++;
		File nextLevel = new File("src/text/Level" + this.currentLevel + "LodeRunner.txt");
		File nextLevelEnd = new File("src/text/Level" + this.currentLevel
				+ "LodeRunnerExit.txt");
		this.level = new LodeRunnerLevel(nextLevel, nextLevelEnd);
		if (this.currentLevel == 5) {
			for (Guard enemy : this.getLevel().getGuards()) {
				enemy.hasGold = true;
			}
		}
		this.scoreLabel.setText("High Score: " + this.highScore
				+ "     Current Score: " + this.currentHighScore
				+ "     Chests: " + 0 + " : " + this.level.getGoldInitial().size());
		this.levelLabel.setText("Level: " + this.currentLevel);
		MainMenu.PlayStartSound();
	}

	/**
	 * Sets the user back one level
	 * 
	 */
	public void previousLevel() {
		this.currentLevel--;
		File prevLevel = new File("src/text/Level" + this.currentLevel + "LodeRunner.txt");
		File prevLevelEnd = new File("src/text/Level" + this.currentLevel
				+ "LodeRunnerExit.txt");
		this.level = new LodeRunnerLevel(prevLevel, prevLevelEnd);
	}

	/**
	 * Resets the current level if the player dies.
	 * 
	 */
	public void resetLevel() {
		this.levelScoreBeforeReset = this.levelScore;
		File currentLevelFile = new File("src/text/Level" + this.currentLevel
				+ "LodeRunner.txt");
		File currentLevelEnd = new File("src/text/Level" + this.currentLevel
				+ "LodeRunnerExit.txt");
		this.level = new LodeRunnerLevel(currentLevelFile, currentLevelEnd);
		this.scoreLabel.setText("High Score: " + this.highScore
				+ "      Chests: " + 0 + " : " + this.level.getGoldInitial().size());
	}
	
	/**
	 * Sets the field called 'level' to the given value.
	 * @param level The level to set.
	 */
	public void setLevel(LodeRunnerLevel level) {
		this.level = level;
	}

	private void scanFile(File highScoreFile) {
		// Method that scans the given file to get the high score.
		Scanner in = null;
		try {
			in = new Scanner(highScoreFile).useDelimiter("\\s*");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.highScore = in.nextLine().length() - 1;
		setCurrentLevel(in.nextLine().length());
	}

}
