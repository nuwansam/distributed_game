package org.nus.cs5223.game.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.GameSyncMessage;
import org.nus.cs5223.game.vo.JoinGameMessage;
import org.nus.cs5223.game.vo.Message;
import org.nus.cs5223.game.vo.MoveMessage;
import org.nus.cs5223.game.vo.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Messenger {

	private static final int SERVER_PORT = 7777;
	private static final Logger log = Logger.getLogger(Messenger.class);
	private static final long MAX_RESPONSE_DELAY = 4000;
	public static List<Message> responseTracked;

	private String serverIp;
	private String backupIp;

	@Autowired
	GameManager gameManager;
	private String playerId;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
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
		log.info("setting backup in " + Utils.getMyIp() + " to " + backupIp);
		this.backupIp = backupIp;
	}

	private static String getMessageType(Message message) {
		return message.getClass().getName();
	}

	public Messenger() {
		responseTracked = new ArrayList<Message>();
		Timer timer = new Timer();
		Date date = Calendar.getInstance().getTime();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				checkResponseTracker();
			}
		};
		timer.scheduleAtFixedRate(task, date, 3000);
	}

	private void scheduleNoMoveMsgSend() {
		Timer timer = new Timer();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, 25);
		Date date = c.getTime();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				sendNoMoveMsg();
			}
		};
		// timer.scheduleAtFixedRate(task, date, 25000);

	}

	protected void sendNoMoveMsg() {
		MoveMessage msg = new MoveMessage();
		msg.setPlayerId(playerId);
		msg.setDirection(MoveMessage.NO_MOVE);
		sendMessage(msg, true);
	}

	protected void checkResponseTracker() {
		synchronized (responseTracked) {
			Iterator<Message> it = responseTracked.iterator();
			Message message;
			while (it.hasNext()) {
				message = it.next();
				if (message.getOriginTime() + MAX_RESPONSE_DELAY < System
						.currentTimeMillis()) {
					if (message instanceof GameSyncMessage) {
						// backup is down. im the server and my game sync
						// message
						// didnt get a response
						setBackupIp("");
						try {
							it.remove();
						} catch (Exception e) {
							log.info("Update exception in " + Utils.getMyIp());
						}
					} else {
						// no response. server is down
						serverIp = backupIp;
						log.info("Changing server IP to: " + serverIp);
						log.info("Resending message: " + message.getId());
						sendMessage(message, true);
						try {
							it.remove();
						} catch (Exception e) {
							log.info("Update exception in " + Utils.getMyIp());
						}
					}
				}
			}
		}
	}

	public void sendMessage(Message message, boolean checkReply) {
		if (serverIp == null || serverIp.isEmpty()) {
			serverIp = Utils.getMyIp();
		}
		String[] strs = serverIp.split(":");
		log.info("SERVER IP:" + serverIp);
		sendMessage(strs[0], Integer.parseInt(strs[1]), message, checkReply);
	}

	public void sendMessage(String ip, int port, Message message,
			boolean checkReply) {
		if (message instanceof JoinGameMessage) {
			scheduleNoMoveMsgSend();
		}
		log.info("Sending message: my IP: " + Utils.getMyIp() + " serverIP: "
				+ serverIp + " Backup: " + backupIp + " to " + ip + ":" + port);
		Socket socket;
		String obj = Utils.toJson(message);
		String messageType = getMessageType(message);
		String str = messageType + "|" + obj;
		log.info("Message type: " + messageType);
		try {
			if (checkReply) {
				synchronized (responseTracked) {
					responseTracked.add(message);
				}
			}
			socket = new Socket(ip, port);
			DataOutputStream dOut = new DataOutputStream(
					socket.getOutputStream());
			dOut.writeUTF(str);
			dOut.flush();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void updateResponseTracker(Message message) {
		synchronized (responseTracked) {
			log.info("Removing msg: " + message.getId());
			responseTracked.remove(message);
		}
	}

	public void sendMessage(String receiverIp, Message message,
			boolean checkReply) {
		String[] strs = receiverIp.split(":");
		sendMessage(strs[0], Integer.parseInt(strs[1]), message, checkReply);
	}

}
