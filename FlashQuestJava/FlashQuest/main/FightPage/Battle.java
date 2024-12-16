package FightPage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import java.io.File;


public class Battle extends Application {
    private Stage stage;
    private BattleSceneController controller = new BattleSceneController();
    private Clip clip;
    boolean music = true;

    public void start(Stage stage) {
        this.stage = stage;
        // Load custom font
        Font vcrFont = Font.loadFont(getClass().getResource("VCR-OSD-MONO.ttf").toExternalForm(), 130);

        // Load the background image
        Image image = new Image(getClass().getResource("fightScene.gif").toExternalForm());
        ImageView imageView = new ImageView(image);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.35);
        imageView.setEffect(colorAdjust);
        imageView.setFitWidth(1280);
        imageView.setFitHeight(620);
        imageView.setTranslateY(-300);

        // Fighter and enemy images
        Image fighter = new Image(getClass().getResource("fighter.gif").toExternalForm());
        Image mage = new Image(getClass().getResource("Mage.gif").toExternalForm());
        Image tank = new Image(getClass().getResource("Tank2.gif").toExternalForm());
        Image enemy = new Image(getClass().getResource("enemy.gif").toExternalForm());

        ImageView setFighter = new ImageView(fighter);
        setFighter.setFitWidth(150);
        setFighter.setFitHeight(150);
        setFighter.setTranslateX(120);
        setFighter.setTranslateY(140);

        ImageView enemyView = new ImageView(enemy);
        enemyView.setFitWidth(150);
        enemyView.setFitHeight(150);
        enemyView.setTranslateX(950);
        enemyView.setTranslateY(140);

        // Health and attack bars
        ProgressBar healthBar = new ProgressBar(1);
        healthBar.getStyleClass().add("health-bar");
        healthBar.setTranslateX(100);
        healthBar.setTranslateY(50);

        ProgressBar attackBar = new ProgressBar(1);
        attackBar.getStyleClass().add("attack-bar");
        attackBar.setTranslateX(100);
        attackBar.setTranslateY(80);

        ProgressBar enemyHealthBar = new ProgressBar(1);
        enemyHealthBar.getStyleClass().add("health-bar");
        enemyHealthBar.setTranslateX(900);
        enemyHealthBar.setTranslateY(50);

        ProgressBar enemyAttackBar = new ProgressBar(1);
        enemyAttackBar.getStyleClass().add("attack-bar");
        enemyAttackBar.setTranslateX(900);
        enemyAttackBar.setTranslateY(80);

        // Buttons
        Button retreat = new Button(" Retreat ");
        Button submit = new Button(" Submit ");
        submit.setPrefWidth(200);
        submit.setPrefHeight(50);
        retreat.setPrefWidth(200);
        retreat.setPrefHeight(50);
        retreat.setTranslateX(210);
        retreat.setTranslateY(560);
        submit.setTranslateX(450);
        submit.setTranslateY(560);
        retreat.getStyleClass().add("retreat-button");
        submit.getStyleClass().add("submit-button");

        Text errorMessage = new Text("");
        errorMessage.setTranslateX(210);
        errorMessage.setTranslateY(550);
        errorMessage.getStyleClass().add("description");
        errorMessage.setStyle("-fx-fill: red;");

        // TextField for answers
        TextField answers = new TextField();
        answers.setTranslateX(210);
        answers.setTranslateY(490);
        answers.setPrefHeight(40);
        answers.setPrefWidth(440);
        answers.setPromptText("Enter your question ");
        answers.getStyleClass().add("placeholder");

        answers.clear();
        // Questions
        Text questions = new Text("When did the WW2 Happen?");
        questions.getStyleClass().add("question");

        HBox layoutAnswers = new HBox();
        layoutAnswers.setLayoutX(0);
        layoutAnswers.setLayoutY(300);
        layoutAnswers.setPrefWidth(1280);
        layoutAnswers.setPrefHeight(600);

        layoutAnswers.setStyle("-fx-background-color: #364444;");
        VBox layoutQuestions = new VBox(5, questions);
        layoutQuestions.setLayoutX(210);
        layoutQuestions.setLayoutY(330);
        layoutQuestions.setMaxWidth(880);
        layoutQuestions.setPrefHeight(140);
        layoutQuestions.setAlignment(Pos.CENTER);
        questions.setTextAlignment(TextAlignment.CENTER);
        layoutQuestions.setStyle("-fx-background-color: #363234;");
        questions.setWrappingWidth(860);

        // Correct/incorrect feedback
        VBox layoutCorrects = new VBox(5);
        layoutCorrects.setLayoutX(680);
        layoutCorrects.setLayoutY(490);
        layoutCorrects.setPrefWidth(390);
        layoutCorrects.setPrefHeight(110);
        layoutCorrects.setAlignment(Pos.CENTER);
        layoutCorrects.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 5px; ");
        layoutAnswers.toBack();

        Label correction = new Label("   Answer to defeat the enemy!");
        correction.getStyleClass().add("question");
        layoutCorrects.getChildren().add(correction);
        correction.setWrapText(true);
        correction.setMaxWidth(800);
        correction.setTextAlignment(TextAlignment.CENTER);
        // Root pane
        Pane root = new Pane();

        root.getChildren().addAll(
                imageView, layoutAnswers, layoutQuestions, answers, retreat, submit,
                healthBar, attackBar, enemyHealthBar, enemyAttackBar, setFighter,
                enemyView, layoutCorrects, errorMessage
        );
        Scene scene = new Scene(root, 1280, 620);
        String css = this.getClass().getResource("fightScene.css").toExternalForm();
        scene.getStylesheets().add(css);
        // Set up the stage
        stage.setTitle("FlashQuest");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Initialize the controller and pass the root pane
        controller.initialize(questions, correction, healthBar, enemyHealthBar, root, setFighter, enemyView, submit, retreat, answers, errorMessage);

        // Button actions// Button actions
        submit.setOnAction(event -> controller.handleSubmit(answers.getText(), submit, retreat, answers, errorMessage, music));
        retreat.setOnAction(event -> controller.handleRetreat(stage));

    }

}