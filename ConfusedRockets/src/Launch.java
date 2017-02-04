import ConfusedRockets.Rocket;
import ConfusedRockets.RocketSwarm;
import ConfusedRockets.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
  private Label inf;
  private Button launchButton;
  private double targX = 600;
  private double targY = 50;
  private int count = 0;

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
    border.setCenter(createPane());
    border.setBottom(createHBox());

    stage.setTitle("Confused Rockets");
    stage.setScene(new Scene(border));
    createTarget();
    stage.setResizable(false);
    stage.show();

  }

  /**
   * Create the pane which is to be used as the canvas of the simulation.
   *
   * @return pane.
   */
  private Node createPane() {
    pane = new Pane();
    pane.setStyle("-fx-background-color: #1BBC9B");
    pane.setPrefSize(1200, 800);
    pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      targX = event.getX();
      targY = event.getY();
      createTarget();
    });

    //The generation stat label (left top)
    inf = new Label();
    inf.setStyle("-fx-text-fill: #16A086; -fx-font-size: 100");
    inf.setTranslateY(-20);
    inf.setTranslateX(20);
    pane.getChildren().add(inf);

    return pane;
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
    launchButton = new Button("Launch");
    launchButton.setDisable(true);
    TextField rocketCount = new TextField();
    rocketCount.setPromptText("Rockets");
    //to ensure that the user only puts integer values.
    rocketCount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        rocketCount.setText(newValue.replaceAll("[^\\d]", ""));
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

    hb.getChildren().addAll(lifeSpan, rocketCount, launchButton);

    //add event handler to the button, and verify user input. Then launch the swarm!
    launchButton.setOnAction(e -> {
      if (!rocketCount.getText().isEmpty() && !lifeSpan.getText().isEmpty()) {
        int genVal = Integer.parseInt(rocketCount.getText());
        int span = Integer.parseInt(lifeSpan.getText());
        if (genVal > 0) {
          launch(genVal, span);
        }
      }
    });

    return hb;
  }

  //create a target object on the desired position.
  private void createTarget() {
    launchButton.setDisable(false);
    //remove the previous target
    pane.getChildren().removeAll(target, targetBand);

    targetPos = new Vector2D(targX, targY);
    target = new Circle(targetPos.x(), targetPos.y(), 6);
    target.setFill(Color.web("#0F6177"));

    //outer ring of the target
    targetBand = new Circle(targetPos.x(), targetPos.y(), 10);
    targetBand.setFill(Color.TRANSPARENT);
    targetBand.setStrokeWidth(2);
    targetBand.setStroke(Color.web("#0F6177"));

    pane.getChildren().addAll(target, targetBand);
  }


  private void launch(int size, int span) {
    RocketSwarm swarm = new RocketSwarm(size, span, 1);
    inf.setText("0");
    AnimationTimer timer = new AnimationTimer() {
      int t = 0;
      int g = 0;

      @Override
      public void handle(long now) {

        if (t < span) {

          pane.getChildren().retainAll(inf, target);

          for (Rocket r : swarm.getRocketStore()) {
            r.update(count);
            r.draw(pane);
          }
          t++;
          count++;
        } else {
          swarm.breed(0.01, targetPos);
          inf.setText(Integer.toString(g));
          g++;
          t = 0;
        }
      }
    };
    timer.start();
  }

}
