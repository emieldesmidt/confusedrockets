import java.util.Random;

public class Vector2D {
    private static Random rnd = new Random();

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

    // Cartesian coordinates
    public double x() {
        return mX;
    }
    public double y() {
        return mY;
    }

    // Polar coordinates
    public double magnitude() {
        return Math.sqrt(Math.pow(x(), 2.0) + Math.pow(y(), 2.0));
    }
    public double angle() {
        return Math.atan2(y(), x());
    }
    public static Vector2D fromPolar(double magnitude, double angle) {
        return new Vector2D(Math.cos(angle), Math.sin(angle)).scale(magnitude);
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

    public Vector2D rotate(double angle) {
        return fromPolar(magnitude(), angle() + angle);
    }

    public double dot(Vector2D v) {
        return x() * v.x() + y() * v.y();
    }

    public Vector2D normalised() {
        return new Vector2D(x(), y()).scale(1.0/magnitude());
    }

    // Returns a new 2D vector pointing in a random direction
    public static Vector2D random(double magnitude) {
        double angle = rnd.nextDouble()*2*Math.PI;
        Vector2D rndVector = new Vector2D(Math.cos(angle), Math.sin(angle));
        return rndVector.scale(magnitude);
    }
    public static Vector2D random() {
        return random(1.0);
    }
}
