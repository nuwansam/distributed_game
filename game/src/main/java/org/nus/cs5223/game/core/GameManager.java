package org.nus.cs5223.game.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.dao.Game;
import org.nus.cs5223.game.vo.Message;
import org.springframework.stereotype.Component;

@Component
public class GameManager {

	public static final int IS_SERVER = 0;
	public static final int IS_BACKUP = 1;
	public static final int IS_CLIENT = 2;

	private int serverStatus;

	private Set<String> messagesReceived;

	private String backupIp="";

	private int boardDimension, numTreasures;
	private static final Logger log = Logger.getLogger(GameManager.class);
	private Game game;

	public String getBackupIp() {
		return backupIp;
	}

	public void setBackupIp(String backupIp) {
		this.backupIp = backupIp;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public GameManager() {
		messagesReceived = new HashSet<String>();
	}

	public int getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(int serverStatus) {
		this.serverStatus = serverStatus;
	}

	public int getBoardDimension() {
		return boardDimension;
	}

	public void setBoardDimension(int boardDimension) {
		this.boardDimension = boardDimension;
	}

	public int getNumTreasures() {
		return numTreasures;
	}

	public void setNumTreasures(int numTreasures) {
		this.numTreasures = numTreasures;
	}

	public void killGame() {
		game = null;
	}

	public Game getGame() {
		if (game != null) {
			return game;
		}
		game = createGame();
		return game;
	}

	public Game createGame() {
		Game game = new Game();
		game.setId(createGameId());
		game.setBoardDimension(boardDimension);
		game.setNumTreasures(numTreasures);
		List<Integer> treasures = new ArrayList<Integer>();
		int numLocations = (boardDimension * boardDimension) - 1;
		for (int i = 0; i < numTreasures; i++) {
			int location = (int) (Math.random() * numLocations);
			treasures.add(location);
		}
		game.setTreasures(treasures);
		log.info("Successfully created game: " + game.getId());
		return game;
	}

	private String createGameId() {
		return System.currentTimeMillis() + "";
	}

	public void addMessagesReceived(Message lastMessage) {
		messagesReceived.add(lastMessage.getId());
	}

	public boolean messageProcessed(Message message) {
		return messagesReceived.contains(message.getId());
	}

}
