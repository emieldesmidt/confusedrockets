import ConfusedRockets.RocketSwarm;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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

    // in the borderpane, create a toolbar.
    HBox hb = new HBox();
    hb.setSpacing(20);
    hb.setPadding(new Insets(10, 10, 10, 10));
    hb.setAlignment(Pos.BOTTOM_RIGHT);
    Button launch = new Button("Launch");
    TextField genCount = new TextField();
    genCount.setPromptText("Generations");
    //to ensure that the user only puts integer values.
    genCount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        genCount.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });

    TextField lifeSpan = new TextField();
    lifeSpan.setPromptText("Life Span");
    //to ensure that the user only puts integer values.
    lifeSpan.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        genCount.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });

    hb.getChildren().addAll(lifeSpan, genCount, launch);
    border.setBottom(hb);

    stage.setTitle("Drawing Operations Test");
    stage.setScene(new Scene(border));

    //add event handler to the button.
    launch.setOnAction(e -> {
      if (!genCount.getText().isEmpty() && !lifeSpan.getText().isEmpty()) {
        int genVal = Integer.parseInt(genCount.getText());
        int span = Integer.parseInt(lifeSpan.getText());
        if (genVal > 0) {
          launch(gc, genVal, span);
        }
      }
    });

    stage.show();

  }


  private void launch(GraphicsContext gc, int genCount, int span) {

    System.out.println("Launching");
    RocketSwarm swarm = new RocketSwarm(100, span, 10);


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
