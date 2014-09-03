package org.nus.cs5223.game.vo;

import org.nus.cs5223.game.dao.Game;

public class ResponseMessage extends Message {

	private Game game;

	public ResponseMessage(Game game) {
		super();
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
