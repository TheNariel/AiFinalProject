package Gamer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class QGamer extends Gamer {
	public int nDecks, id, toTrain = 2000, toTrainMax = 2000;
	List<Integer> deck;
	Map<String, Double> qTable = new HashMap<String, Double>();
	final List<String> actions = new ArrayList<String>(Arrays.asList("Hit", "Stand"));
	List<List<Integer>> oldTable;

	double alpha = 0.8, gamma = 0.9, epsilon = 1;

	public QGamer(int nDecks, int id) {
		this.nDecks = nDecks;
		this.id = id;
	}

	public void newDeck() {
		this.deck = makeDeck(nDecks);
	}

	public String getNextAction(List<List<Integer>> table) {
		List<List<Integer>> state = makeState(table);
		Random rng = new Random();

		// if (toTrain <= 0)
		addToTableIfNew(state);

		oldTable = new ArrayList<>();
		for (int i = 0; i < table.size(); i++) {
			oldTable.add(new ArrayList<>());
			for (Integer integer : table.get(i)) {
				oldTable.get(i).add(integer);
			}
		}

		if (sumHand(table.get(id)) > 21)
			return "Stand";

		if (rng.nextDouble() > epsilon) {
			return actions.get(getMaxQAction(state));

		} else {
			if (rng.nextBoolean())
				return "Hit";
			return "Stand";
		}
		/*
		 * int sum = sumHand(table.get(id)); if (sum < 17) return "Hit";
		 * 
		 * return "Stand";
		 */
	}

	public void printQTable() {

		List<String> list = new ArrayList<>();
		Set<String> keys = qTable.keySet();
		for (String string : keys) {
			list.add(string);
		}
		Collections.sort(list);
		int i = 0;
		for (String string : list) {
			System.out.print(string + " = " + qTable.get(string) + ";");
			i++;
			if (i % 2 == 0)
				System.out.println();
		}
		// System.out.println(qTable.size());
		// System.out.println(qTable);
	}

	public void handOver(List<List<Integer>> table) {
		// ToDo some kind of statistics maybe.
		for (List<Integer> list : table) {
			for (Integer integer : list) {
				deck.remove(integer);
			}
		}

	}

	public void learn(List<List<Integer>> table, int reward, int action) {
		List<List<Integer>> state = makeState(oldTable);
		state.get(state.size() - 1).add(action);

		if (toTrain > (0.7 * toTrainMax)) {
			epsilon -= (0.1) / (0.3 * toTrainMax);
		} else {
			if (toTrain > (0.3 * toTrainMax)) {

				epsilon -= (0.8) / (0.4 * toTrainMax);
			} else {
				if (toTrain > 0) {

					epsilon -= (0.1) / (0.3 * toTrainMax);
				} else {
					epsilon = 0;
					alpha = 0;
				}
			}
		}

		/*
		 * if (toTrain <= 0) { epsilon = 0; alpha = 0; }
		 */
		if (reward != 0)
			toTrain--;
		double Q = qTable.get(state.toString());
		double maxNextQ = getMaxQ(makeState(table));
		double update = Q + alpha * (reward + gamma * maxNextQ - Q);

		qTable.put(state.toString(), update);

	}

	private void addToTableIfNew(List<List<Integer>> state) {
		for (int i = 0; i < actions.size(); i++) {
			state.get(state.size() - 1).add(i);
			if (!qTable.containsKey(state.toString()))
				qTable.put(state.toString(), 0.0);
			state.get(state.size() - 1).remove(0);
		}

	}

	private Double getMaxQ(List<List<Integer>> state) {
		// addToTableIfNew(state);
		List<Double> q = new ArrayList<>();
		for (int i = 0; i < actions.size(); i++) {
			state.get(state.size() - 1).add(i);
			q.add(qTable.get(state.toString()));
			state.get(state.size() - 1).remove(0);
		}
		double ret;
		try {
			ret = Collections.max(q);
		} catch (Exception e) {
			ret = 0.0;
		}
		return ret;
		// return Collections.max(q);
	}

	private int getMaxQAction(List<List<Integer>> state) {
		addToTableIfNew(state);
		List<Double> q = new ArrayList<>();
		for (int i = 0; i < actions.size(); i++) {
			state.get(state.size() - 1).add(i);
			q.add(qTable.get(state.toString()));
			state.get(state.size() - 1).remove(0);
		}
		Random rng = new Random();
		double max = Collections.max(q);
		if (q.get(0) == max && q.get(1) == max)
			return rng.nextInt(2);

		return q.indexOf(max);

	}

	private List<List<Integer>> makeState(List<List<Integer>> table) {
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

		/*
		 * Collections.sort(rest); Collections.sort(myHand); Collections.sort(deck);
		 * state.add(rest); state.add(myHand); state.add(new ArrayList<Integer>());
		 */
		// state.add(deck);

		List<Integer> myHandtemp = new ArrayList<Integer>();
		List<Integer> resttemp = new ArrayList<Integer>();
		myHandtemp.add(sumHand(myHand));
		resttemp.add(sumHand(rest));
		state.add(myHandtemp);
		// state.add(resttemp);
		state.add(new ArrayList<Integer>());

		// state.add(new ArrayList<Integer>());
		return state;
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
