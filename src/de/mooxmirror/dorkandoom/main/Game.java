package de.mooxmirror.dorkandoom.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.mooxmirror.dorkandoom.states.StateManager;

/**
 * Main class in the game that manages the game loop, updates and draws the
 * game.
 * 
 * @author Mooxmirror
 * @version 0.1.0-0002
 */
@SuppressWarnings("serial")
public class Game extends Canvas {
	public static List<Object> dataStorage = new ArrayList<Object>();
	/**
	 * Target fps for the game.
	 */
	private final int TARGET_FPS = 60;
	/**
	 * Flag for testing if the game should exit.
	 */
	private boolean mRunning = true;
	/**
	 * Stores the local buffer strategy for double-buffering.
	 */
	private BufferStrategy mStrategy;
	/**
	 * Stores the value in milliseconds, when the game was displayed.
	 */
	private long mLastUpdate;
	/**
	 * Saves the value, how many frames per second are displayed.
	 */
	private int mCurrentFps;
	/**
	 * Stores window width.
	 */
	private int mWidth;
	/**
	 * Stores window height.
	 */
	private int mHeight;
	/**
	 * Manages game states.
	 */
	private static StateManager mStateManager;
	/**
	 * Initialize a new game instance with a given width and height.
	 * 
	 * @param mWidth
	 *            The window width.
	 * @param mHeight
	 *            The window height.
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
		mWidth = width;
		mHeight = height;

	}

	/**
	 * Starts the main loop.
	 */
	public void run() {
		// FPS Variables
		long lastFPSCount = System.currentTimeMillis();
		int fpsCounter = 0;
		init();
		while (mRunning) {
			long thisGameUpdate = System.currentTimeMillis();
			if (thisGameUpdate - mLastUpdate > 1000 / TARGET_FPS) {
				update();
				draw();
				mLastUpdate = thisGameUpdate;
				fpsCounter += 1;
			}
			if (System.currentTimeMillis() - lastFPSCount > 1000) {
				mCurrentFps = fpsCounter;
				fpsCounter = 0;
				lastFPSCount = System.currentTimeMillis();
				parentWindow.setTitle("Dorkandoom @ " + mCurrentFps + " FPS");
			}
		}
	}

	/**
	 * Initialize the game and resources.
	 */
	public void init() {
		// Create double buffering method
		createBufferStrategy(2);
		mStrategy = getBufferStrategy();

		requestFocus();
		mStateManager = new StateManager();
	}

	/**
	 * Reacts on user input and update player/enemy movement etc.
	 */
	public void update() {
		try {
			mStateManager.current().update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draws the game to the screen.
	 */
	public void draw() {
		Graphics2D g2d = (Graphics2D) mStrategy.getDrawGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, mWidth, mHeight);

		mStateManager.current().draw(g2d);

		g2d.dispose();
		mStrategy.show();
	}

	private class GameController implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				mStateManager.current().keyDown("left");
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				mStateManager.current().keyDown("right");
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				mStateManager.current().keyDown("space");
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				mStateManager.current().keyDown("up");
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mStateManager.current().keyDown("down");
			} else if (e.getKeyCode() == KeyEvent.VK_Q) {
				mStateManager.current().keyDown("q");
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				mStateManager.current().keyDown("w");
			} else if (e.getKeyCode() == KeyEvent.VK_E) {
				mStateManager.current().keyDown("e");
			} else if (e.getKeyCode() == KeyEvent.VK_1) {
				mStateManager.current().keyDown("1");
			} else if (e.getKeyCode() == KeyEvent.VK_2) {
				mStateManager.current().keyDown("2");
			} else if (e.getKeyCode() == KeyEvent.VK_3) {
				mStateManager.current().keyDown("3");
			} else if (e.getKeyCode() == KeyEvent.VK_4) {
				mStateManager.current().keyDown("4");
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				mStateManager.current().keyUp("left");
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				mStateManager.current().keyUp("right");
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				mStateManager.current().keyUp("space");
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				mStateManager.current().keyUp("up");
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mStateManager.current().keyUp("down");
			} else if (e.getKeyCode() == KeyEvent.VK_Q) {
				mStateManager.current().keyUp("q");
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				mStateManager.current().keyUp("w");
			} else if (e.getKeyCode() == KeyEvent.VK_E) {
				mStateManager.current().keyUp("e");
			} else if (e.getKeyCode() == KeyEvent.VK_1) {
				mStateManager.current().keyUp("1");
			} else if (e.getKeyCode() == KeyEvent.VK_2) {
				mStateManager.current().keyUp("2");
			} else if (e.getKeyCode() == KeyEvent.VK_3) {
				mStateManager.current().keyUp("3");
			} else if (e.getKeyCode() == KeyEvent.VK_4) {
				mStateManager.current().keyUp("4");
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}
	public static StateManager getStates() {
		return mStateManager;
	}
}
