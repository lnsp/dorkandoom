package de.mooxmirror.dorkandoom.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.mooxmirror.dorkandoom.states.StateManager;
/**
 * Main class in the game that manages the game loop, updates and draws the game.
 * @author Mooxmirror
 * @version 0.1.0-0002
 */
@SuppressWarnings("serial")
public class Game extends Canvas {
	public static List<Object> dataStorage = new ArrayList<Object>();
	/**
	 * Target fps for the game.
	 */
	private final int FPS = 60;
	/**
	 * Flag for testing if the game should exit.
	 */
	private boolean gameRunning = true;
	/**
	 * Stores the local buffer strategy for double-buffering.
	 */
	private BufferStrategy bufferStrategy;
	/**
	 * Stores the value in milliseconds, when the game was displayed.
	 */
	private long lastGameUpdate;
	/**
	 * Saves the value, how many frames per second are displayed.
	 */
	private int workingFPS;
	/**
	 * Stores window width.
	 */
	private int width;
	/**
	 * Stores window height.
	 */
	private int height;
	/**
	 * Manages game states.
	 */
	public static StateManager stateManager;
	/**
	 * Initialize a new game instance with a given width and height.
	 * @param width The window width.
	 * @param height The window height.
	 */
	private JFrame parentWindow;
	public Game(int width, int height) {
		// Calling superconstructor
		super();
		// Rematch the window
		parentWindow = new JFrame("Dorkandoom");
		parentWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(0, 0, width, height);
		setFocusable(true);
		setEnabled(true);
		addKeyListener(new GameController());
		parentWindow.requestFocus();
		parentWindow.add(this);
		parentWindow.pack();
		parentWindow.setVisible(true);
		parentWindow.setLocationRelativeTo(null);
		this.width = width;
		this.height = height;
		
	}
	/**
	 * Starts the main loop.
	 */
	public void run() {
		// FPS Variables
		long lastFPSCount = System.currentTimeMillis();
		int fpsCounter = 0;
		init();
		while (gameRunning) {
			long thisGameUpdate = System.currentTimeMillis();
			if (thisGameUpdate - lastGameUpdate > 1000 / FPS) {
				update();
				draw();
				lastGameUpdate = thisGameUpdate;
				fpsCounter += 1;
			}
			if (System.currentTimeMillis() - lastFPSCount > 1000) {
				workingFPS = fpsCounter;
				fpsCounter = 0;
				lastFPSCount = System.currentTimeMillis();
				parentWindow.setTitle("Dorkandoom @ " + workingFPS + " FPS");
			}
		}
	}
	/**
	 * Initialize the game and resources.
	 */
	public void init() {
		// Create double buffering method
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();

		requestFocus();
		stateManager = new StateManager();
	}
	/**
	 * Reacts on user input and update player/enemy movement etc.
	 */
	public void update() {
		try {
			stateManager.getCurrentState().update();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 * Draws the game to the screen.
	 */
	public void draw() {
		Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		stateManager.getCurrentState().draw(g2d);
		
		g2d.dispose();
		bufferStrategy.show();
	}
	private class GameController implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				stateManager.getCurrentState().keyDown("left");
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				stateManager.getCurrentState().keyDown("right");
			}
			else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				stateManager.getCurrentState().keyDown("space");
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP){
				stateManager.getCurrentState().keyDown("up");
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				stateManager.getCurrentState().keyDown("down");
			}
			else if(e.getKeyCode() == KeyEvent.VK_Q) {
				stateManager.getCurrentState().keyDown("q");
			}
			else if(e.getKeyCode() == KeyEvent.VK_W) {
				stateManager.getCurrentState().keyDown("w");
			}
			else if(e.getKeyCode() == KeyEvent.VK_E) {
				stateManager.getCurrentState().keyDown("e");
			}
			else if(e.getKeyCode() == KeyEvent.VK_1) {
				stateManager.getCurrentState().keyDown("1");
			}
			else if(e.getKeyCode() == KeyEvent.VK_2) {
				stateManager.getCurrentState().keyDown("2");
			}
			else if(e.getKeyCode() == KeyEvent.VK_3) {
				stateManager.getCurrentState().keyDown("3");
			}
			else if(e.getKeyCode() == KeyEvent.VK_4) {
				stateManager.getCurrentState().keyDown("4");
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				stateManager.getCurrentState().keyUp("left");
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				stateManager.getCurrentState().keyUp("right");
			}
			else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				stateManager.getCurrentState().keyUp("space");
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP){
				stateManager.getCurrentState().keyUp("up");
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				stateManager.getCurrentState().keyUp("down");
			}
			else if(e.getKeyCode() == KeyEvent.VK_Q){
				stateManager.getCurrentState().keyUp("q");
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}
}
