package org.nus.cs5223.game.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.Message;

public class ClientMessenger {

	private static final int SERVER_PORT = 7777;

	public static void sendMessage(Message message) {
		Socket socket;
		String str = Utils.toJson(message);
		try {
			socket = new Socket(getServerIp(), SERVER_PORT);
			DataOutputStream dOut = new DataOutputStream(
					socket.getOutputStream());
			dOut.writeUTF(str);
			dOut.flush();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getServerIp() {
		// TODO Auto-generated method stub
		return "localhost";
	}
}
