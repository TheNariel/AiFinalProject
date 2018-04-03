package Gamer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class QGamerFinal extends Gamer {
	Map<String, List<Double>> qTable = new HashMap<String, List<Double>>();
	final List<String> actions = new ArrayList<String>(Arrays.asList("Hit", "Stand"));
	public int nDecks, id;
	String oldTable;
	
	double toTrain, toTrainMax = 500000;
	double alpha = 0.5, gamma = 0.99, epsilon = 1;

	public QGamerFinal(int nDecks, int id) {
		toTrain = toTrainMax;
		this.nDecks = nDecks;
		this.id = id;
	}

	public String getNextAction(List<List<Integer>> table) {
		Random rng = new Random();
		// initiate the state.
		String state = makeState(table);
		oldTable = state;
		// add to Q table if its not there (default value is 0)
		addToTableIfNew(state);
		// Necessary for the rules.
		if (sumHand(table.get(id)) > 21)
			return "Stand";

		// selection part.
		if (rng.nextDouble() > epsilon) {
			return actions.get(getMaxQAction(state));

		} else {
			if (rng.nextDouble() > epsilon) {
				// Guidance
				int sum = sumHand(table.get(id));
				if (sum < 17)
					return "Hit";

				return "Stand";
			} else {
				if (rng.nextBoolean())
					return "Hit";
				return "Stand";

			}
		}

	}

	public void printQTable() {
//printing out Q table sorted based on cards in agents hand.
		List<String> list = new ArrayList<>();
		Set<String> keys = qTable.keySet();
		for (String string : keys) {
			list.add(string);
		}
		Collections.sort(list);
		for (String string : list) {
			System.out.println(string + " = " + qTable.get(string));

		}

		// System.out.println(qTable.size());
		// System.out.println(qTable);
	}

	public void learn(List<List<Integer>> table, int reward, int action) {
		if (toTrain < 0) {
			epsilon = 0;
			alpha = 0;
		} else {
			List<Double> temp = qTable.get(oldTable);

			// standard learning
			double Q = temp.get(action);
			double maxNextQ = getMaxQ(makeState(table));
			double update = Q + alpha * (reward + (gamma * maxNextQ) - Q);
			temp.set(action, update);

			// epsilon decay
			epsilon = (toTrain / toTrainMax);

			// System.out.println(epsilon);
			if (reward != 0)
				toTrain--;
		}

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

		Collections.sort(rest);
		Collections.sort(myHand);
		state.add(myHand);
		state.add(rest);

		return state.toString();
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
