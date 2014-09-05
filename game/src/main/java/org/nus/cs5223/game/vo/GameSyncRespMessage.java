package org.nus.cs5223.game.vo;

public class GameSyncRespMessage extends Message {

	private Message originMessage;

	public GameSyncRespMessage(Message message) {
		this.originMessage = message;
	}

	public Message getOriginMessage() {
		return originMessage;
	}

	public void setOriginMessage(Message originMessage) {
		this.originMessage = originMessage;
	}


}
