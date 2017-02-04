import ConfusedRockets.RocketSwarm;
import ConfusedRockets.Vector2D;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


/**
 * Created by Emiel de Smidt on 27-1-2017.
 * Main class of the application
 */
public class Launch extends Application {

  private Vector2D targetPos = new Vector2D();
  private Pane pane;
  private Circle target = new Circle();
  private Circle targetBand;

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
    pane = new Pane();
    pane.setStyle("-fx-background-color: #1BBC9B");
    pane.setPrefSize(1200, 800);
    pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> createTarget(event.getX(), event.getY()));

    border.setCenter(pane);

    border.setBottom(createHBox());

    stage.setTitle("Confused Rockets");
    stage.setScene(new Scene(border));

    stage.setResizable(false);
    stage.show();

  }


  /**
   * Returns a toolbar HBox to be used in the scene.
   *
   * @return the toolbar HBox.
   */
  private HBox createHBox() {
    HBox hb = new HBox();
    hb.setStyle("-fx-background-color: #16A086");
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

    //add event handler to the button, and verify user input. Then launch the swarm!
    launchButton.setOnAction(e -> {
      if (!genCount.getText().isEmpty() && !lifeSpan.getText().isEmpty()) {
        int genVal = Integer.parseInt(genCount.getText());
        int span = Integer.parseInt(lifeSpan.getText());
        if (genVal > 0) {
          launch(genVal, span);
        }
      }
    });

    return hb;
  }

  //create a target object on the desired position.
  private void createTarget(double x, double y) {
    pane.getChildren().removeAll(target, targetBand);
    targetPos = new Vector2D(x, y);
    target = new Circle(targetPos.x(), targetPos.y(), 6);
    target.setFill(Color.web("#0F6177"));

    targetBand = new Circle(targetPos.x(), targetPos.y(), 10);
    targetBand.setFill(Color.TRANSPARENT);
    targetBand.setStrokeWidth(2);
    targetBand.setStroke(Color.web("#0F6177"));
    pane.getChildren().addAll(target, targetBand);
  }

  /**
   * Controls the animation
   *
   * @param genCount the amount of generations that it should loop through.
   * @param span     lifespan of the rockets.
   **/
  private void launch(int genCount, int span) {
    System.out.println("Launching");
    RocketSwarm swarm = new RocketSwarm(100, span, 10);

    for (int i = 0; i < genCount + 1; i++) {
      //animate each frame
      int count = 0;
      for (int j = 0; j < span; j++) {
        swarm.update(pane, count);
        count++;
      }
      //at the end of the population's lifespan, generate a new population.
      swarm.breed(0.01, targetPos);
    }
  }

}
