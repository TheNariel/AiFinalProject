package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Gamer.Gamer;

public class Server {
	private final static int NUMBER_OF_DECKS = 1;
	private final static int NUMBER_OF_PLAYERS = 2;

	public static void main(String[] args) {
		List<String> deck = makeDeck(NUMBER_OF_DECKS);
		List<Gamer> players = initPlayers(NUMBER_OF_PLAYERS, NUMBER_OF_DECKS);
		Dealer dealer = new Dealer(deck, players);
		dealer.doGame(15);

	}

	private static List<Gamer> initPlayers(int nPlayers, int nDecks) {
		List<Gamer> players = new ArrayList<Gamer>();
		for (int j = 1; j <= nPlayers; j++) {
			players.add(new Gamer(nDecks,j));
		}
		return players;
	}

	public static List<String> makeDeck(int nDecks) {
		List<String> deck = new ArrayList<String>();

		for (int i = 0; i < nDecks; i++) {
			for (int a = 0; a < 4; a++) {
				deck.add("2");
				deck.add("3");
				deck.add("4");
				deck.add("5");
				deck.add("6");
				deck.add("7");
				deck.add("8");
				deck.add("9");
				deck.add("10");
				deck.add("J");
				deck.add("Q");
				deck.add("K");
				deck.add("A");
			}
		}
		return deck;
	}

}
