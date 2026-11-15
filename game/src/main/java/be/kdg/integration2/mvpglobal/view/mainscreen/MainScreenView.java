package be.kdg.integration2.mvpglobal.view.mainscreen;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.util.Duration;
import java.util.List;

public class MainScreenView extends StackPane {
    private ImageView backgroundView;
    private Button newGameButton;
    private Button continueGameButton;
    private Button leaderboardButton;
    private Button helpButton;
    private Button settingsButton;
    private Button quitButton;
    private VBox vBox;

    private final int width = 300;
    private final int height = 75;

    public MainScreenView() {
        initialiseNodes();
        layoutNodes();
        setupAnimations();
    }

    private void initialiseNodes() {
        Image backgroundImage = new Image(getClass().getResource("/images/main_background.png").toExternalForm());
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 1.25);
        backgroundView.setFitHeight(Screen.getPrimary().getBounds().getHeight() - 200);

        newGameButton = new Button("New Game");
        continueGameButton = new Button("Continue Game");
        leaderboardButton = new Button("Leaderboard");
        helpButton = new Button("Help");
        settingsButton = new Button("Settings");
        quitButton = new Button("Quit");
        vBox = new VBox(newGameButton, continueGameButton, leaderboardButton, helpButton, settingsButton, quitButton);
    }

    private void layoutNodes() {
        StackPane backgroundPane = new StackPane(backgroundView);
        getChildren().addAll(backgroundPane, vBox);

        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);

        List<Button> buttons = List.of(newGameButton, continueGameButton, leaderboardButton, helpButton, settingsButton, quitButton);

        for (Button button : buttons) {
            button.setMinWidth(width);
            button.setMaxWidth(width);
            button.setMaxHeight(height);
            button.setMinHeight(height);
            button.setOpacity(0); // Initial state for fade-in
        }

        getStylesheets().add(getClass().getResource("/stylesheets/buttons.css").toExternalForm());
    }

    private void setupAnimations() {
        // Background fade-in animation
        FadeTransition bgFade = new FadeTransition(Duration.seconds(1.5), backgroundView);
        bgFade.setFromValue(0.3);
        bgFade.setToValue(1);
        bgFade.play();

        // Sequential button animations with delays
        List<Button> buttons = List.of(newGameButton, continueGameButton, leaderboardButton, helpButton, settingsButton,quitButton);

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);

            // Button fade-in animation with bounce effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), button);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setInterpolator(Interpolator.EASE_OUT);

            // Delay between button animations
            PauseTransition delay = new PauseTransition(Duration.millis(100 * i));

            SequentialTransition seqTransition = new SequentialTransition(delay, fadeIn);
            seqTransition.play();

            // Enhanced hover animation that works with your CSS colors
            button.setOnMouseEntered(e -> {
                javafx.animation.ScaleTransition scaleIn = new javafx.animation.ScaleTransition(Duration.millis(150), button);
                scaleIn.setToX(1.03);
                scaleIn.setToY(1.03);
                scaleIn.setInterpolator(Interpolator.EASE_OUT);

                javafx.animation.FillTransition fillIn = new javafx.animation.FillTransition(Duration.millis(150));
                fillIn.setToValue(javafx.scene.paint.Color.web("#C19A6B")); // Matches your hover color

                javafx.animation.ParallelTransition parallel = new javafx.animation.ParallelTransition(scaleIn);
                parallel.play();
            });

            button.setOnMouseExited(e -> {
                javafx.animation.ScaleTransition scaleOut = new javafx.animation.ScaleTransition(Duration.millis(200), button);
                scaleOut.setToX(1.0);
                scaleOut.setToY(1.0);
                scaleOut.setInterpolator(Interpolator.EASE_OUT);

                javafx.animation.FillTransition fillOut = new javafx.animation.FillTransition(Duration.millis(200));
                fillOut.setToValue(javafx.scene.paint.Color.web("#D2B48C")); // Matches your normal color

                javafx.animation.ParallelTransition parallel = new javafx.animation.ParallelTransition(scaleOut);
                parallel.play();
            });
        }
    }

    public ImageView getBackgroundView() {
        return backgroundView;
    }

    public Button getNewGameButton() {
        return newGameButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getContinueGameButton() {
        return continueGameButton;
    }

    public Button getLeaderboardButton() {
        return leaderboardButton;
    }

    public Button getHelpButton() {
        return helpButton;
    }

    public Button getQuitButton() {
        return quitButton;
    }
}