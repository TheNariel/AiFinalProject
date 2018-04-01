package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Gamer.Gamer;

public class Dealer {
	int nDecks;
	List<Integer> deck;
	List<Gamer> players;
	List<List<Integer>> table;
	List<String> nextActions = new ArrayList<>();
	// [payoff,win,lose,bust,tie]
	List<int[]> gameResults;
	List<Integer> dealerCard = new ArrayList<>();

	public Dealer(int nDecks, List<Gamer> players) {
		this.nDecks = nDecks;
		this.deck = makeDeck(nDecks);
		this.players = players;
		for (Gamer gamer : players) {
			gamer.newDeck();
		}
		init();
	}

	private void init() {
		cleenUp();
		gameResults = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
			int[] tempRes = new int[5];
			tempRes[0] = 0;
			tempRes[1] = 0;
			tempRes[2] = 0;
			tempRes[3] = 0;
			tempRes[4] = 0;
			gameResults.add(tempRes);
		}
		Collections.shuffle(deck);

	}

	public void doGame(int nHands) {
		init();
		for (int i = 0; i < nHands; i++) {
			if (deck.size() < (3 * (2 + 2 * players.size()))) {
				deck = makeDeck(nDecks);
				for (Gamer gamer : players) {
					gamer.newDeck();
				}
				Collections.shuffle(deck);

			}
			dealerCard = new ArrayList<>();

			for (int p = 1; p < table.size(); p++) {
				table.get(p).add(deck.remove(0));
			}

			table.get(0).add(deck.remove(0));

			for (int p = 1; p < table.size(); p++) {
				table.get(p).add(deck.remove(0));
			}
			dealerCard.add(deck.remove(0));

			for (int p = 0; p < players.size(); p++) {
				while (players.get(p).getNextAction(table).equals("Hit")) {
					table.get(p + 1).add(deck.remove(0));
					players.get(p).learn(table, 0, 0);
				}
			}

			dealerCard.addAll(table.get(0));
			while (sumHand(dealerCard) < 17) {
				dealerCard.add(deck.remove(0));
			}
			
			results(table);
			for (Gamer gamer : players) {
				gamer.handOver(table);
			}
			cleenUp();
			
		}
		printOutResults();
		for (Gamer gamer : players) {
			//gamer.printQTable();
		}
	}

	private void printOutResults() {

		/*for (int[] r : gameResults) {

			System.out.print("[");

			for (int i = 0; i < r.length - 1; i++) {
				System.out.print(r[i] + " ");
			}
			System.out.print(r[r.length - 1]);

			System.out.println("]");

		}*/

		System.out.println(gameResults.get(0)[0]);
	}

	private void cleenUp() {
		table = new ArrayList<List<Integer>>();
		for (int i = 0; i <= players.size(); i++) {
			table.add(new ArrayList<Integer>());
		}
	}

	private void results(List<List<Integer>> table) {
		// System.out.println(table);
		List<Integer> results = new ArrayList<>();
		for (int i = 0; i < table.size(); i++) {
			results.add(sumHand(table.get(i)));
		}

		// [payoff,win,lose,bust,tie]
		int dealerScore =sumHand(dealerCard);
		int playerScore;
		for (int i = 0; i < results.size() - 1; i++) {
			playerScore = results.get(i + 1);

			if (playerScore > 21) {
				gameResults.get(i)[0] -= 10;
				gameResults.get(i)[3] += 1;
				players.get(i).learn(table, -10, 1);
			} else {
				if (dealerScore > 21) {
					gameResults.get(i)[0] += 10;
					gameResults.get(i)[1] += 1;
					players.get(i).learn(table, 10, 1);
				} else {
					if (playerScore == dealerScore) {
						gameResults.get(i)[4] += 1;
						players.get(i).learn(table, 0, 1);
					}
					if (playerScore > dealerScore) {
						gameResults.get(i)[0] += 10;
						gameResults.get(i)[1] += 1;
						players.get(i).learn(table, 10, 1);
					}
					if (playerScore < dealerScore) {
						gameResults.get(i)[0] -= 10;
						gameResults.get(i)[2] += 1;
						players.get(i).learn(table, -10, 1);
					}

				}

			}

		}
	}

	private int sumHand(List<Integer> hand) {
		int sum = 0;

		Collections.sort(hand);
		for (Integer value : hand) {
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

	public List<Integer> makeDeck(int nDecks) {
		List<Integer> deck = new ArrayList<Integer>();

		for (int i = 0; i < nDecks; i++) {
			for (int a = 0; a < 4; a++) {
				deck.add(2);
				deck.add(3);
				deck.add(4);
				deck.add(5);
				deck.add(6);
				deck.add(7);
				deck.add(8);
				deck.add(9);
				deck.add(10);
				deck.add(10);
				deck.add(10);
				deck.add(10);
				deck.add(11);
			}
		}
		return deck;
	}

}

/*
 * public int getValue(String card) {
 * 
 * switch (card) { case "2": return 2; case "3": return 3; case "4": return 4;
 * case "5": return 5; case "6": return 6; case "7": return 7; case "8": return
 * 8; case "9": return 9; case "10": return 10; case "J": return 10; case "Q":
 * return 10; case "K": return 10; case "A": return 11; default: return 0; }
 * 
 * }
 */
