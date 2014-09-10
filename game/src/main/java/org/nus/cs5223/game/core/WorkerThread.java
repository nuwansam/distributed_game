package org.nus.cs5223.game.core;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.dao.Game;
import org.nus.cs5223.game.exceptions.PlayerAddWindowExpiredException;
import org.nus.cs5223.game.ui.MainFrame;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.ErrorMessage;
import org.nus.cs5223.game.vo.GameSyncRespMessage;
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
					messenger.setBackupIp(message.getOriginIp() + ":"
							+ message.getResponsePort());
				}
				ResponseMessage reply = new ResponseMessage(
						gameManager.getGame(), Utils.getMyIp(),
						messenger.getBackupIp(), message);
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
			messenger.updateResponseTracker(((ResponseMessage) message)
					.getOriginMessage());
			messenger.setServerIp(((ResponseMessage) message).getServerIp());
			messenger.setBackupIp(((ResponseMessage) message).getBackupIp());
		} else if (message instanceof MoveMessage) {
			Game game = gameManager.getGame();
			if (Utils.getMyIp().equals(messenger.getBackupIp())) {
				// i was the backup now im getting msgs from others since the
				// server is down. set this messenger the backup
				messenger.setBackupIp(message.getOriginIp() + ":"
						+ message.getResponsePort());
				messenger.setServerIp(Utils.getMyIp());
				messenger.setBackupIp(messenger.getBackupIp());
				if (!gameManager.messageProcessed(message)) {
					game.movePlayer(message.getPlayerId(),
							((MoveMessage) message).getDirection());
				}
			} else {
				// i am the server. just process it
				game.movePlayer(message.getPlayerId(),
						((MoveMessage) message).getDirection());
				if (messenger.getBackupIp().isEmpty()
						&& !Utils.getMyIp().equals(Utils.getIp(message))) {
					// backup is down. appoint this as the backup
					messenger.setBackupIp(message.getOriginIp() + ":"
							+ message.getResponsePort());
				}
			}
			messenger.sendMessage(messenger.getBackupIp(), new GameSyncMessage(
					gameManager.getGame(), message), true);
			messenger.sendMessage(message.getOriginIp(), message
					.getResponsePort(),
					new ResponseMessage(gameManager.getGame(), Utils.getMyIp(),
							messenger.getBackupIp(), message), false);
		} else if (message instanceof GameSyncMessage) {
			// im getting a game sync message. im the backup server
			messenger.setBackupIp(Utils.getMyIp());
			gameManager.setGame(((GameSyncMessage) message).getGame());
			gameManager.addMessagesReceived(((GameSyncMessage) message)
					.getLastMessage());
			messenger.sendMessage(new GameSyncRespMessage(message), false);
		} else if (message instanceof GameSyncRespMessage) {
			messenger.updateResponseTracker(((GameSyncRespMessage) message)
					.getOriginMessage());
		}
	}
}
