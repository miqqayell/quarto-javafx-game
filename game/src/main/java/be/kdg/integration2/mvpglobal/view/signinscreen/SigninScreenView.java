package be.kdg.integration2.mvpglobal.view.signinscreen;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SigninScreenView extends StackPane {
    private ImageView backgroundView;
    private Label username;
    private Label password;
    private TextField usernameInput;
    private PasswordField passwordInput;
    private Button submitButton;
    private Button backButton;

    public SigninScreenView() {
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

        username = new Label("Username: ");
        password = new Label("Password: ");
        usernameInput = new TextField();
        passwordInput = new PasswordField();
        submitButton = new Button("Submit");
        backButton = new Button("Back");

        // Set initial opacity for animations
        username.setOpacity(0);
        password.setOpacity(0);
        usernameInput.setOpacity(0);
        passwordInput.setOpacity(0);
        submitButton.setOpacity(0);
        backButton.setOpacity(0);
    }

    private void layoutNodes() {
        GridPane grid = new GridPane();

        setPadding(new Insets(0, 20, 0, 20));
        grid.setHgap(20);
        grid.setVgap(15);

        grid.setConstraints(this.username, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        grid.add(this.username, 0, 0);

        grid.setConstraints(this.usernameInput, 1, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
        grid.add(this.usernameInput, 1, 0);

        grid.setConstraints(this.password, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        grid.add(this.password, 0, 1);

        grid.setConstraints(this.passwordInput, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        grid.add(this.passwordInput, 1, 1);

        grid.setConstraints(this.submitButton, 1, 4, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        grid.add(this.submitButton, 1, 4);

        grid.setConstraints(this.backButton, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        grid.add(this.backButton, 0, 4);

        StackPane backgroundPane = new StackPane(backgroundView);
        getChildren().addAll(backgroundPane, grid);

        grid.setAlignment(Pos.CENTER);

        getStylesheets().add(getClass().getResource("/stylesheets/registration.css").toExternalForm());
    }

    private void setupAnimations() {
        // Background fade-in animation
        FadeTransition bgFade = new FadeTransition(Duration.seconds(1.5), backgroundView);
        bgFade.setFromValue(0.3);
        bgFade.setToValue(1);
        bgFade.play();

        // Create sequential animations for form elements
        animateFormElement(username, 0);
        animateFormElement(usernameInput, 1);
        animateFormElement(password, 2);
        animateFormElement(passwordInput, 3);
        animateSubmitButton(submitButton, 4);
        animateSubmitButton(backButton, 4);
    }

    private void animateFormElement(javafx.scene.Node node, int sequenceOrder) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        // Add slight vertical movement for more dynamic effect
        javafx.animation.TranslateTransition translateIn =
                new javafx.animation.TranslateTransition(Duration.seconds(0.8), node);
        translateIn.setFromY(20);
        translateIn.setToY(0);
        translateIn.setInterpolator(Interpolator.EASE_OUT);

        PauseTransition delay = new PauseTransition(Duration.millis(150 * sequenceOrder));
        SequentialTransition seqTransition = new SequentialTransition(
                delay,
                new javafx.animation.ParallelTransition(fadeIn, translateIn)
        );
        seqTransition.play();
    }

    private void animateSubmitButton(Button button, int sequenceOrder) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), button);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        // Add bounce effect for the submit button
        javafx.animation.ScaleTransition scaleIn =
                new javafx.animation.ScaleTransition(Duration.seconds(0.6), button);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        scaleIn.setInterpolator(Interpolator.EASE_OUT);

        PauseTransition delay = new PauseTransition(Duration.millis(150 * sequenceOrder));
        SequentialTransition seqTransition = new SequentialTransition(
                delay,
                new javafx.animation.ParallelTransition(fadeIn, scaleIn)
        );
        seqTransition.play();

        // Enhanced hover animation
        button.setOnMouseEntered(e -> {
            javafx.animation.ScaleTransition hoverIn =
                    new javafx.animation.ScaleTransition(Duration.millis(150), button);
            hoverIn.setToX(1.05);
            hoverIn.setToY(1.05);
            hoverIn.setInterpolator(Interpolator.EASE_OUT);
            hoverIn.play();
        });

        button.setOnMouseExited(e -> {
            javafx.animation.ScaleTransition hoverOut =
                    new javafx.animation.ScaleTransition(Duration.millis(200), button);
            hoverOut.setToX(1.0);
            hoverOut.setToY(1.0);
            hoverOut.setInterpolator(Interpolator.EASE_OUT);
            hoverOut.play();
        });
    }

    public TextField getUsernameInput() {
        return usernameInput;
    }

    public PasswordField getPasswordInput() {
        return passwordInput;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public Button getBackButton() {
        return backButton;
    }
}