package org.nus.cs5223.game.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.dao.Game;
import org.springframework.stereotype.Component;

@Component
public class GameManager {

	private int boardDimension, numTreasures;
	private static final Logger log = Logger.getLogger(GameManager.class);
	private Game game;

	public GameManager() {

	}

	public int getBoardDimension() {
		return boardDimension;
	}

	public void setBoardDimension(int boardDimension) {
		this.boardDimension = boardDimension;
	}

	public int getNumTreasures() {
		return numTreasures;
	}

	public void setNumTreasures(int numTreasures) {
		this.numTreasures = numTreasures;
	}

	public void killGame() {
		game = null;
	}

	public Game getGame() {
		if (game != null) {
			return game;
		}
		game = createGame();
		return game;
	}

	public Game createGame() {
		Game game = new Game();
		game.setId(createGameId());
		game.setBoardDimension(boardDimension);
		game.setNumTreasures(numTreasures);
		List<Integer> treasures = new ArrayList<Integer>();
		int numLocations = (boardDimension * boardDimension) - 1;
		for (int i = 0; i < numTreasures; i++) {
			int location = (int) (Math.random() * numLocations);
			treasures.add(location);
		}
		game.setTreasures(treasures);
		log.info("Successfully created game: " + game.getId());
		return game;
	}

	private String createGameId() {
		return System.currentTimeMillis() + "";
	}

}
