package ConfusedRockets.Tests;

import ConfusedRockets.CrossoverException;
import ConfusedRockets.DNA;
import ConfusedRockets.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DNATest {
    private static final double EPS = 1E-10;

    @Test
    void numberOfGenes() {
        DNA test = new DNA(10, 1.0);
        assert test.numberOfGenes() == 100;


        Vector2D[] genes = new Vector2D[5];
        test = new DNA(genes);
        assert test.numberOfGenes() == 5;
    }

    @Test
    void getGene() {
        Vector2D[] genes = new Vector2D[10];
        genes[6] = new Vector2D(1.0, 2.0);
        DNA test = new DNA(genes);

        assert equal(test.getGene(6), new Vector2D(1.0, 2.0));
    }

    @Test
    void crossover() {
        try {
            // Create a child (rocket sex)
            DNA dad = new DNA(10, 1.0);
            DNA mom = new DNA(10, 1.0);
            DNA child = dad.crossover(mom);

            // Go through the genes of the child and essentially check if there is only one change from dad genes to mom
            // genes. This should be the case if the crossover is working properly.
            if (child.numberOfGenes() == 0) {
                return;
            }
            boolean dadPartOfGenes = true;
            // Do a check to see whether it is the case that there are no dad genes in the child his genes
            if (equal(child.getGene(0), mom.getGene(0))) {
                dadPartOfGenes = false;
            }
            for (int i = 0; i < child.numberOfGenes(); i++) {
                if (dadPartOfGenes) {
                    if (!equal(child.getGene(i), dad.getGene(i))) {
                        if (equal(child.getGene(i), mom.getGene(i))) {
                            dadPartOfGenes = false;
                        } else {
                            fail("The child has DNA that originates from neither parents");
                        }
                    }
                } else {
                    // The dad part of the child his DNA has ended. From now on, it should only be genes originates
                    // from the mom.
                    assert equal(child.getGene(i), mom.getGene(i));
                }
            }
        } catch (CrossoverException exc) {
            fail("The parents have the same length of DNA, but the exception indicates this is not the case");
        }
    }

    @Test
    void mutation() {
        DNA original = new DNA(100, 1.0);
        DNA mutant = original.mutation();

        assertEquals(original.numberOfGenes(), mutant.numberOfGenes());

        double mutationThreshold = DNA.getMutationThreshold();

        // As mutations are random, there can be some variation in the number of mutated genes. Sigma denotes the
        // fraction of mutations that are allowed compared to the expected number of mutations.
        double sigma = 0.5;

        int numOfExpMutations = (int)(mutant.numberOfGenes() * mutationThreshold);

        int mutations = 0;
        for (int i = 0; i < original.numberOfGenes(); i++) {
            if (!equal(original.getGene(i), mutant.getGene(i))) {
                mutations++;
            }
        }

        if (mutations > (1.0 + sigma) * numOfExpMutations | mutations < (1.0 - sigma) * numOfExpMutations) {
            fail("The have been too many mutations compared to what we would expect on average. " +
                    "THIS FAIL COULD BE DUE TO A VERY UNLIKELY EVENT, RERUN THE TEST SEVERAL TIMES TO CHECK.");
        }
    }

    private boolean equal(double a, double b) {
        return (Math.abs(a-b) < EPS);
    }
    private boolean equal(Vector2D a, Vector2D b) {
        return (Math.abs(a.x()-b.x()) < EPS & Math.abs(a.y() - b.y()) < EPS);
    }
}