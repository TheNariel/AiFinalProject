package Gamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomGamer extends Gamer {
	public int nDecks, id;

	public RandomGamer(int nDecks, int id) {
		this.nDecks = nDecks;
		this.id = id;
	}

	public String getNextAction(List<List<Integer>> table) {
		Random rng = new Random();
		int sum = sumHand(table.get(id));
		if (sum < 21)
			if (rng.nextBoolean())
				return "Hit";

		return "Stand";
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
