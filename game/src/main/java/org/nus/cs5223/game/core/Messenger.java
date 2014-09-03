package org.nus.cs5223.game.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.Message;

public class Messenger {

	private static final int SERVER_PORT = 7777;
	private static final Logger log = Logger.getLogger(Messenger.class);

	public static void sendMessage(Message message) {
		sendMessage(getServerIp(), getServerPort(), message);
	}

	private static int getServerPort() {
		// TODO Auto-generated method stub
		return SERVER_PORT;
	}

	private static String getMessageType(Message message) {
		return message.getClass().getName();
	}

	private static String getServerIp() {
		// TODO Auto-generated method stub
		return "localhost";
	}

	public static void sendMessage(String ip, int port, Message message) {
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
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}
}
