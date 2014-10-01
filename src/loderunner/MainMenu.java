package loderunner;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * Creates a main start menu that appears upon start of game. Can return to this
 * menu as needed.
 * 
 * @author brownba1. Created May 18, 2014.
 */
public class MainMenu {
	/**
	 * The frame for the game.
	 */
	static JFrame LodeRunnerFrame = new JFrame("LodeRunner");
	private JPanel menuPanel;
	private LodeRunnerManager manager;
	private LodeRunnerPanel panel;
	private OptionsPanel optionsPanel;
	private JPanel helpPanel;
	private LodeRunnerLevel level;
	/**
	 * Constructs the main menu with start, load, help, and quit buttons.
	 * 
	 * @param gameManager
	 */
	public MainMenu(LodeRunnerManager gameManager) {

		this.menuPanel = new JPanel();
		this.helpPanel = new JPanel();
		this.manager = gameManager;

		this.manager.setPaused();

		this.panel = new LodeRunnerPanel();
		this.optionsPanel = new OptionsPanel();
		FlowLayout flow = new FlowLayout();
		flow.setHgap(50);
		this.optionsPanel.setLayout(flow);
		
		File levelFile = new File("src/text/Level"
				+ this.manager.getCurrentLevel()
				+ "LodeRunner.txt");
		File levelEndFile = new File("src/text/Level"
				+ this.manager.getCurrentLevel()
				+ "LodeRunnerExit.txt");
		this.level = new LodeRunnerLevel(levelFile, levelEndFile);

		JButton startButton = new JButton("Start Game");
		JButton loadButton = new JButton("Load Game");
		JButton helpButton = new JButton("Help");
		JButton quitButton = new JButton("Quit");

		this.menuPanel.add(startButton, BorderLayout.NORTH);
		this.menuPanel.add(loadButton, BorderLayout.AFTER_LAST_LINE);
		this.menuPanel.add(helpButton, BorderLayout.AFTER_LAST_LINE);
		this.menuPanel.add(quitButton, BorderLayout.AFTER_LAST_LINE);

		final JButton backButton = new JButton("Back");
		JLabel helpLabel = new JLabel(
				"<html>Movement: Arrow Keys<br>Dig: Z/X<br>Pause/Resume: P<br><html>"
						+ "<html>Main Menu: F1<br>Load: F3<br>Save: F5<br>Help: F7<br>Exit: F9<html>");
		this.helpPanel.add(backButton, BorderLayout.SOUTH);
		this.helpPanel.add(helpLabel, BorderLayout.NORTH);

		LodeRunnerFrame.add(this.menuPanel);
		LodeRunnerFrame.setResizable(false);

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainMenu.this.manager.setCurrentLevel(1);
				MainMenu.this.manager.resetLevel();
				LodeRunnerFrame.add(MainMenu.this.panel, BorderLayout.NORTH);
				LodeRunnerFrame.add(MainMenu.this.optionsPanel,
						BorderLayout.SOUTH);
				LodeRunnerFrame.remove(MainMenu.this.menuPanel);
				MainMenu.this.manager.setPaused();
				PlayStartSound();
				LodeRunnerFrame.setFocusable(true);
				LodeRunnerFrame.setVisible(true);
				LodeRunnerFrame.pack();
				
				
				PlayBackGroundMusic();

			}
		});

		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LodeRunnerFrame.add(MainMenu.this.panel, BorderLayout.NORTH);
				LodeRunnerFrame.add(MainMenu.this.optionsPanel,
						BorderLayout.SOUTH);
				LodeRunnerFrame.remove(MainMenu.this.menuPanel);
				MainMenu.this.manager.setPaused();

				LodeRunnerFrame.setFocusable(true);
				LodeRunnerFrame.setVisible(true);
				LodeRunnerFrame.pack();
				PlayStartSound();
				PlayBackGroundMusic();
			}
		});

		helpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LodeRunnerFrame.add(MainMenu.this.helpPanel);
				LodeRunnerFrame.remove(MainMenu.this.menuPanel);

				LodeRunnerFrame.setFocusable(true);
				LodeRunnerFrame.setVisible(true);
				LodeRunnerFrame.pack();
			}
		});

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LodeRunnerFrame.add(MainMenu.this.menuPanel);
				LodeRunnerFrame.remove(MainMenu.this.helpPanel);

				LodeRunnerFrame.setFocusable(true);
				LodeRunnerFrame.setVisible(true);
				LodeRunnerFrame.pack();
			}
		});

		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					PrintWriter writer = new PrintWriter("HighScore.txt",
							"UTF-8");
					for (int i = 0; i < MainMenu.this.manager.getHighScore(); i++) {
						writer.print(0);
					}
					writer.print("0\n");
					for (int i = 0; i < MainMenu.this.manager.getCurrentLevel(); i++) {
						writer.print(1);
					}
					writer.close();
				} catch (FileNotFoundException exception) {
					exception.printStackTrace();
				} catch (UnsupportedEncodingException exception) {
					exception.printStackTrace();
				}
				System.exit(0);
			}
		});

		LodeRunnerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LodeRunnerFrame.setVisible(true);
		LodeRunnerFrame.pack();
	}


/**
 * This plays the audio file for every new level
 */
public static void PlayStartSound() {
	
		InputStream in;
		AudioStream audios;
		try {
			in = new FileInputStream(new File("src/audio/pacman_beginning.wav"));
			audios = new AudioStream(in);
			AudioPlayer.player.start(audios);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

private static void PlayBackGroundMusic() {
	File soundFile; 
	AudioInputStream audioIn; 
	Clip clip;
	try {
		soundFile = new File("src/audio/Daft Punk vs Kanye West (Harder, Better, Faster, Stronger).wav");
		audioIn = AudioSystem.getAudioInputStream(soundFile);
		clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	} catch (LineUnavailableException exception) {
		exception.printStackTrace();
	} catch (IOException exception) {
		exception.printStackTrace();
	} catch (UnsupportedAudioFileException exception) {
		exception.printStackTrace();
	} 
	
}	

	/**
	 * Returns the value of the field called 'lodeRunnerFrame'.
	 * 
	 * @return Returns the lodeRunnerFrame.
	 */
	public JFrame getLodeRunnerFrame() {
		return LodeRunnerFrame;
	}

	/**
	 * Returns the value of the field called 'helpPanel'.
	 * 
	 * @return Returns the helpPanel.
	 */
	public JPanel getHelpPanel() {
		return this.helpPanel;
	}

	/**
	 * Returns the value of the field called 'menuPanel'.
	 * 
	 * @return Returns the menuPanel.
	 */
	public JPanel getMenuPanel() {
		return this.menuPanel;
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
}
