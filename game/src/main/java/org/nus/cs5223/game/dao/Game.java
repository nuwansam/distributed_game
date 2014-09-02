package org.nus.cs5223.game.dao;

import java.util.List;
import java.util.Map;

public class Game {

	private String id;
	private int N;
	private int numTreasures;
	private List<Integer> treasures;
	private List<Player> players;

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

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
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

}
