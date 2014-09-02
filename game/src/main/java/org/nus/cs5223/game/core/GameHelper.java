package org.nus.cs5223.game.core;

import java.util.ArrayList;
import java.util.List;

import org.nus.cs5223.game.dao.Game;
import org.springframework.stereotype.Component;

@Component
public class GameHelper {

	public Game createGame(int N, int M) {
		Game game = new Game();
		game.setId(createGameId());
		game.setN(N);
		game.setNumTreasures(M);
		List<Integer> treasures = new ArrayList<Integer>();
		int numLocations = (N * N) - 1;
		for (int i = 0; i < M; i++) {
			int location = (int) (Math.random() * numLocations);
			treasures.add(location);
		}
		game.setTreasures(treasures);
		return game;
	}

	private String createGameId() {
		return System.currentTimeMillis() + "";
	}
}
