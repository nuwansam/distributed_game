package org.nus.cs5223.game.dao;

public class Player {

	private String id;
	private int currentLocation;
	private int previousLocation;
	private int numTreasuresCollected;

	public boolean equals(Object o) {
		if (o instanceof Player) {
			return ((Player) o).getId().equals(getId());
		}
		return false;
	}

	public int getPreviousLocation() {
		return previousLocation;
	}

	public void setPreviousLocation(int previousLocation) {
		this.previousLocation = previousLocation;
	}

	public Player(String playerId) {
		this.id = playerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
	}

	public int getNumTreasuresCollected() {
		return numTreasuresCollected;
	}

	public void setNumTreasuresCollected(int numTreasuresCollected) {
		this.numTreasuresCollected = numTreasuresCollected;
	}

}
