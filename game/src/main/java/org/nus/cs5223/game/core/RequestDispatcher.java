package org.nus.cs5223.game.core;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestDispatcher extends Thread {

	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("Listener Running . . .");
			Socket socket = null;
			while ((socket = serverSocket.accept()) != null) {
				DataInputStream stream = new DataInputStream(
						socket.getInputStream());
				String str = stream.readUTF();
				System.out.println("Received: " + str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
