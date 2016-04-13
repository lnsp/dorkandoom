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
	List<GameState> mStates;
	/**
	 * Stores the current game state.
	 */
	GameState mCurrentState;
	/**
	 * Initialize the state manager.
	 */
	public StateManager() {
		mStates = new ArrayList<GameState>();

		mStates.add(new MainMenuState());
		mStates.add(new SingleplayerGameState());
		mStates.add(new HighscoreScreenState());
		mStates.add(new CreditsScreenState());
		mCurrentState = mStates.get(0);
		mCurrentState.init();
	}
	public GameState current() {
		return mCurrentState;
	}
	public void drop() {
		mCurrentState = null;
	}
	public void load(int id) {
		mCurrentState = null;
		mCurrentState = mStates.get(id);
		mCurrentState.init();
	}
}
