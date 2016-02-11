package de.mooxmirror.dorkandoom.states;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the different game states like main menu, ingame, toplist etc.
 * @author Mooxmirror
 * @version 1.0
 */
public class StateManager {
	/**
	 * Stores the game states.
	 */
	List<GameState> states;
	/**
	 * Stores the current game state.
	 */
	GameState currentGameState;
	/**
	 * Initialize the state manager.
	 */
	public StateManager() {
		states = new ArrayList<GameState>();

		states.add(new MainMenuState());
		states.add(new SingleplayerGameState());
		states.add(new HighscoreScreenState());
		states.add(new CreditsScreenState());
		currentGameState = states.get(0);
		currentGameState.init();
	}
	public GameState getCurrentState() {
		return currentGameState;
	}
	public void dropState() {
		currentGameState = null;
	}
	public void loadState(int stateID) {
		currentGameState = null;
		currentGameState = states.get(stateID);
		currentGameState.init();
	}
}
