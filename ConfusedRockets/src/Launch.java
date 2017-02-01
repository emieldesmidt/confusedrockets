import ConfusedRockets.RocketSwarm;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;


/**
 * Created by Emiel de Smidt on 27-1-2017.
 * Main class of the application
 */
public class Launch extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Confused Rockets");
    Group root = new Group();
    Canvas canvas = new Canvas(1000, 750);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    launch(gc);
  }



  public void launch(GraphicsContext gc){
    int span = 100;

    System.out.println("Launching");
    RocketSwarm swarm = new RocketSwarm(100, span, 10);
    int genCount = 300;

    for (int i = 0; i < genCount + 1; i++) {
      //animate each frame
      for (int j = 0; j < span; j++) {
        //swarm.update();
        System.out.print("Generation:  " + i + "  ( " + 100 * i / genCount + "% )");
        System.out.write('\r');


      }
      //at the end of the population's lifespan, generate a new population.
      swarm.breed(0.01);
    }
  }
}
