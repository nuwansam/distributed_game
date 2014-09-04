package org.nus.cs5223.game.util;

import java.awt.Point;
import java.net.SocketImpl;

import org.apache.log4j.Logger;
import org.nus.cs5223.game.vo.Message;

import com.google.gson.Gson;

public class Utils {

	private static Gson gson = new Gson();
	private static final Logger log = Logger.getLogger(Utils.class);
	public static final int LISTEN_PORT = 7777;

	public static int getCellNo(Point point, int N) {
		return point.y * N + point.x;
	}

	public static Point getPosition(int cellNo, int N) {
		Point point = new Point();
		point.x = cellNo % N;
		point.y = (int) (cellNo / N);
		return point;
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static Object fromJson(String json, String messageType) {
		try {
			Class<?> className = Class.forName(messageType);
			return gson.fromJson(json, className);
		} catch (ClassNotFoundException e) {
			log.error(e);
		}
		return gson.fromJson(json, Message.class);
	}

	public static String createPlayerId(String name) {
		return "ply_" + name;
	}

	public static String getMessageId() {
		return "msg_" + System.currentTimeMillis();
	}
}
