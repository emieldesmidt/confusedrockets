package ConfusedRockets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Emiel de Smidt on 27-1-2017.
 * RocketSwarm contains the Rocket objects
 */
public class RocketSwarm {
  private List<Rocket> rocketStore;

  private static final Random rnd = new Random();


  /**
   * RocketSwarm constructor.
   *
   * @param size  the amount of rockets that is desired.
   * @param span  the lifespan of the rockets.
   * @param force the force of the rockets.
   */
  public RocketSwarm(int size, int span, int force) {
    rocketStore = createPopulation(size, span, force);
  }

  /**
   * Initialises a rocket population.
   * @param size The number of rockets to be included in the population
   * @param span The lifespan of the rockets (will be the gene sequence length)
   * @param force The magnitude of the forces which represent the rocket's DNA
   * @return A list that contains randomly initialised rockets
   */
  private List<Rocket> createPopulation(int size, int span, int force) {
    return Stream.generate(() -> new Rocket(new DNA(span, force)))
                 .limit(size)
                 .collect(Collectors.toList());
  }

  /**
   * Method that replaces the current population with a new generation.
   *
   * @param mutate weight of the mutation factor.
   */
  public void breed(double mutate, Vector2D targetPos) {
    /*
     * Replaces the mating pool with new rockets.
     * Rockets with the highest fitness levels will be present significantly more often.
     */
    ArrayList<Rocket> matingPool = new ArrayList<Rocket>();
    for (Rocket r : rocketStore) {
      double n = r.getFitness(targetPos) * 100;
      for (int i = 0; i < n; i++) {
        matingPool.add(r);
      }
    }

    /*
     * Replaces the rocketStore rockets with a new generation.
     * Two rockets from the mating pool are randomly drawn and their genes are combined
     * to create the DNA for a new rocket.
     */
    rocketStore.clear();
    for (int i = 0; i < rocketStore.size(); i++) {
      int p1 = rnd.nextInt(matingPool.size());
      int p2 = rnd.nextInt(matingPool.size());
      Rocket father = matingPool.get(p1);
      Rocket mother = matingPool.get(p2);
      rocketStore.add(father.mate(mother));
    }
  }

  //updates all the rockets
  public void update(Pane pane, int count) {
    for (Rocket rocket : this.rocketStore) {
      rocket.update(count);
      rocket.draw(pane);
    }
  }
}
