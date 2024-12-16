package FightPage;

import Backend.Controller.FlashQuestController;
import QuestPage.quest;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import java.io.File;

public class BattleSceneController {
    private Text questionText;
    private Label resultLabel;
    private ProgressBar playerHealthBar;
    private ProgressBar enemyHealthBar;
    public Pane pane;
    public ImageView characterSprite;
    public ImageView enemySprite;

    private int playerHealth = 100;
    private int enemyHealth = 100;
    private final int attackDamage = 10;
    private final int enemyDamage = 10;
    public Button submitButton;
    public Button retreatButton;
    public TextField answerField;
    public Text errorMessage;
    FlashCardFolder card;
    Clip clip;

    private int correctAnswers = 0; // Track correct answers

    public void setFlashCardFolder(FlashCardFolder flashCardFolder) {
        this.card = flashCardFolder;
    }

    private String currentQuestion = "When did WW2 happen?";
    private String currentAnswer = "1939";

    public void initialize(Text questionText, Label resultLabel, ProgressBar playerHealthBar, ProgressBar enemyHealthBar, Pane pane,
                           ImageView characterSprite, ImageView enemySprite, Button submitButton, Button retreatButton,
                           TextField answerField, Text errorMessage) {
        this.questionText = questionText;
        this.resultLabel = resultLabel;
        this.playerHealthBar = playerHealthBar;
        this.enemyHealthBar = enemyHealthBar;
        this.characterSprite = characterSprite;
        this.enemySprite = enemySprite;
        this.pane = pane;
        this.submitButton = submitButton;
        this.retreatButton = retreatButton;
        this.answerField = answerField;
        this.errorMessage = errorMessage;

        // Set the initial question
        questionText.setText(currentQuestion);
        playSound();
    }

