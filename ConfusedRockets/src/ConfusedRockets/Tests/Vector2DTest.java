package ConfusedRockets.Tests;

import org.junit.jupiter.api.Test;
import ConfusedRockets.*;

public class Vector2DTest {
    private static final double eps = 1E-6;

    @Test
    public void x() throws Exception {
        Vector2D v = new Vector2D(1.0, 1.0);
        assert equal(v.x(), 1.0);
    }

    @Test
    public void y() throws Exception {
        Vector2D v = new Vector2D(1.0, 1.0);
        assert equal(v.y(), 1.0);
    }

    @Test
    public void magnitude() throws Exception {
        Vector2D v = new Vector2D(-1.0, 1.0);
        assert equal(v.magnitude(), Math.sqrt(2.0));
    }

    @Test
    public void angle() throws Exception {
        Vector2D v = new Vector2D(-0.5, -0.5*Math.sqrt(3.0));
        assert equal(v.angle(), -2.0*Math.PI/3);
    }

    @Test
    public void fromPolar() throws Exception {
        Vector2D v = Vector2D.fromPolar(2.0, -0.25*Math.PI);
        assert equal(v, new Vector2D(Math.sqrt(2.0), -Math.sqrt(2.0)));
    }

    @Test
    public void add() throws Exception {
        Vector2D a = new Vector2D(1.0, -1.0);
        Vector2D b = new Vector2D(-5.5, 2.0);
        assert equal(a.add(b), new Vector2D(-4.5, 1.0));
    }

    @Test
    public void subtract() throws Exception {
        Vector2D a = new Vector2D(1.0, -1.0);
        Vector2D b = new Vector2D(-5.5, 2.0);
        assert equal(a.subtract(b), new Vector2D(6.5, -3.0));
    }

    @Test
    public void scale() throws Exception {
        Vector2D v = new Vector2D(3.0, 4.2);
        assert equal(v.scale(-1.5), new Vector2D(-4.5, -6.3));
    }

    @Test
    public void rotate() throws Exception {
        Vector2D v = new Vector2D(1.0, 1.0);
        assert equal(v.rotate(-5*Math.PI/12), new Vector2D(0.5*Math.sqrt(6.0), -0.5*Math.sqrt(2.0)));
    }

    @Test
    public void dot() throws Exception {
        Vector2D a = new Vector2D(1.0, -2.0);
        Vector2D b = new Vector2D(-3.0, 2.5);
        assert equal(a.dot(b), -8.0);
    }

    @Test
    public void normalised() throws Exception {
        Vector2D v = new Vector2D(1.0, 2.0);
        assert equal(v.normalised(), new Vector2D(1.0/Math.sqrt(5.0), 2.0/Math.sqrt(5.0)));
    }

    @Test
    public void distance() throws Exception {
        Vector2D a = new Vector2D(2.0, -1.0);
        Vector2D b = new Vector2D(3.0, 1.5);
        assert equal(Vector2D.distance(a,b), Math.sqrt(7.25));
    }

    private boolean equal(double a, double b) {
        return (Math.abs(a-b) < eps);
    }
    private boolean equal(Vector2D a, Vector2D b) {
        return (Math.abs(a.x()-b.x()) < eps & Math.abs(a.y() - b.y()) < eps);
    }
}