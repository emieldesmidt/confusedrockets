import java.util.Arrays;

/**
 * The DNA of the rockets. They represent the route that they will follow.
 */
public class DNA {
    private Vector2D[] mGenes;

    public DNA (Vector2D[] genes) {
        mGenes = genes;
    }
    public DNA (int lifeSpan) {
        // Create a new set of randomised genes
        Vector2D[] genes = new Vector2D[10*lifeSpan];
        Arrays.stream(genes).forEach(u-> u = Vector2D.random());
    }


}
