package ConfusedRockets;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

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
  private int timeOfFlight;

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
    double fitness = Math.exp(-dist/100.0);

    switch (mStatus) {
      case COMPLETED:
        fitness *= completeMultiplier;
        //fitness *= 10.0/timeOfFlight;
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

  public void update(int count, List<Rectangle> obstacles, Circle target, Rectangle boundaries) {
    if (getStatus() == RocketStatus.COMPLETED || getStatus() == RocketStatus.CRASHED) {
      return;
    }

    // Save the current position in case the rocket collides or finishes
    Vector2D oldPosition = getPosition();

    this.applyForce(this.getGenes().getGene(count));
    this.mVelocity = this.mVelocity.add(this.mAcceleration);
    this.mPosition = this.mPosition.add(this.mVelocity);
    this.mAcceleration = this.mAcceleration.scale(0);

    if (this.targetReached(target)) {
      // If the rockets has reached its target, change its status and freeze it
      this.mStatus = RocketStatus.COMPLETED;
      this.mPosition = oldPosition;
      this.mVelocity = Vector2D.ZERO;
      this.mAcceleration = Vector2D.ZERO;
    } else if (this.collides(obstacles, oldPosition, mPosition) | this.outOfBounds(boundaries)) {
      // If the rocket collides with any of the obstacles or is out of bounds, change its status and freeze it
      this.mStatus = RocketStatus.CRASHED;
      this.mPosition = oldPosition;
      this.mVelocity = Vector2D.ZERO;
      this.mAcceleration = Vector2D.ZERO;
    }



    timeOfFlight++;
  }

  public Vector2D getPosition() {
    return mPosition;
  }

  public RocketStatus getStatus() {
    return mStatus;
  }

  public int getTimeOfFlight() {
    return timeOfFlight;
  }

  private static boolean collides(List<Rectangle> obstacles, Vector2D oldPosition, Vector2D newPosition) {
    for (Rectangle r: obstacles) {

      // Rocket collides if it its movement hits any of the rectangle boundaries on the way
      if (intersectRectangle(r, oldPosition, newPosition)) {
        return true;
      }
    }

    return false;
  }

  // Returns true if the line from the old position to the new position crosses any of the rectangle lines
  private static boolean intersectRectangle(Rectangle r, Vector2D oldPosition, Vector2D newPosition) {
    Vector2D leftTop = new Vector2D(r.getX(), r.getY());
    Vector2D rightTop = new Vector2D(r.getX() + r.getWidth(), r.getY());
    Vector2D leftBottom = new Vector2D(r.getX(), r.getY() + r.getHeight());
    Vector2D rightBottom = new Vector2D(r.getX() + r.getWidth(), r.getY() + r.getHeight());

    // The line intersect with rectangle if any of the boundaries of the rectangle crosses it
    return linesIntersect(leftTop, rightTop, oldPosition, newPosition) |
           linesIntersect(rightTop, rightBottom, oldPosition, newPosition) |
           linesIntersect(rightBottom, leftBottom, oldPosition, newPosition) |
           linesIntersect(leftBottom, leftTop, oldPosition, newPosition);
  }

  // (c) http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
  private static boolean linesIntersect(Vector2D a1, Vector2D a2, Vector2D b1, Vector2D b2) {
    // See http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/ for more information

    // Find the four orientations needed for general and special cases
    int o1 = orientation(a1, a2, b1);
    int o2 = orientation(a1, a2, b2);
    int o3 = orientation(b1, b2, a1);
    int o4 = orientation(b1, b2, a2);

    // General case
    if (o1 != o2 && o3 != o4) {
      return true;
    }

    // Special Cases
    // a1, a2 and b1 are colinear and b1 lies on segment a1a2
    if (o1 == 0 && onSegment(a1, b1, a2)) return true;

    // a1, a2 and b1 are colinear and b2 lies on segment a1a2
    if (o2 == 0 && onSegment(a1, b2, a2)) return true;

    // b1, b2 and a1 are colinear and a1 lies on segment b1b2
    if (o3 == 0 && onSegment(b1, a1, b2)) return true;

    // b1, b2 and a2 are colinear and a2 lies on segment b1b2
    if (o4 == 0 && onSegment(b1, a2, b2)) return true;

    return false;
  }

  // (c) http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
  // To find orientation of ordered triplet (p, q, r).
  // The function returns following values
  // 0 --> p, q and r are colinear
  // 1 --> Clockwise
  // 2 --> Counterclockwise
  private static int orientation(Vector2D p, Vector2D q, Vector2D r)
  {
    // See http://www.geeksforgeeks.org/orientation-3-ordered-points/
    // for details of below formula.
    double val = (q.y() - p.y()) * (r.x() - q.x()) -
            (q.x() - p.x()) * (r.y() - q.y());

    if (Math.abs(val) < 1E-5) return 0;  // colinear

    return (val > 1E-5)? 1: 2; // clock or counterclock wise
  }

  // (c) http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
  // Given three colinear points p, q, r, the function checks if
  // point q lies on line segment 'pr'
  private static boolean onSegment(Vector2D p, Vector2D q, Vector2D r)
  {
    if (q.x() <= Math.max(p.x(), r.x()) && q.x() >= Math.min(p.x(), r.x()) &&
            q.y() <= Math.max(p.y(), r.y()) && q.y() >= Math.min(p.y(), r.y()))
      return true;

    return false;
  }

  private boolean targetReached(Circle target) {
    // If the rocket is closer to the target center than the target radius, than it has reached its target
    if (Vector2D.distance(getPosition(), new Vector2D(target.getCenterX(), target.getCenterY())) < target.getRadius()) {
      return true;
    }
    return false;
  }

  private boolean outOfBounds(Rectangle bounds) {
    return (mPosition.x() < bounds.getX() | mPosition.x() > bounds.getX() + bounds.getWidth() |
            mPosition.y() < bounds.getY() | mPosition.y() > bounds.getY() + bounds.getHeight());
  }
}

