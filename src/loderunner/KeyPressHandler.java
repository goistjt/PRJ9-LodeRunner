package loderunner;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Creates the handler for the key controls of the game.
 * 
 * @author brownba1. Created May 9, 2014.
 */
public class KeyPressHandler implements KeyListener {
	private LodeRunnerManager manager;
	private MainMenu menu;
	
	/**
	 * Constructs the keyPressHandler to use the given manager.
	 *
	 * @param manager
	 */
	public KeyPressHandler(LodeRunnerManager manager) {
		this.manager = manager;
		this.menu = manager.getStartMenu();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {

		case KeyEvent.VK_LEFT:
			if (!this.manager.isPaused()) {
				this.manager.getLevel().getHero().setMoveCommand(KeyEvent.VK_LEFT);
			}
			break;

		case KeyEvent.VK_RIGHT:
			if (!this.manager.isPaused()) {
				this.manager.getLevel().getHero().setMoveCommand(KeyEvent.VK_RIGHT);
			}
			break;

		case KeyEvent.VK_UP:
			if (!this.manager.isPaused()) {
				this.manager.getLevel().getHero().setMoveCommand(KeyEvent.VK_UP);
			}
			break;

		case KeyEvent.VK_DOWN:
			if (!this.manager.isPaused()) {
				this.manager.getLevel().getHero().setMoveCommand(KeyEvent.VK_DOWN);
			}
			break;

		case KeyEvent.VK_Z:
			if (!this.manager.isPaused()) {
				this.manager.getLevel().getHero().digLeft();
			}
			break;

		case KeyEvent.VK_X:
			if (!this.manager.isPaused()) {
				this.manager.getLevel().getHero().digRight();
			}
			break;

		case KeyEvent.VK_P:
			this.manager.setPaused();
			break;

		case KeyEvent.VK_U:
			this.manager.nextLevel();
			break;
			
		case KeyEvent.VK_D:
			this.manager.previousLevel();
			break;
			
		case KeyEvent.VK_F1:
			//return to main menu
			if (!this.manager.isPaused()) {
				this.manager.setPaused();
			}
			this.menu.getLodeRunnerFrame().add(this.menu.getMenuPanel());
			this.menu.getLodeRunnerFrame().remove(this.manager.getOptionsPanel());
			this.menu.getLodeRunnerFrame().remove(this.manager.getPanel());
			
			this.menu.getLodeRunnerFrame().setFocusable(true);
			this.menu.getLodeRunnerFrame().setVisible(true);
			this.menu.getLodeRunnerFrame().pack();
			
			break;
			
		case KeyEvent.VK_F3:
			// load game
			File highScoreFile = new File("HighScore.txt");
			scanFile(highScoreFile);
			
			File levelFile = new File("src/text/Level"
					+ this.manager.getCurrentLevel()
					+ "LodeRunner.txt");
			File levelEndFile = new File("src/text/Level"
					+ this.manager.getCurrentLevel()
					+ "LodeRunnerExit.txt");
			
			this.manager.setLevel(new LodeRunnerLevel(levelFile, levelEndFile));
			break;
			
		case KeyEvent.VK_F5:
			//Save game at level & high score
			//"checkpoint save"
			try {
				PrintWriter writer = new PrintWriter("HighScore.txt", "UTF-8");
				for (int i=0; i<this.manager.getHighScore(); i++) {
					writer.print(0);
				}
				writer.print("0\n");
				for (int i=0; i< this.manager.getCurrentLevel(); i++) {
					writer.print(1);
				}
				writer.close();
			} catch (FileNotFoundException exception) {
				exception.printStackTrace();
			} catch (UnsupportedEncodingException exception) {
				exception.printStackTrace();
			}
			break;
			
		case KeyEvent.VK_F7:
			if (!this.manager.isPaused()) {
				this.manager.setPaused();
			}
			this.menu.getLodeRunnerFrame().add(this.menu.getHelpPanel());
			this.menu.getLodeRunnerFrame().remove(this.manager.getOptionsPanel());
			this.menu.getLodeRunnerFrame().remove(this.manager.getPanel());

			this.menu.getLodeRunnerFrame().setFocusable(true);
			this.menu.getLodeRunnerFrame().setVisible(true);
			this.menu.getLodeRunnerFrame().pack();
			break;
			
		case KeyEvent.VK_F9:
			try {
				PrintWriter writer = new PrintWriter("HighScore.txt", "UTF-8");
				for (int i=0; i<this.manager.getHighScore(); i++) {
					writer.print(0);
				}
				writer.print("0\n");
				for (int i=0; i< this.manager.getCurrentLevel(); i++) {
					writer.print(1);
				}
				writer.close();
			} catch (FileNotFoundException exception) {
				exception.printStackTrace();
			} catch (UnsupportedEncodingException exception) {
				exception.printStackTrace();
			}
			System.exit(0);
			break;
		
		default:
			break;

		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		//Nothing here
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//Nothing here
	}
	
	private void scanFile(File highScoreFile) {
		// Method that scans the given file to get the high score.
		Scanner in = null;
		try {
			in = new Scanner(highScoreFile).useDelimiter("\\s*");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		in.nextLine();
		this.manager.setCurrentLevel(in.nextLine().length());
	}

}
