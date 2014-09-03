package org.nus.cs5223.game.vo;

public class JoinGameMessage extends Message {

	public JoinGameMessage() {
		super();
	}

	public JoinGameMessage(String playerId) {
		super();
		this.setPlayerId(playerId);
	}

}
