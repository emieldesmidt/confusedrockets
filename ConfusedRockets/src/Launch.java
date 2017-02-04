import ConfusedRockets.RocketSwarm;
import ConfusedRockets.Vector2D;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Created by Emiel de Smidt on 27-1-2017.
 * Main class of the application
 */
public class Launch extends Application {

  private Vector2D targetPos = new Vector2D();


  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Creates the scene.
   *
   * @param stage the stage
   */
  @Override
  public void start(Stage stage) {
    BorderPane border = new BorderPane();
    Canvas canvas = new Canvas(1000, 600);
    border.setCenter(canvas);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // in the borderpane, create a toolbar.
    HBox hb = new HBox();
    hb.setSpacing(20);
    hb.setPadding(new Insets(10, 10, 10, 10));
    hb.setAlignment(Pos.BOTTOM_RIGHT);
    Button launchButton = new Button("Launch");
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
        lifeSpan.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });

    hb.getChildren().addAll(lifeSpan, genCount, launchButton);
    border.setBottom(hb);

    stage.setTitle("Confused Rockets");
    stage.setScene(new Scene(border));

    //add event handler to the button, and verify user input. Then launch the swarm!
    launchButton.setOnAction(e -> {
      if (!genCount.getText().isEmpty() && !lifeSpan.getText().isEmpty()) {
        int genVal = Integer.parseInt(genCount.getText());
        int span = Integer.parseInt(lifeSpan.getText());
        if (genVal > 0) {
          launch(gc, genVal, span);
        }
      }
    });

    canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      createTarget(gc, event.getSceneX(), event.getSceneY());
    });


    stage.show();

  }


  //create a target object on the desired position.
  private void createTarget(GraphicsContext gc, double x, double y) {
    gc.clearRect(targetPos.x(), targetPos.y(), 12, 12);
    gc.setFill(Color.DARKBLUE);
    gc.fillOval(x, y, 12, 12);
    targetPos = new Vector2D(x, y);
  }

  /**
   * Controls the animation
   *
   * @param gc       the canvas.
   * @param genCount the amount of generations that it should loop through.
   * @param span     lifespan of the rockets.
   */
  private void launch(GraphicsContext gc, int genCount, int span) {
    System.out.println("Launching");
    RocketSwarm swarm = new RocketSwarm(100, span, 10);


    for (int i = 0; i < genCount + 1; i++) {
      //animate each frame
      int count = 0;
      for (int j = 0; j < span; j++) {
        swarm.update(gc, count);
        count++;
      }
      //at the end of the population's lifespan, generate a new population.
      swarm.breed(0.01, targetPos);
    }
  }
}
