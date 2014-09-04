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
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.Message;
import org.nus.cs5223.game.vo.ResponseMessage;
import org.springframework.stereotype.Component;

@Component
public class Messenger {

	private static final int SERVER_PORT = 7777;
	private static final Logger log = Logger.getLogger(Messenger.class);
	private static final long MAX_RESPONSE_DELAY = 4000;
	public static List<Message> responseTracked = new ArrayList<Message>();

	private String serverIp;
	private String backupIp;

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

	private static String getMessageType(Message message) {
		return message.getClass().getName();
	}

	public Messenger() {
		Timer timer = new Timer();
		Date date = Calendar.getInstance().getTime();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				checkResponseTracker();

			}
		};
		timer.scheduleAtFixedRate(task, date, 1000);
	}

	protected void checkResponseTracker() {
		Iterator<Message> it = responseTracked.iterator();
		Message message;
		while (it.hasNext()) {
			message = it.next();
			if (message.getOriginTime() + MAX_RESPONSE_DELAY < System
					.currentTimeMillis()) {
				// no response. server is down
				serverIp = backupIp;
				sendMessage(message, true);
				it.remove();
			}
		}
	}

	public void sendMessage(Message message, boolean checkReply) {
		String[] strs = serverIp.split(":");
		sendMessage(strs[0], Integer.parseInt(strs[1]), message, checkReply);
	}

	public void sendMessage(String ip, int port, Message message,
			boolean checkReply) {
		Socket socket;
		String obj = Utils.toJson(message);
		String messageType = getMessageType(message);
		String str = messageType + "|" + obj;
		log.info("Message type: " + messageType);
		try {
			socket = new Socket(ip, port);
			DataOutputStream dOut = new DataOutputStream(
					socket.getOutputStream());
			dOut.writeUTF(str);
			dOut.flush();
			if (checkReply) {
				responseTracked.add(message);
			}
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	public void updateResponseTracker(Message message) {
		responseTracked.remove(message);
		if (message instanceof ResponseMessage) {
			setServerIp(((ResponseMessage) message).getServerIp());
			setBackupIp(((ResponseMessage) message).getBackupIp());
		}
	}

}
