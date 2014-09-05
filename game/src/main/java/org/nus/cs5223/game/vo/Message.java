package org.nus.cs5223.game.vo;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.nus.cs5223.game.util.Utils;

public class Message implements Serializable {

	private String id;
	private String playerId;
	private String originIp;
	private int responsePort;
	private long originTime;

	public int getResponsePort() {
		return responsePort;
	}

	public void setResponsePort(int responsePort) {
		this.responsePort = responsePort;
	}

	public String getOriginIp() {
		return originIp;
	}

	public void setOriginIp(String originIp) {
		this.originIp = originIp;
	}

	public Message() {
		this.setId(Utils.getMessageId());
		this.setOriginTime(System.currentTimeMillis());
		try {
			setOriginIp(InetAddress.getLocalHost().getHostAddress());
			setResponsePort(Utils.LISTEN_PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public long getOriginTime() {
		return originTime;
	}

	public void setOriginTime(long originTime) {
		this.originTime = originTime;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Message) {
			return ((Message) obj).getId().equals(getId());
		}
		return false;
	}
}
