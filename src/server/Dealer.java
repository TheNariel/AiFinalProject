package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Gamer.Gamer;

public class Dealer {
	List<String> deckOriginal;
	List<String> deck;
	List<Gamer> players;
	List<List<String>> table;
	List<String> nextActions = new ArrayList<>();
	int[] gameResults;
	boolean allStand;

	public Dealer(List<String> deck, List<Gamer> players) {
		this.deckOriginal = deck;
		this.deck = deck;
		this.players = players;

		cleenUp();
		gameResults = new int[players.size()];
		for (int i = 0; i < gameResults.length; i++) {
			gameResults[i] = 0;
		}
		Collections.shuffle(deck);
	}

	public void doGame(int nHands) {
		for (int i = 0; i < nHands; i++) {
			if (deck.size() < 3 * (2 + 2 * players.size())) {
				deck = new ArrayList<>(deckOriginal);
				Collections.shuffle(deck);
			}
			String dealerCard;

			for (int p = 1; p < table.size(); p++) {
				table.get(p).add(deck.remove(0));
			}

			table.get(0).add(deck.remove(0));

			for (int p = 1; p < table.size(); p++) {
				table.get(p).add(deck.remove(0));
			}
			dealerCard = deck.remove(0);

			for (int p = 0; p < players.size(); p++) {
				while (players.get(p).getNextAction(table).equals("Hit")) {
					table.get(p + 1).add(deck.remove(0));
				}
			}

			table.get(0).add(dealerCard);
			while (sumHand(table.get(0)) < 17) {
				table.get(0).add(deck.remove(0));
			}
			results(table);
			cleenUp();
		}
	}

	private void cleenUp() {
		table = new ArrayList<List<String>>();
		for (int i = 0; i <= players.size(); i++) {
			table.add(new ArrayList<String>());
		}
	}

	private void results(List<List<String>> table) {
		System.out.println(table);
		List<Integer> results = new ArrayList<>();
		for (int i = 0; i < table.size(); i++) {
			results.add(sumHand(table.get(i)));
		}
		for (int i = 0; i < table.size(); i++) {
			results.add(sumHand(table.get(i)));
		}
		// System.out.println(results);
	}

	private int sumHand(List<String> hand) {
		int sum = 0;

		List<Integer> values = new ArrayList<>();
		for (int i = 0; i < hand.size(); i++) {
			values.add(getValue(hand.get(i)));
		}
		Collections.sort(values);
		for (Integer value : values) {
			if (value == 11) {
				if (sum + value > 21) {
					sum += 1;
				} else {
					sum += value;
				}
			} else {
				sum += value;
			}

		}
		return sum;
	}

	public int getValue(String card) {

		switch (card) {
		case "2":
			return 2;
		case "3":
			return 3;
		case "4":
			return 4;
		case "5":
			return 5;
		case "6":
			return 6;
		case "7":
			return 7;
		case "8":
			return 8;
		case "9":
			return 9;
		case "10":
			return 10;
		case "J":
			return 10;
		case "Q":
			return 10;
		case "K":
			return 10;
		case "A":
			return 11;
		default:
			return 0;
		}

	}

}
