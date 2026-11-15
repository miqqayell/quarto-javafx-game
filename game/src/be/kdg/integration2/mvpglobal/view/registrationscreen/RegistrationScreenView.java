package be.kdg.integration2.mvpglobal.view.registrationscreen;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.List;

public class RegistrationScreenView extends StackPane {
    private ImageView backgroundView;
    private Button registerButton;
    private Button loginButton;
    private Button exitButton;
    private Button playGuest;
    List<Button> buttons;
    private VBox vBox;

    private final int width = 300;
    private final int height = 75;

    public RegistrationScreenView() {
        initialiseNodes();
        layoutNodes();
        setupAnimations();
    }

    private void initialiseNodes() {
        Image backgroundImage = new Image(getClass().getResource("/images/start_background.png").toExternalForm());
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(1000); // Adjust as needed
        backgroundView.setFitHeight(700); // Adjust as needed

        registerButton = new Button("Sign Up");
        loginButton = new Button("Sign In");
        playGuest = new Button("Play as Guest");
        exitButton = new Button("Exit");
        vBox = new VBox(registerButton, loginButton, playGuest, exitButton);
    }

    private void layoutNodes() {
        StackPane backgroundPane = new StackPane(backgroundView);
        getChildren().addAll(backgroundPane, vBox);

        vBox.setSpacing(50);
        vBox.setAlignment(Pos.CENTER);

        buttons = List.of(registerButton, loginButton, playGuest, exitButton);

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

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);

            // Button fade-in animation with bounce effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), button);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setInterpolator(Interpolator.EASE_OUT);

            // Delay between button animations
            PauseTransition delay = new PauseTransition(Duration.millis(150 * i));

            SequentialTransition seqTransition = new SequentialTransition(delay, fadeIn);
            seqTransition.play();

            // Enhanced hover animation that works with your CSS colors
            button.setOnMouseEntered(e -> {
                javafx.animation.ScaleTransition scaleIn = new javafx.animation.ScaleTransition(Duration.millis(150), button);
                scaleIn.setToX(1.05);
                scaleIn.setToY(1.05);
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

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getPlayGuestButton() {
        return playGuest;
    }

    public Button getExitButton() {
        return exitButton;
    }
}