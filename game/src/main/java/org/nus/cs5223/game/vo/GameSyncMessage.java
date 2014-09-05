package org.nus.cs5223.game.vo;

import org.nus.cs5223.game.dao.Game;

public class GameSyncMessage extends Message {

	private Game game;
	private Message lastMessage;

	public GameSyncMessage(Game game, Message lastMessage) {
		this.game = game;
		this.lastMessage = lastMessage;
	}

	public Message getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(Message lastMessage) {
		this.lastMessage = lastMessage;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
