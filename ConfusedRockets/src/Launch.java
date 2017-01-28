import ConfusedRockets.RocketSwarm;

/**
 * Created by Emiel de Smidt on 27-1-2017.
 * Main class of the application
 */
public class Launch {

  public static void main(String[] args) {
    System.out.println("Launching");
    RocketSwarm swarm = new RocketSwarm(100, 200, 10);
    int genCount = 300;

    for (int i = 0; i < genCount + 1; i++) {
      //animate each frame
      for (int j = 0; j < swarm.getSpan(); j++) {
        //swarm.update();
        System.out.print("Generation:  " + i + "  ( " + 100 * i / genCount + "% )");
        System.out.write('\r');

      }
      //at the end of the population's lifespan, generate a new population.
      swarm.breed(0.01);
    }
  }
}
