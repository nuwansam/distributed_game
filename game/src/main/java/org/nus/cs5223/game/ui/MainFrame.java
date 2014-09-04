package org.nus.cs5223.game.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.nus.cs5223.game.core.GameManager;
import org.nus.cs5223.game.core.Messenger;
import org.nus.cs5223.game.dao.Game;
import org.nus.cs5223.game.dao.Player;
import org.nus.cs5223.game.util.Utils;
import org.nus.cs5223.game.vo.JoinGameMessage;
import org.nus.cs5223.game.vo.MoveMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFrame extends JFrame {

	JTextField txtName;
	JTextField txtServer;
	JTextArea txtLog;
	JTextField txtMove;
	JTable board;
	String playerId;
	DefaultTableModel boardModel;

	@Autowired
	GameManager gameManager;

	@Autowired
	Messenger messenger;

	public MainFrame() {
		setTitle("Running on: " + Utils.LISTEN_PORT);
	}

	public MainFrame(String string) {
		super(string);
	}

	public void createUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		KeyListener keyListener = new keyListener();
		this.addKeyListener(keyListener);
		// Add the ubiquitous "Hello World" label.
		txtName = new JTextField("", 20);
		txtServer = new JTextField("", 20);
		txtMove = new JTextField("", 20);
		txtLog = new JTextArea(20, 60);
		board = new JTable();
		board.addKeyListener(keyListener);
		boardModel = new DefaultTableModel();

		board.setDefaultRenderer(Object.class, new TableCellRenderer() {

			public java.awt.Component getTableCellRendererComponent(
					JTable arg0, Object obj, boolean arg2, boolean arg3,
					int arg4, int arg5) {
				JLabel lbl;
				if (obj instanceof Player) {
					lbl = new JLabel(((Player) obj).getId().substring(0, 1));
					lbl.setToolTipText(((Player) obj).getId());
				} else {
					lbl = new JLabel((String) obj);
				}
				return lbl;

			}
		});

		/*
		 * boardModel.setColumnCount(gameManager.getBoardDimension());
		 * boardModel.setRowCount(gameManager.getBoardDimension());
		 */board.setModel(boardModel);

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
		panel.addKeyListener(keyListener);
		// panel.add(new JLabel("Move:"));
		// panel.add(txtMove);
		// JButton btnMove = new JButton("Move");
		// btnMove.addActionListener(new ActionListener() {
		//
		// public void actionPerformed(ActionEvent arg0) {
		// sendMoveMessage();
		// }
		// });
		// panel.add(btnMove, "wrap");
		panel.add(board, "span");
		setContentPane(panel);

		// Display the window.
		this.pack();
		this.setVisible(true);

	}

	protected void captureKey(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			sendMoveMessage(0);
			break;
		case KeyEvent.VK_DOWN:
			sendMoveMessage(2);
			break;
		case KeyEvent.VK_LEFT:
			sendMoveMessage(3);
			break;
		case KeyEvent.VK_RIGHT:
			sendMoveMessage(1);
			break;
		}
	}

	public void updateBoard(Game game) {
		Point point;
		boardModel.setColumnCount(game.getBoardDimension());
		boardModel.setRowCount(game.getBoardDimension());
		for (Integer t : game.getTreasures()) {
			point = Utils.getPosition(t, game.getBoardDimension());
			boardModel.setValueAt("T", point.y, point.x);
			boardModel.fireTableCellUpdated(point.y, point.x);
		}
		for (Player p : game.getPlayers()) {
			point = Utils.getPosition(p.getCurrentLocation(),
					game.getBoardDimension());
			boardModel.setValueAt(p, point.y, point.x);
			boardModel.fireTableCellUpdated(point.y, point.x);
			point = Utils.getPosition(p.getPreviousLocation(),
					game.getBoardDimension());
			if (!Utils.getPosition(p.getCurrentLocation(),
					game.getBoardDimension()).equals(point)) {
				if (p.equals(board.getValueAt(point.y, point.x))) {
					boardModel.setValueAt("", point.y, point.x);
				}
				boardModel.fireTableCellUpdated(point.y, point.x);
			}
		}
	}

	protected void sendMoveMessage(int dir) {
		MoveMessage message = new MoveMessage();
		message.setPlayerId(playerId);
		message.setDirection(dir);
		messenger.sendMessage(message, true);
	}

	public void addLog(String message) {
		txtLog.append(message + System.lineSeparator());
	}

	protected void joinGame() {
		playerId = Utils.createPlayerId(txtName.getText());
		JoinGameMessage message = new JoinGameMessage(playerId);
		String str = txtServer.getText().trim();
		if (str.isEmpty()) {
			str = "localhost" + Utils.LISTEN_PORT;
		}
		messenger.setServerIp(str);
		messenger.sendMessage(message, true);
	}

	private class keyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {
			captureKey(e);
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
