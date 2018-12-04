package Game2;

import java.util.Arrays;

/**
 * Represents a person in population.
 *
 * Remember to manually udpate fitness value before use.
 */
public class Person implements Comparable<Person> {
	public double[] weights;
	private AtomicInteger fitness = new AtomicInteger(0);

	public Person() {
		this.randomWeightVector();
        this.updateFitness();
	}

    public Person(double[] weights) {
        this.weights = weights;
    }

	private void randomWeightVector() {
		weights = new double[Constant.NUMB_FEATURES];
		for (int i = 0; i < Constant.NUMB_FEATURES; i++) {
			weights[i] = Math.abs(Utility.randomReal() * 10) * Constant.FEATURE_TYPE[i];
		}
	}

	public void updateFitness() {
        ThreadController threadMaster = ThreadController.getInstance();

        for (int i = 0;  i < Constant.NUMB_GAMES_PER_UPDATE;  i++) {
            long randomSeed = Constant.SEEDS[i];
            String threadName = this.toString() + " #" + i;

            PlayerThread game = new PlayerThread(threadName, randomSeed, weights, fitness);
            threadMaster.submitTask(game);
        }
    }

    /**
     * Uniform cross-over
     */
	public static Person crossOver(Person self, Person other) {
        double[] weights = Arrays.copyOf(self.weights, self.weights.length);
        for (int i = 0;  i < weights.length;  i++) {
            if (Utility.flipCoin()) {
                weights[i] = other.weights[i];
            }
        }
        Person child = new Person(weights);
        child.updateFitness();
        return child;
	}

    /**
     * Mutate a given person with delta in range [-2, 2]
     */
	public static Person mutate(Person self, int mutateLocation) {
        double[] weights = Arrays.copyOf(self.weights, self.weights.length);
        weights[mutateLocation] += Utility.randomReal() * 2;
        Person child = new Person(weights);
        child.updateFitness();
        return child;
	}

	public int compareTo(Person other) {
		return other.fitness.getValue() - this.fitness.getValue();
	}

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double[] getWeights() {
        return weights;
    }

    public AtomicInteger getFitness() {
        return this.fitness;
    }

    public Person clone() {
        return new Person(Arrays.copyOf(weights, weights.length));
    }

    public String toString() {
        String text = "";
        for (double weight : weights) {
            text += "|" + weight;
        }
        return text;
    }

}
