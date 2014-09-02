package org.nus.cs5223.game.core;

import org.nus.cs5223.game.vo.Message;

public class WorkerThread implements Runnable {

	private Message message;

	public WorkerThread(Message message) {
		super();
		this.message = message;
	}

	public void run() {

	}

}
