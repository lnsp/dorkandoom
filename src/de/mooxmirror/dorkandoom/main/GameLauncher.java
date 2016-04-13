package de.mooxmirror.dorkandoom.main;

/**
 * The GameLauncher launches the game window.
 * 
 * @author Mooxmirror
 * @version 0.1.0-0001
 */
public class GameLauncher {
	public static void main(String args[]) {
		Game myGame = new Game(256, 512);
		myGame.run();
	}
}
