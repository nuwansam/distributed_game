package org.nus.cs5223.game.core;

import javax.swing.JFrame;

import org.nus.cs5223.game.ui.MainFrame;
import org.nus.cs5223.game.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Hello world!
 *
 */
@Component
public class App {

	@Autowired
	private RequestDispatcher requestDispatcher;

	@Autowired
	private GameManager gameFactory;

	@Autowired
	private MainFrame mainFrame;

	private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		App server = (App) ctx.getBean(App.class);
		server.init(args);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

	private void init(String[] args) {
		Utils.LISTEN_PORT = Integer.parseInt(args[2]);
		mainFrame.createUI();
		gameFactory.setBoardDimension(Integer.parseInt(args[0]));
		gameFactory.setNumTreasures(Integer.parseInt(args[1]));
		requestDispatcher.start();
	}
}
