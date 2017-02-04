package ConfusedRockets;

import java.util.Random;

/**
 * The DNA of the rockets. They represent the route that they will follow.
 */
public class DNA implements Cloneable {
  private static final Random rnd = new Random();
  private static final double mutationThreshold = 0.01;
  private Vector2D[] mGenes;

  public DNA(Vector2D[] genes) {
    mGenes = genes.clone();
  }

  public DNA(int lifeSpan, double maxForce) {
    // Create a new set of randomised genes (2D vectors)
    mGenes = new Vector2D[10 * lifeSpan];
    for (int i = 0; i < mGenes.length; i++) {
      mGenes[i] = Vector2D.random(maxForce);
    }
  }

  public int numberOfGenes() {
    return mGenes.length;
  }

  public Vector2D getGene(int idx) {
    return mGenes[idx];
  }

  @Override
  public DNA clone() {
    return new DNA(mGenes.clone());
  }

  /**
   * A new DNA object with a new set of genes representing a child will be created.
   * The first p (a random point) genes will be inherited from this DNA instance,
   * whereas the other genes will be inherited from the partner DNA.
   *
   * @param partner The partner DNA to be crossed with
   * @return The new crossed DNA of the child
   */
  public DNA crossover(DNA partner) throws CrossoverException {
    if (numberOfGenes() != partner.numberOfGenes()) {
      throw new CrossoverException("The parents have unequal gene sequence lengths.");
    }

    Vector2D[] crossedGenes = new Vector2D[numberOfGenes()];

    // The split point where the crossover takes place
    int p = rnd.nextInt(numberOfGenes() + 1);

    // Start crossing over
    for (int i = 0; i < numberOfGenes(); i++) {
      if (i < p) {
        crossedGenes[i] = getGene(i);
      } else {
        crossedGenes[i] = partner.getGene(i);
      }
    }

    return new DNA(crossedGenes);
  }

  /**
   * For each gene in this DNA, if a certain randomly generated number meets the
   * mutation threshold, then randomise that gene.
   *
   * @return The new (possible randomised) DNA
   */
  public DNA mutation() {
    Vector2D[] newGenes = mGenes.clone();

    for (int i = 0; i < numberOfGenes(); i++) {
      if (rnd.nextDouble() < mutationThreshold) {
        double maxForce = newGenes[i].magnitude();
        newGenes[i] = Vector2D.random(maxForce);
      }
    }

    return new DNA(newGenes);
  }
}
