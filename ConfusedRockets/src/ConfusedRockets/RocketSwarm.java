package ConfusedRockets;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Emiel de Smidt on 27-1-2017.
 * RocketSwarm contains the Rocket objects
 */
public class RocketSwarm {
  private ArrayList<Rocket> rocketStore;
  private ArrayList<Rocket> matingPool;
  private double maxFitness;
  private int size;
  private Vector2D vec = new Vector2D();


  /**
   * RocketSwarm constructor.
   *
   * @param size  the amount of rockets that is desired.
   * @param span  the lifespan of the rockets.
   * @param force the force of the rockets.
   */
  public RocketSwarm(int size, int span, int force) {
    this.rocketStore = createPopulation(size, span, force);
    this.size = size;
  }


  /**
   * Initializes a rocket population.
   * Returns an arraylist that contains rockets.
   */
  private ArrayList<Rocket> createPopulation(int size, int span, int force) {
    ArrayList r = new ArrayList<Rocket>();

    for (int i = 0; i < size; i++) {
      DNA dna = new DNA(span, force);
      r.add(new Rocket(0, vec, vec, vec, dna));
    }
    return r;
  }

  /**
   * Method that is called at the end of the population's lifetime.
   * Evaluates the individual rockets in the population.
   */
  public void evaluatePopulation() {

    //find the maximum fitness level that is present in the population
    for (Rocket r : this.rocketStore) {
      double fit = r.fitness();
      if (fit > maxFitness) {
        maxFitness = fit;
      }
    }

    //iterates over the rockets and normalizes their fitness
    for (Rocket r : this.rocketStore) {
      double fit = r.getFitness() / maxFitness;
      r.setFitness(fit);
    }

    //


  }

  /**
   * Method that replaces the current population with a new generation.
   * @param mutate weight of the mutation factor.
   */
  public void breed(int mutate) {

    /**
     * Replaces the mating pool with new rockets.
     * Rockets with the highest fitness levels will be present significantly more often.
     */
    matingPool.clear();

    for (Rocket r : this.rocketStore) {
      double n = r.getFitness() * 100;
      for (int i = 0; i < n; i++) {
        matingPool.add(r);
      }
    }

    /**
     * Replaces the rocketStore rockets with a new generation.
     * Two rockets from the mating pool are randomly drawn and their genes are combined
     * to create the DNA for a new rocket. 
     */
    for (int i = 0; i < this.rocketStore.size(); i++) {
      int rndF = new Random().nextInt(matingPool.size());
      int rndM = new Random().nextInt(matingPool.size());
      DNA father = matingPool.get(rndF).getmGenes();
      DNA mother = matingPool.get(rndM).getmGenes();
      //sex
      DNA child = null;
      try {
        child = father.crossover(mother);
      } catch (CrossoverException e) {
        System.out.println("An error occurred while breeding.");
      }
      child.mutation(mutate);
      Rocket childRocket = new Rocket(0, vec, vec, vec, child);
      this.rocketStore.add(i, childRocket);
    }
  }
}
