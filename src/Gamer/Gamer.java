package Gamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gamer {
	int nDecks, id;

	public Gamer(int nDecks, int id) {
		this.nDecks = nDecks;
		this.id = id;
	}

	public String getNextAction(List<List<String>> table) {
		int sum = sumHand(table.get(id));

		if (sum < 17)
			return "Hit";

		return "Stand";
	}

	public void handOver(List<List<String>> table) {
		// ToDo some kind of statistics maybe.
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
