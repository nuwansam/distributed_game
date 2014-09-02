package org.nus.cs5223.game.vo;

import java.io.Serializable;

import org.nus.cs5223.game.util.Utils;

public abstract class Message implements Serializable {

	private String id;
	private String playerId;

	public Message() {
		this.setId(Utils.getMessageId());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

}
