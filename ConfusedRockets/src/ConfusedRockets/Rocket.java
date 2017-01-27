package ConfusedRockets;

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
  private double fitness;

  /**
   * Rocket constructor:
   *
   * @param fitness       the fitness of the rocket.
   * @param mPosition     the position of the rocket.
   * @param mAcceleration the acceleration of the rocket.
   * @param mVelocity     the velocity of the rocket.
   * @param mGenes        the genes of the rocket.
   */
  public Rocket(double fitness, Vector2D mPosition, Vector2D mAcceleration, Vector2D mVelocity,
                DNA mGenes) {
    this.fitness = fitness;
    this.mPosition = mPosition;
    this.mAcceleration = mAcceleration;
    this.mVelocity = mVelocity;
    this.mGenes = mGenes;
  }

  public double getFitness() {
    return fitness;
  }

  public void setFitness(double fitness) {
    this.fitness = fitness;
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
  public double fitness() {
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

  public void update() {
    throw new NotImplementedException();
  }
}