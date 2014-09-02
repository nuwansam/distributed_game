package org.nus.cs5223.game.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMessenger {

	public static void sendMessage(String ip, int port, String message) {
		Socket socket;
		try {
			socket = new Socket(ip, port);
			DataOutputStream dOut = new DataOutputStream(
					socket.getOutputStream());
			dOut.writeUTF(message);
			dOut.flush();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
