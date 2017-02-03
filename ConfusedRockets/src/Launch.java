import ConfusedRockets.RocketSwarm;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.io.IOException;


/**
 * Created by Emiel de Smidt on 27-1-2017.
 * Main class of the application
 */
public class Launch extends Application {


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws IOException {
    BorderPane border = new BorderPane();
    Canvas canvas = new Canvas(1000, 600);
    border.setCenter(canvas);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    HBox hb = new HBox();
    Button launch = new Button("Launch");
    TextField genCount = new TextField("generations");
    hb.getChildren().addAll(genCount, launch);

    border.setBottom(hb);

    stage.setTitle("Drawing Operations Test");
    stage.setScene(new Scene(border));
    stage.show();

    launch(gc);

  }


  private void launch(GraphicsContext gc) {
    int span = 100;

    System.out.println("Launching");
    RocketSwarm swarm = new RocketSwarm(100, span, 10);
    int genCount = 300;

    for (int i = 0; i < genCount + 1; i++) {
      //animate each frame
      for (int j = 0; j < span; j++) {
        swarm.update(gc);

      }
      //at the end of the population's lifespan, generate a new population.
      swarm.breed(0.01);
    }
  }
}
