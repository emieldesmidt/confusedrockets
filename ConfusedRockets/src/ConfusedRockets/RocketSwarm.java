package ConfusedRockets;

import java.util.ArrayList;

/**
 * Created by Emiel de Smidt on 27-1-2017.
 * RocketSwarm contains the Rocket objects
 */
public class RocketSwarm {
  private ArrayList<Rocket> rocketStore;
  private ArrayList<Rocket> matingPool;
  private double maxFitness;


  /**
   * RocketSwarm constructor.
   *
   * @param size  the amount of rockets that is desired.
   * @param span  the lifespan of the rockets.
   * @param force the force of the rockets.
   */
  public RocketSwarm(int size, int span, int force) {
    this.rocketStore = createPopulation(size, span, force);
  }


  /**
   * Initializes a rocket population.
   * Returns an arraylist that contains rockets.
   */
  private ArrayList<Rocket> createPopulation(int size, int span, int force) {
    ArrayList r = new ArrayList<Rocket>();

    for (int i = 0; i < size; i++) {
      Vector2D vec = new Vector2D();
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
    for (Rocket i : this.rocketStore) {
      double fit = i.fitness();
      if (fit > maxFitness) {
        maxFitness = fit;
      }
    }

    //iterates over the rockets and normalizes their fitness
    for (Rocket i : this.rocketStore) {
      double fit = i.getFitness() / maxFitness;
      i.setFitness(fit);
    }

    //


  }

}
