package Gamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BasicGamer extends Gamer {
	public int nDecks, id;

	public BasicGamer(int nDecks, int id) {
		this.nDecks = nDecks;
		this.id = id;
	}

	public String getNextAction(List<List<Integer>> table) {
		int sum = sumHand(table.get(id));
		if (sum < 17)
			return "Hit";

		return "Stand";
	}

	public void handOver(int payout,List<List<Integer>> table) {
		// ToDo some kind of statistics maybe.
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

}
