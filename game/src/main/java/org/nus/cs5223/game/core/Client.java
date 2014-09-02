package org.nus.cs5223.game.core;

import javax.swing.JFrame;

import org.nus.cs5223.game.dao.Player;
import org.nus.cs5223.game.ui.MainFrame;

public class Client {
	private Player player;

	public Client() {
	}

	private static void createAndShowGUI(Client client) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		MainFrame frame = new MainFrame("Board Game", client);
		frame.createUI();
	}

	public static void main(String[] args) {
		final Client client = new Client();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(client);
			}
		});
	}
}
