package org.nus.cs5223.game.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.nus.cs5223.game.core.Messenger;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.JoinGameMessage;
import org.springframework.stereotype.Component;

@Component
public class MainFrame extends JFrame {

	JTextField txtName;
	JTextField txtServer;
	JTextArea txtLog;

	public MainFrame() {
		setTitle("Running on: " + Utils.LISTEN_PORT);
	}

	public MainFrame(String string) {
		super(string);
	}

	public void createUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the ubiquitous "Hello World" label.
		txtName = new JTextField();
		txtServer = new JTextField();
		txtLog = new JTextArea();
		JButton btnJoin = new JButton("Join Game");
		btnJoin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				joinGame();
			}
		});

		JPanel panel = new JPanel(new GridLayout());
		panel.add(txtServer);
		panel.add(txtName);
		panel.add(btnJoin);
		panel.add(txtLog);
		setContentPane(panel);

		// Display the window.
		this.pack();
		this.setVisible(true);

	}

	public void addLog(String message) {
		txtLog.append(message + System.lineSeparator());
	}

	protected void joinGame() {
		String playerId = Utils.createPlayerId(txtName.getText());
		JoinGameMessage message = new JoinGameMessage(playerId);
		String[] strs = txtServer.getText().split(":");
		if (txtServer.getText().isEmpty()) {
			strs = new String[] { "localhost", Utils.LISTEN_PORT + "" };
		}
		Messenger.sendMessage(strs[0], Integer.parseInt(strs[1]), message);
	}
}
