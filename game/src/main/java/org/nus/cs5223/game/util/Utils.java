package org.nus.cs5223.game.util;

import java.awt.Point;

import org.nus.cs5223.game.vo.Message;

import com.google.gson.Gson;

public class Utils {

	private static Gson gson = new Gson();

	public static int getCellNo(int r, int c, int N) {
		return r * N + c;
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

	public static Object fromJson(String json) {
		return gson.fromJson(json, Message.class);
	}

	public static String createPlayerId(String name) {
		return "ply_" + name;
	}

	public static String getMessageId() {
		return "msg_" + System.currentTimeMillis();
	}
}
