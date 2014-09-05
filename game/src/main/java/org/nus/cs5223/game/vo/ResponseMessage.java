package org.nus.cs5223.game.vo;

import org.nus.cs5223.game.dao.Game;

public class ResponseMessage extends Message {

	private Game game;
	private String serverIp;
	private String backupIp;
	private Message originMessage;

	public Message getOriginMessage() {
		return originMessage;
	}

	public void setOriginMessage(Message originMessage) {
		this.originMessage = originMessage;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getBackupIp() {
		return backupIp;
	}

	public void setBackupIp(String backupIp) {
		this.backupIp = backupIp;
	}

	public ResponseMessage(Game game, String serverIp, String backupIp,
			Message message) {
		super();
		this.game = game;
		this.serverIp = serverIp;
		this.backupIp = backupIp;
		this.originMessage = message;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
