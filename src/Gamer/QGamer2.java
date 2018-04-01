package Gamer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class QGamer2 extends Gamer {
	public int nDecks, id;
	double toTrain = 1000, toTrainMax = 1000;
	List<Integer> deck;
	Map<String, List<Double>> qTable = new HashMap<String, List<Double>>();
	final List<String> actions = new ArrayList<String>(Arrays.asList("Hit", "Stand"));
	String oldTable;

	double alpha = 0.6, gamma = 0.75, epsilon = 1;

	public QGamer2(int nDecks, int id) {
		this.nDecks = nDecks;
		this.id = id;
	}

	public void newDeck() {
		this.deck = makeDeck(nDecks);
	}

	public String getNextAction(List<List<Integer>> table) {
		Random rng = new Random();
		String state = makeState(table);
		oldTable = state;
		addToTableIfNew(state);

		if (toTrain < 0) {
			if (sumHand(table.get(id)) > 21)
				return "Stand";

			if (rng.nextDouble() > epsilon) {
				return actions.get(getMaxQAction(state));

			} else {
				if (rng.nextBoolean())
					return "Hit";
				return "Stand";
			}
		} else {

			int sum = sumHand(table.get(id));
			if (sum < 17)
				return "Hit";

			return "Stand";
		}
	}

	public void printQTable() {

		/*
		 * List<String> list = new ArrayList<>(); Set<String> keys = qTable.keySet();
		 * for (String string : keys) { list.add(string); } Collections.sort(list); for
		 * (String string : list) { System.out.println(string + " = " +
		 * qTable.get(string));
		 * 
		 * }
		 */

		// System.out.println(qTable.size());
		System.out.println(qTable);
	}

	public void handOver(List<List<Integer>> table) {

	}

	public void learn(List<List<Integer>> table, int reward, int action) {
		List<Double> temp = qTable.get(oldTable);
		double Q = temp.get(action);

		double maxNextQ = getMaxQ(makeState(table));
		double update = Q + alpha * (reward + gamma * maxNextQ - Q);
		temp.set(action, update);

		epsilon = (toTrain / toTrainMax * 2);
		if (reward != 0)
			toTrain--;
		if (toTrain < 0) {
			epsilon = 0;
			alpha = 0;
		}
		// System.out.println(epsilon);
	}

	private void addToTableIfNew(String state) {
		if (!qTable.containsKey(state)) {
			List<Double> temp = new ArrayList<>();
			for (String string : actions) {
				temp.add(0.0);
			}
			qTable.put(state, temp);
		}
	}

	private Double getMaxQ(String state) {
		List<Double> temp = qTable.get(state);
		if (temp == null)
			return 0.0;
		return Collections.max(temp);
	}

	private int getMaxQAction(String state) {
		Random rng = new Random();
		List<Double> temp = qTable.get(state);

		if (temp.get(0).equals(temp.get(1)))
			return rng.nextInt(2);

		return temp.indexOf(Collections.max(temp));
	}

	private String makeState(List<List<Integer>> table) {
		List<List<Integer>> state = new ArrayList<List<Integer>>();
		List<Integer> myHand = new ArrayList<Integer>();
		List<Integer> rest = new ArrayList<Integer>();
		for (int i = 0; i < table.size(); i++) {
			if (i == id) {
				for (Integer integer : table.get(i)) {
					myHand.add(new Integer(integer));
				}
			} else {
				for (Integer integer : table.get(i)) {

					rest.add(new Integer(integer));
				}
			}
		}

		// Collections.sort(rest);
		// Collections.sort(myHand);
		// Collections.sort(deck);
		// state.add(rest);
		// state.add(myHand);
		// state.add(deck);

		List<Integer> ace = new ArrayList<Integer>();
		List<Integer> myHandtemp = new ArrayList<Integer>();
		myHandtemp.add(sumHand(myHand));
		state.add(myHandtemp);
		state.add(rest);

		if (myHand.contains(11)) {
			ace.add(1);
		} else {
			ace.add(0);
		}
		state.add(ace);
		return state.toString();
	}

	private List<Integer> makeDeck(int nDecks) {
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
