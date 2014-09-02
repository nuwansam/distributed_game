package org.nus.cs5223.game.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.nus.cs5223.game.core.Client;
import org.nus.cs5223.game.core.ClientMessenger;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.JoinGameMessage;

public class MainFrame extends JFrame {

	private Client client;
	JTextField txtName;

	public MainFrame(String string, Client client) {
		super(string);
		this.client = client;
	}

	public void createUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the ubiquitous "Hello World" label.
		txtName = new JTextField();
		JButton btnJoin = new JButton("Join Game");
		btnJoin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				joinGame();
			}
		});

		JPanel panel = new JPanel(new GridLayout());
		panel.add(txtName);
		panel.add(btnJoin);
		setContentPane(panel);

		// Display the window.
		this.pack();
		this.setVisible(true);

	}

	protected void joinGame() {
		String playerId = Utils.createPlayerId(txtName.getText());
		client.setPlayer(playerId);
		JoinGameMessage message = new JoinGameMessage(playerId);
		ClientMessenger.sendMessage(message);
	}
}
