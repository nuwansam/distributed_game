package org.nus.cs5223.game.dao;

import java.util.ArrayList;
import java.util.List;

import org.nus.cs5223.game.exceptions.PlayerAddWindowExpiredException;

public class Game {

	private static final long PLAYER_ADD_WINDOW = 20000;
	private long startTime;
	private String id;
	private int boardDimension;
	private int numTreasures;
	private List<Integer> treasures;
	private List<Player> players;

	public Game() {
		startTime = System.currentTimeMillis();
		players = new ArrayList<Player>();
		treasures = new ArrayList<Integer>();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<Integer> getTreasures() {
		return treasures;
	}

	public void setTreasures(List<Integer> treasures) {
		this.treasures = treasures;
	}

	public void addPlayer(String playerId)
			throws PlayerAddWindowExpiredException {
		if (System.currentTimeMillis() - PLAYER_ADD_WINDOW > startTime) {
			throw new PlayerAddWindowExpiredException();
		}
		Player player = new Player(playerId);
		player.setCurrentLocation((int) (Math.random() * (boardDimension
				* boardDimension - 1)));
		player.setNumTreasuresCollected(0);
		players.add(player);
	}

}
