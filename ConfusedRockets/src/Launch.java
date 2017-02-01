import ConfusedRockets.RocketSwarm;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;


/**
 * Created by Emiel de Smidt on 27-1-2017.
 * Main class of the application
 */
public class Launch extends Application {

  @FXML
  public Button Run;
  @FXML
  public Canvas cvs;


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent splash = new FXMLLoader(getClass().getResource("window.fxml")).load();

    Scene mainScene = new Scene(splash, 1200, 800);


    primaryStage.setTitle("Confused Rockets");
    primaryStage.setScene(mainScene);
    primaryStage.setMinWidth(1000);
    primaryStage.setMinHeight(700);


    primaryStage.show();

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
