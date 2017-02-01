package ConfusedRockets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Represents the rocket which will evolve until it reaches its goal. The genetic algorithm will
 * be run on these rockets where the DNA of the rockets will be used as parameters to be altered.
 */
public class Rocket {
  private static final double completeMultiplier = 10;
  private static final double crashMultiplier = 0.1;
  private Vector2D mPosition;
  private Vector2D mTargetPosition;
  private Vector2D mVelocity;
  private Vector2D mAcceleration;
  private RocketStatus mStatus = RocketStatus.FLYING;
  private DNA mGenes;

  /**
   * Rocket constructor:
   *
   * @param mPosition     the position of the rocket.
   * @param mAcceleration the acceleration of the rocket.
   * @param mVelocity     the velocity of the rocket.
   * @param mGenes        the genes of the rocket.
   */
  public Rocket(Vector2D mPosition, Vector2D mAcceleration, Vector2D mVelocity,
                DNA mGenes) {
    this.mPosition = mPosition;
    this.mAcceleration = mAcceleration;
    this.mVelocity = mVelocity;
    this.mGenes = mGenes;
  }

  public Rocket(DNA mGenes) {
    this(Vector2D.ZERO, Vector2D.ZERO, Vector2D.ZERO, mGenes);
  }

  public DNA getGenes() {
    return mGenes;
  }

  private void applyForce(Vector2D v) {
    mAcceleration = mAcceleration.add(v);
  }

  /**
   * The fitness of the rocket is calculated by combining how close it is,
   * if it reached it goal and if not whether it crashed or not. A higher
   * fitness is represented by a higher number
   *
   * @return A metric for the fitness of the rocket
   */
  public double getFitness() {
    //temporary
    Vector2D mTargetPosition = new Vector2D();

    double dist = Vector2D.distance(mPosition, mTargetPosition);

    // Turn the distance into a fitness metric
    double fitness = Math.exp(-dist);

    switch (mStatus) {
      case COMPLETED:
        fitness *= completeMultiplier;
        break;
      case CRASHED:
        fitness *= crashMultiplier;
        break;
    }

    return fitness;
  }

  public Rocket mate(Rocket partner) {
    DNA p1Genes = getGenes();
    DNA p2Genes = partner.getGenes();
    DNA childGenes = null;
    try {
      childGenes = p1Genes.crossover(p2Genes).mutation();
    } catch (CrossoverException e) {
      System.out.println(e.getMessage());
    }

    return new Rocket(childGenes);
  }

  public void update() {
    throw new NotImplementedException();
  }

  //Draw the rocket on the canvas
  public void draw(GraphicsContext gc) {
    gc.setFill(Color.BLACK);
    gc.fillRect(this.mPosition.x(), this.mPosition.y(), 5, 10);
  }
}

