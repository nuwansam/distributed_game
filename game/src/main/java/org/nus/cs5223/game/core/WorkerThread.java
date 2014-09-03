package org.nus.cs5223.game.core;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.dao.Game;
import org.nus.cs5223.game.exceptions.PlayerAddWindowExpiredException;
import org.nus.cs5223.game.vo.ErrorMessage;
import org.nus.cs5223.game.vo.JoinGameMessage;
import org.nus.cs5223.game.vo.Message;
import org.nus.cs5223.game.vo.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkerThread implements Runnable {

	private Message message;
	private String id;
	private static final Logger log = Logger.getLogger(WorkerThread.class);

	@Autowired
	private GameManager gameFactory;

	public WorkerThread() {
		id = System.currentTimeMillis() + "thread";
	}

	public GameManager getGameFactory() {
		return gameFactory;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void setGameFactory(GameManager gameFactory) {
		this.gameFactory = gameFactory;
	}

	public WorkerThread(Message message) {
		super();
		this.message = message;
	}

	public void run() {
		if (message instanceof JoinGameMessage) {
			Game game = gameFactory.getGame();
			try {
				game.addPlayer(message.getPlayerId());
				ResponseMessage reply = new ResponseMessage(
						gameFactory.getGame());
				Messenger.sendMessage(message.getOriginIp(),
						message.getResponsePort(), reply);
			} catch (PlayerAddWindowExpiredException e) {
				ErrorMessage reply = new ErrorMessage(
						"Player Add Time window has expired");
				Messenger.sendMessage(message.getOriginIp(),
						message.getResponsePort(), reply);
			}
		} else if (message instanceof ResponseMessage) {
			log.info("Received response message: " + message.getId());
		}
		System.out.println("Received message: " + message.getId());
	}
}
