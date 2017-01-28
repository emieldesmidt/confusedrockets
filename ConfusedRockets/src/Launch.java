import ConfusedRockets.RocketSwarm;

/**
 * Created by Emiel de Smidt on 27-1-2017.
 * Main class of the application
 */
public class Launch {

  public static void main(String[] args) {
    System.out.println("Launching");
    RocketSwarm swarm = new RocketSwarm(100, 200, 10);
    swarm.breed(10);

  }
}
