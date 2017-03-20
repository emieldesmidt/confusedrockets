import ConfusedRockets.Rocket;
import ConfusedRockets.RocketStatus;
import ConfusedRockets.RocketSwarm;
import ConfusedRockets.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolatable;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


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
  private Label score;
  private Button launchButton;
  private double targX = 600;
  private double targY = 50;
  private double targRadius = 6;
  private double obsStartX;
  private double obsStartY;
  private double obsEndX;
  private Rectangle boundaries = new Rectangle(1200, 800);
  private ArrayList<Rectangle> obstacleStore = new ArrayList<>();

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
    pane.setStyle("-fx-background-color: #282830");
    pane.setPrefSize(boundaries.getWidth(), boundaries.getHeight());
    pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getButton() == MouseButton.SECONDARY) {
        targX = event.getX();
        targY = event.getY();
        createTarget();
      }
    });

    pane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
      obsStartX = event.getX();
      obsStartY = event.getY();
    });

    pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
      obsEndX = event.getX();
      createObs();
    });

    //The generation stat label (left top)
    inf = new Label();
    inf.setStyle("-fx-text-fill: #25373D; -fx-font-size: 100");
    inf.setTranslateY(-20);
    inf.setTranslateX(20);
    pane.getChildren().add(inf);

    //The score stat label (left top)
    score = new Label();
    score.setStyle("-fx-text-fill: #25373D; -fx-font-size: 35");
    score.setTranslateY(80);
    score.setTranslateX(20);
    pane.getChildren().add(score);

    return pane;
  }

  private void createObs() {
    Rectangle obs = new Rectangle(obsStartX, obsStartY, obsEndX - obsStartX, 12);
    obs.setFill(Color.web("#0F6177"));
    obstacleStore.add(obs);
    pane.getChildren().retainAll(inf, score, target, targetBand);
    pane.getChildren().addAll(obstacleStore);
  }


  /**
   * Returns a toolbar HBox to be used in the scene.
   *
   * @return the toolbar HBox.
   */
  private HBox createHBox() {
    HBox hb = new HBox();
    hb.setStyle("-fx-background-color: #3E4651");
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
    target = new Circle(targetPos.x(), targetPos.y(), targRadius);
    target.setFill(Color.web("#0F6177"));

    //outer ring of the target
    targetBand = new Circle(targetPos.x(), targetPos.y(), 2*targRadius);
    targetBand.setFill(Color.TRANSPARENT);
    targetBand.setStrokeWidth(2);
    targetBand.setStroke(Color.web("#0F6177"));

    pane.getChildren().addAll(target, targetBand);
  }


  private void launch(int size, int span) {
    RocketSwarm swarm = new RocketSwarm(size, span, 1);
    inf.setText("0");
    score.setText("0/0");

    AnimationTimer timer = new AnimationTimer() {
      int t = 0;
      int g = 0;

      @Override
      public void handle(long now) {

        if (t < span) {

          pane.getChildren().retainAll(inf, score, target, targetBand);
          pane.getChildren().addAll(obstacleStore);

          int finishedRockets = 0;
          for (Rocket r : swarm.getRocketStore()) {
            r.update(t, obstacleStore, new Circle(targX,  targY, targRadius), boundaries);
            drawRocket(r);
            if (r.getStatus() == RocketStatus.COMPLETED) {
              finishedRockets++;
            }
          }
          t++;
          score.setText(Integer.toString(finishedRockets) + "/" + Integer.toString(swarm.getRocketStore().size()));

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

  //Draw the rocket on the canvas
  public void drawRocket(Rocket r) {
    Ellipse rocket = new Ellipse(r.getPosition().x(), r.getPosition().y(), 2, 2);

    switch (r.getStatus()) {
      case COMPLETED:
        rocket.setFill(Color.web("#97CE68"));
        break;
      case CRASHED:
        rocket.setFill(Color.web("#E3000E"));
        break;
      default:
        rocket.setFill(Color.web("#1DABB8"));
    }
    pane.getChildren().add(rocket);
  }
}
