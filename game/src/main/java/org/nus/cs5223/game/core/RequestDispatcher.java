package org.nus.cs5223.game.core;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.Message;
import org.springframework.stereotype.Component;

@Component
public class RequestDispatcher extends Thread {

	private static final int THREAD_COUNT = 10;
	ExecutorService executor;

	public RequestDispatcher() {
		executor = Executors.newFixedThreadPool(THREAD_COUNT);
	}

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
				Message message = (Message) Utils.fromJson(str);
				Runnable worker = new WorkerThread(message);
				executor.execute(worker);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
