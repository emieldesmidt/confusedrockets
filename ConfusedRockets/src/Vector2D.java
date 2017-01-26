import java.util.*;

public class Vector2D {
    private static int seed = 0;

    private double mX;
    private double mY;

    public Vector2D(double x, double y) {
        mX = x;
        mY = y;
    }
    public Vector2D(Vector2D v) {
        mX = v.x();
        mY = v.y();
    }
    public Vector2D() {
        mX = 0;
        mY = 0;
    }

    public double x() {
        return mX;
    }
    public double y() {
        return mY;
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(x() + v.x(), y() + v.y());
    }

    public Vector2D subtract(Vector2D v) {
        return add(v.scale(-1));
    }

    public Vector2D scale(double magnitude) {
        return new Vector2D(x() * magnitude, y() * magnitude);
    }

    // Returns a new 2D unity vector pointing in a random direction
    public static Vector2D random() {
        Random rnd = new Random();

        double angle = rnd.nextDouble()*2*Math.PI;
        return new Vector2D(Math.cos(angle), Math.sin(angle));
    }
}