    public void handleSubmit(String userAnswer, Button submitButton, Button retreatButton, TextField answerField, Text errorMessage, Boolean music) {
        Image attack1 = new Image(getClass().getResource("AttackEffect1.gif").toExternalForm());
        ImageView fighterAttack = new ImageView(attack1);

        // Load the hit effect
        Image hitEffect1 = new Image(getClass().getResource("HitEffect.gif").toExternalForm());
        ImageView hitEffect = new ImageView(hitEffect1);

        Image hitEffect2 = new Image(getClass().getResource("HitEffect.gif").toExternalForm());
        ImageView hitEffectFighter = new ImageView(hitEffect2);

        fighterAttack.setFitWidth(150);
        fighterAttack.setFitHeight(150);
        fighterAttack.setTranslateX(160); // Match character's position
        fighterAttack.setTranslateY(150);

        // Set the size and position of the hit effect
        hitEffect.setFitWidth(200);
        hitEffect.setFitHeight(200);
        hitEffect.setTranslateX(enemySprite.getTranslateX());
        hitEffect.setTranslateY(enemySprite.getTranslateY());
        hitEffectFighter.setFitWidth(200);
        hitEffectFighter.setFitHeight(200);
        hitEffectFighter.setTranslateX(enemySprite.getTranslateX());
        hitEffectFighter.setTranslateY(enemySprite.getTranslateY());

        // 1. Handle "Next" logic first
        if (submitButton.getText().equals("Next")) {
            errorMessage.setText("");
            // Prepare the next question
            answerField.clear();
            submitButton.setText("Submit");
            currentQuestion = "What is the capital of France?";
            currentAnswer = "Paris";
            questionText.setText(currentQuestion);
            return; // Exit after handling "Next"
        }

        // 2. Handle "Submit" logic
        if (userAnswer.equalsIgnoreCase(currentAnswer)) {
            errorMessage.setText("");
            // Add the attack effect to the pane
            pane.getChildren().addAll(fighterAttack, hitEffect, hitEffectFighter);

            // Animate the attack with hit effect
            animateTackle(characterSprite, fighterAttack, 50, hitEffect, hitEffectFighter);
            answerField.clear();
            enemyHealth -= attackDamage;
            updateHealthBars();
            resultLabel.setText("Correct! You dealt damage to the enemy.");
            resultLabel.setStyle("-fx-text-fill: green;");
            submitButton.setText("Next");

            correctAnswers++; // Increment correct answers counter

            if (correctAnswers % 3 == 0) {
                triggerUniqueAbility(); // Trigger unique ability after 3 consecutive correct answers
            }

            if (enemyHealth <= 0) {
                resultLabel.setText("\tYou won the battle!");
                disableActions(submitButton);
                pane.getChildren().remove(enemySprite);
                retreatButton.setText("Exit Battle");
                music = false;

            }
        } else {
            if (!userAnswer.isEmpty()) {
                errorMessage.setText("");
                // Incorrect answer logic
                submitButton.setText("Next");
                animateTackle(enemySprite, null, -50, hitEffect, hitEffectFighter);
                playerHealth -= enemyDamage;
                updateHealthBars();

                resultLabel.setText("Incorrect! The correct answer was: " + currentAnswer);
                resultLabel.setStyle("-fx-text-fill: red;");

                if (playerHealth <= 0) {
                    resultLabel.setText("       You lost the battle.");
                    disableActions(submitButton);
                    pane.getChildren().remove(characterSprite);
                    retreatButton.setText("Exit Battle");
                    music = false;
                }
            } else {
                errorMessage.setText("Enter a answer");
            }
        }
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    private void triggerUniqueAbility() {
        resultLabel.setText("Unique Ability Activated! Massive damage dealt to the enemy.");
        resultLabel.setStyle("-fx-text-fill: blue;");
        enemyHealth -= 30; // Deal massive damage
        updateHealthBars();

        if (enemyHealth <= 0) {
            resultLabel.setText("\tYou won the battle with your unique ability!");
            disableActions(submitButton);
            pane.getChildren().remove(enemySprite);
            retreatButton.setText("Exit Battle");
        }
    }

    public void handleRetreat(Stage stage) {
        // quest Quest = new quest(stage, flashQuestController);
        // Quest.show();
        stopSound();
    }

    private void updateHealthBars() {
        playerHealthBar.setProgress(playerHealth / 100.0);
        enemyHealthBar.setProgress(enemyHealth / 100.0);
    }

    private void disableActions(Button submitButton) {
        questionText.setDisable(true);
        resultLabel.setDisable(true);
        this.submitButton.setDisable(true);
    }

    private void animateTackle(ImageView sprite, ImageView attackEffect, double moveBy, ImageView hitEffect, ImageView hitEffectFighter) {
        TranslateTransition moveForward = new TranslateTransition(Duration.seconds(0.1), sprite);
        moveForward.setByX(moveBy);

        TranslateTransition moveBackward = new TranslateTransition(Duration.seconds(0.1), sprite);
        moveBackward.setByX(-moveBy);

        if (attackEffect != null) {
            TranslateTransition effectMove = new TranslateTransition(Duration.seconds(0.18), attackEffect);
            effectMove.setByX(moveBy);

            TranslateTransition hitEffectMove = new TranslateTransition(Duration.seconds(0.18), hitEffectFighter);
            hitEffectMove.setByX(moveBy);

            TranslateTransition hitEffect1 = new TranslateTransition(Duration.seconds(0.3), hitEffect);
            hitEffectMove.setByX(moveBy);

            pane.getChildren().remove(hitEffect);

            moveForward.setOnFinished(e -> {
                effectMove.play();
                moveBackward.play();

                hitEffectFighter.setVisible(true);

                effectMove.setOnFinished(f -> {
                    pane.getChildren().remove(attackEffect);
                    hitEffectMove.play();
                });
                attackSound();
            });

            hitEffectMove.setOnFinished(f -> {
                pane.getChildren().remove(hitEffectFighter);
            });

        } else {
            moveForward.setOnFinished(e -> moveBackward.play());
            attackSound();
        }

        moveForward.play();
    }

    public void playSound() {
        try {
            // Check if clip is already playing, if so, do nothing
            if (clip != null && clip.isRunning()) {
                return;  // Music is already playing, no need to start it again
            }

            File soundFile = new File(getClass().getResource("/FightPage/battleMusic.wav").toURI());
            clip = AudioSystem.getClip();  // Use the class-level clip variable
            clip.open(AudioSystem.getAudioInputStream(soundFile));
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Infinite loop
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f);  // Adjust the volume level here
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace for more details
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }
    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();  // Stop the clip if it's currently playing
        }
    }

    public void attackSound() {
        try {
            // Create a new Clip instance for each attack to avoid reuse issues
            File soundFile = new File(getClass().getResource("/FightPage/hitSoundEffect.wav").toURI());
            Clip attackClip = AudioSystem.getClip();
            attackClip.open(AudioSystem.getAudioInputStream(soundFile));

            FloatControl volumeControl = (FloatControl) attackClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f); // Adjust the volume level as needed

            attackClip.start();

            // Close the clip after it finishes playing
            attackClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    attackClip.close();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error playing attack sound: " + e.getMessage());
        }
    }
}
