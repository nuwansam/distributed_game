package org.nus.cs5223.game.dao;

public class Player {

	private String id;
	private int currentLocation;
	private int numTreasuresCollected;

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
