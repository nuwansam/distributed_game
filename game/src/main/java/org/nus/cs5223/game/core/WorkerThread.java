package org.nus.cs5223.game.core;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.dao.Game;
import org.nus.cs5223.game.exceptions.PlayerAddWindowExpiredException;
import org.nus.cs5223.game.ui.MainFrame;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.ErrorMessage;
import org.nus.cs5223.game.vo.GameSyncMessage;
import org.nus.cs5223.game.vo.JoinGameMessage;
import org.nus.cs5223.game.vo.Message;
import org.nus.cs5223.game.vo.MoveMessage;
import org.nus.cs5223.game.vo.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkerThread implements Runnable {

	private Message message;
	private String id;
	private static final Logger log = Logger.getLogger(WorkerThread.class);

	@Autowired
	private Messenger messenger;

	@Autowired
	private GameManager gameManager;

	@Autowired
	private MainFrame mainFrame;

	public WorkerThread() {
		id = System.currentTimeMillis() + "thread";
	}

	public GameManager getGameFactory() {
		return gameManager;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void setGameFactory(GameManager gameFactory) {
		this.gameManager = gameFactory;
	}

	public WorkerThread(Message message) {
		super();
		this.message = message;
	}

	public void run() {

		if (message instanceof JoinGameMessage) {
			Game game = gameManager.getGame();
			try {
				game.addPlayer(message.getPlayerId());
				// second joinee is backup
				if (gameManager.getGame().getPlayers().size() == 2) {
					gameManager.setBackupIp(message.getOriginIp() + ":"
							+ message.getResponsePort());
				}
				if (Utils.getMyIp().equals(gameManager.getBackupIp())) {
					// i was the backup now im getting msgs from others since
					// the
					// server is down. set this messenger the backup
					gameManager.setBackupIp(message.getOriginIp()
							+ message.getResponsePort());
				}
				ResponseMessage reply = new ResponseMessage(
						gameManager.getGame(), Utils.getMyIp(),
						gameManager.getBackupIp());
				messenger.sendMessage(message.getOriginIp(),
						message.getResponsePort(), reply, false);
			} catch (PlayerAddWindowExpiredException e) {
				ErrorMessage reply = new ErrorMessage(
						"Player Add Time window has expired");
				messenger.sendMessage(message.getOriginIp(),
						message.getResponsePort(), reply, false);
			}
		} else if (message instanceof ResponseMessage) {
			mainFrame.updateBoard(((ResponseMessage) message).getGame());
			messenger.updateResponseTracker(message);
		} else if (message instanceof MoveMessage) {
			Game game = gameManager.getGame();
			game.movePlayer(message.getPlayerId(),
					((MoveMessage) message).getDirection());
			if (Utils.getMyIp().equals(gameManager.getBackupIp())) {
				// i was the backup now im getting msgs from others since the
				// server is down. set this messenger the backup
				gameManager.setBackupIp(message.getOriginIp()
						+ message.getResponsePort());

			}
			messenger.sendMessage(message.getOriginIp(), message
					.getResponsePort(),
					new ResponseMessage(gameManager.getGame(), Utils.getMyIp(),
							gameManager.getBackupIp()), false);
		} else if (message instanceof GameSyncMessage) {
			// im getting a game sync message. im the backup server
			gameManager.setBackupIp(Utils.getMyIp());
			gameManager.setGame(((GameSyncMessage) message).getGame());
		}
		System.out.println("Received message: " + message.getId());
	}
}
