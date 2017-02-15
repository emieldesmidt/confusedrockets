package ConfusedRockets;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Emiel de Smidt on 27-1-2017.
 * RocketSwarm contains the Rocket objects
 */
public class RocketSwarm {
  private static final Random rnd = new Random();
  private static final int matingPoolSizePerRocket = 10;
  private List<Rocket> rocketStore;


  /**
   * RocketSwarm constructor.
   *
   * @param size  the amount of rockets that is desired.
   * @param span  the lifespan of the rockets.
   * @param force the force of the rockets.
   */
  public RocketSwarm(int size, int span, double force) {
    rocketStore = createPopulation(size, span, force);
  }

  public List<Rocket> getRocketStore() {
    return rocketStore;
  }

  /**
   * Initialises a rocket population.
   *
   * @param size  The number of rockets to be included in the population
   * @param span  The lifespan of the rockets (will be the gene sequence length)
   * @param force The magnitude of the forces which represent the rocket's DNA
   * @return A list that contains randomly initialised rockets
   */
  private List<Rocket> createPopulation(int size, int span, double force) {
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

    // First store the fitness levels of each rocket in a map
    Map<Rocket, Double> fitnessLevels = new HashMap<>();
    for (Rocket r : rocketStore) {
      fitnessLevels.put(r, r.getFitness(targetPos));
    }

    // Normalise the fitness levels of the rockets. Each rockets will now have a number assigned to it which represents
    // the number of occurences of that rocket in the mating pool
    Map<Rocket, Integer> normalisedFitnessLevels = normaliseMapValues(fitnessLevels, matingPoolSizePerRocket * rocketStore.size());

    List<Rocket> matingPool = new ArrayList<>();
    for (Rocket r: normalisedFitnessLevels.keySet()) {
      for (int i = 0; i < normalisedFitnessLevels.get(r); i++) {
        matingPool.add(r);
      }
    }

    /*
     * Replaces the rocketStore rockets with a new generation.
     * Two rockets from the mating pool are randomly drawn and their genes are combined
     * to create the DNA for a new rocket.
     */
    int size = rocketStore.size();
    rocketStore.clear();
    for (int i = 0; i < size; i++) {
      int p1 = rnd.nextInt(matingPool.size());
      int p2 = rnd.nextInt(matingPool.size());
      Rocket father = matingPool.get(p1);
      Rocket mother = matingPool.get(p2);
      rocketStore.add(father.mate(mother));
    }
  }

  /**
   * Normalises the map values from double to integer. The map values will stay the same relative to each other, but
   * they will sum up the the given size of the new map.
   * @param currentMap Map containing the relative values
   * @param sizeOfNewMap Size of the normalised map
   * @return Normalised (Object, Integer) map
   * @example normaliseMapValues({Object1: 0.5, Object2: 0.1, Object3: 1.0}, 16) will give
   * {Object1: 5, Object2: 1, Object3: 10}.
   */
  private Map<Rocket, Integer> normaliseMapValues(Map<Rocket, Double> currentMap, int sizeOfNewMap) {
    Map<Rocket, Integer> normalisedMap = new HashMap<>();

    double totalValue = 0;
    for (Rocket r: currentMap.keySet()) {
      totalValue += currentMap.get(r);
    }

    // Start fill spaces in the new map (preserving relative values) until it is full
    int totalSpacesToFill = sizeOfNewMap;

    for (Rocket r: currentMap.keySet()) {
      if (totalSpacesToFill == 0) {
        break;
      }

      double relValue = currentMap.get(r);
      int spacesToFill = (int)((relValue/totalValue) * totalSpacesToFill);
      normalisedMap.put(r, spacesToFill);

      totalValue -= relValue;
      totalSpacesToFill -= spacesToFill;
    }

    assert(normalisedMap.size() == sizeOfNewMap);

    return normalisedMap;
  }

}
