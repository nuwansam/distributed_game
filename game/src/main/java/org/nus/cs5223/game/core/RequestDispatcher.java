package org.nus.cs5223.game.core;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.ui.MainFrame;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class RequestDispatcher extends Thread {

	private static final int THREAD_COUNT = 10;
	private static final Logger log = Logger.getLogger(RequestDispatcher.class);

	@Autowired
	TaskExecutor executor;

	@Autowired
	MainFrame mainFrame;
	
	@Autowired
	ApplicationContext applicationContext;

	public RequestDispatcher() {
	}

	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(Utils.LISTEN_PORT);
			log.info("Listener Running . . .");
			Socket socket = null;
			while ((socket = serverSocket.accept()) != null) {
				DataInputStream stream = new DataInputStream(
						socket.getInputStream());
				String str = stream.readUTF();
				String[] strs = str.split("\\|");
				String messageType = strs[0];
				String obj = strs[1];
				String msg = "Received: " + messageType + " message. " + obj;
				log.info(msg);
				mainFrame.addLog(msg);
				Message message = (Message) Utils.fromJson(obj, messageType);
				WorkerThread worker = applicationContext
						.getBean(WorkerThread.class);
				worker.setMessage(message);
				executor.execute(worker);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
