package org.nus.cs5223.game.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.nus.cs5223.game.core.Messenger;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.JoinGameMessage;
import org.nus.cs5223.game.vo.MoveMessage;
import org.springframework.stereotype.Component;

@Component
public class MainFrame extends JFrame {

	JTextField txtName;
	JTextField txtServer;
	JTextArea txtLog;
	JTextField txtMove;
	String playerId;

	public MainFrame() {
		setTitle("Running on: " + Utils.LISTEN_PORT);
	}

	public MainFrame(String string) {
		super(string);
	}

	public void createUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the ubiquitous "Hello World" label.
		txtName = new JTextField("", 20);
		txtServer = new JTextField("", 20);
		txtMove = new JTextField("", 20);
		txtLog = new JTextArea(20, 60);
		JButton btnJoin = new JButton("Join Game");
		btnJoin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				joinGame();
			}
		});

		JPanel panel = new JPanel();
		MigLayout layout = new MigLayout();
		panel.setLayout(layout);

		panel.add(new JLabel("Server IP:"));
		panel.add(txtServer, "wrap");
		panel.add(new JLabel("Player Name:"));
		panel.add(txtName);
		panel.add(btnJoin, "wrap");
		panel.add(new JLabel("Move:"));
		panel.add(txtMove);
		JButton btnMove = new JButton("Move");
		btnMove.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				sendMoveMessage();
			}
		});
		panel.add(btnMove, "wrap");
		panel.add(txtLog, "span");
		setContentPane(panel);

		// Display the window.
		this.pack();
		this.setVisible(true);

	}

	protected void sendMoveMessage() {
		MoveMessage message = new MoveMessage();
		message.setPlayerId(playerId);
		message.setDirection(Integer.parseInt(txtMove.getText().trim()));
		String[] strs = txtServer.getText().split(":");
		if (txtServer.getText().isEmpty()) {
			strs = new String[] { "localhost", Utils.LISTEN_PORT + "" };
		}
		Messenger.sendMessage(strs[0], Integer.parseInt(strs[1]), message);
	}

	public void addLog(String message) {
		txtLog.append(message + System.lineSeparator());
	}

	protected void joinGame() {
		playerId = Utils.createPlayerId(txtName.getText());
		JoinGameMessage message = new JoinGameMessage(playerId);
		String[] strs = txtServer.getText().split(":");
		if (txtServer.getText().isEmpty()) {
			strs = new String[] { "localhost", Utils.LISTEN_PORT + "" };
		}
		Messenger.sendMessage(strs[0], Integer.parseInt(strs[1]), message);
	}
}
