package ConfusedRockets;

/**
 * Created by desmi on 27-1-2017.
 */
public class RocketSwarm {
  private Rocket[] rocketStore;
  private Rocket[] matingPool;
  private int popSize;
  private double maxFitness;

  /**
   * Initializes a rocket population.
   */
  public void createPopulation() {
    for (int i = 0; i < popSize; i++) {
      rocketStore[i] = new Rocket();
    }
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
    for(Rocket i : this.rocketStore){
      System.out.println("rocket");
    }


  }

}
