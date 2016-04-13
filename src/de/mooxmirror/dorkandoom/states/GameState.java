package de.mooxmirror.dorkandoom.states;

import java.awt.Graphics2D;

/**
 * Interface for game states. Implements basic functions like drawing, updating
 * and initializing.
 * 
 * @author Mooxmirror
 * @version 1.0
 */
public interface GameState {
	/**
	 * Initialize the game state.
	 */
	public void init();

	/**
	 * Draws the game state.
	 */
	public void draw(Graphics2D g2d);

	/**
	 * Updates the game state.
	 */
	public void update();

	/**
	 * React on user keyboard input.
	 */
	public void keyDown(String msg);

	public void keyUp(String msg);
}
