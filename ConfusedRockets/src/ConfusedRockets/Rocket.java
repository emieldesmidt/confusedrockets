package ConfusedRockets;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
    this(new Vector2D(600, 800), Vector2D.ZERO, Vector2D.ZERO, mGenes);
  }

  private DNA getGenes() {
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
  double getFitness(Vector2D targetPos) {


    double dist = Vector2D.distance(mPosition, targetPos);

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

  Rocket mate(Rocket partner) {
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

  public void update(int count) {
    this.applyForce(this.getGenes().getGene(count));
    this.mVelocity = this.mVelocity.add(this.mAcceleration);
    this.mPosition = this.mPosition.add(this.mVelocity);
    this.mAcceleration = this.mAcceleration.scale(0);
  }

  //Draw the rocket on the canvas
  public void draw(Pane pane) {
    Rectangle rocket = new Rectangle(this.mPosition.x(), this.mPosition.y(), 3, 12);
    rocket.setFill(Color.web("#0F6177"));
    pane.getChildren().add(rocket);
  }
}

