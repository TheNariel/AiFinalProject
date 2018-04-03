package server;

import java.util.ArrayList;
import java.util.List;

import Gamer.Gamer;
import Gamer.QGamerFinal;

public class Server {
	private final static int NUMBER_OF_DECKS = 1;
	private final static int NUMBER_OF_PLAYERS = 1;

	public static void main(String[] args) {
		List<Gamer> players = initPlayers(NUMBER_OF_PLAYERS, NUMBER_OF_DECKS);
		Dealer dealer = new Dealer(NUMBER_OF_DECKS, players);
		for (int i = 0; i < 1000; i++) {
			dealer.doGame(1000);
		}
		for (Gamer gamer : players) {
			//gamer.printQTable();
		}

	}

	private static List<Gamer> initPlayers(int nPlayers, int nDecks) {
		List<Gamer> players = new ArrayList<Gamer>();
		for (int j = 1; j <= nPlayers; j++) {
			players.add(new QGamerFinal(nDecks, j));
		}
		return players;
	}

}
