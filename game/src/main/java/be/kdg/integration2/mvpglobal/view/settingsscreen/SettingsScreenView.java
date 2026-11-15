package be.kdg.integration2.mvpglobal.view.settingsscreen;

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
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SettingsScreenView extends StackPane {
    private ImageView backgroundView;
    private Label mainVolumeLabel;
    private Label bgMusicLabel;
    private Label clickVolumeLabel;
    private Label pieceVolumeLabel;
    private Label winLoseVolumeLabel;
    private Slider mainVolumeSlider;
    private Slider bgMusicSlider;
    private Slider clickVolumeSlider;
    private Slider pieceVolumeSlider;
    private Slider winLoseVolumeSlider;
    private Button submitButton;
    private Button backButton;

    public SettingsScreenView() {
        initialiseNodes();
        layoutNodes();
        setupAnimations();
    }

    private void initialiseNodes() {
        Image backgroundImage = new Image(getClass().getResource("/images/start_background.png").toExternalForm());
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(1000);
        backgroundView.setFitHeight(700);

        mainVolumeLabel = new Label("Main Volume: ");
        bgMusicLabel = new Label("Background Music: ");
        clickVolumeLabel = new Label("Click Volume: ");
        pieceVolumeLabel = new Label("Piece Volume: ");
        winLoseVolumeLabel = new Label("Win/Lose Volume: ");

        mainVolumeSlider = new Slider(0, 100, 5);
        bgMusicSlider = new Slider(0, 100, 5);
        clickVolumeSlider = new Slider(0, 100, 5);
        pieceVolumeSlider = new Slider(0, 100, 5);
        winLoseVolumeSlider = new Slider(0, 100, 5);

        submitButton = new Button("Submit");
        backButton = new Button("Back");

        mainVolumeLabel.setOpacity(0);
        bgMusicLabel.setOpacity(0);
        clickVolumeLabel.setOpacity(0);
        pieceVolumeLabel.setOpacity(0);
        winLoseVolumeLabel.setOpacity(0);
        mainVolumeSlider.setOpacity(0);
        bgMusicSlider.setOpacity(0);
        clickVolumeSlider.setOpacity(0);
        pieceVolumeSlider.setOpacity(0);
        winLoseVolumeSlider.setOpacity(0);
        submitButton.setOpacity(0);
        backButton.setOpacity(0);
    }
    //

    private void layoutNodes() {
        GridPane grid = new GridPane();

        setPadding(new Insets(0, 20, 0, 20));
        grid.setHgap(20);
        grid.setVgap(15);

        grid.add(mainVolumeLabel, 0, 0);
        grid.add(mainVolumeSlider, 1, 0);
        grid.add(bgMusicLabel, 0, 1);
        grid.add(bgMusicSlider, 1, 1);
        grid.add(clickVolumeLabel, 0, 2);
        grid.add(clickVolumeSlider, 1, 2);
        grid.add(pieceVolumeLabel, 0, 3);
        grid.add(pieceVolumeSlider, 1, 3);
        grid.add(winLoseVolumeLabel, 0, 4);
        grid.add(winLoseVolumeSlider, 1, 4);
        grid.add(submitButton, 1, 5);
        grid.add(backButton, 0, 5);

        StackPane backgroundPane = new StackPane(backgroundView);
        getChildren().addAll(backgroundPane, grid);

        grid.setAlignment(Pos.CENTER);

        getStylesheets().add(getClass().getResource("/stylesheets/settings.css").toExternalForm());
    }

    private void setupAnimations() {
        FadeTransition bgFade = new FadeTransition(Duration.seconds(1.5), backgroundView);
        bgFade.setFromValue(0.3);
        bgFade.setToValue(1);
        bgFade.play();
        animateFormElement(mainVolumeLabel, 1);
        animateFormElement(mainVolumeSlider, 2);
        animateFormElement(bgMusicLabel, 3);
        animateFormElement(bgMusicSlider, 4);
        animateFormElement(clickVolumeLabel, 5);
        animateFormElement(clickVolumeSlider, 6);
        animateFormElement(pieceVolumeLabel, 7);
        animateFormElement(pieceVolumeSlider, 8);
        animateFormElement(winLoseVolumeLabel, 9);
        animateFormElement(winLoseVolumeSlider, 10);
        animateBackButton(backButton, 11);
    }

    private void animateFormElement(javafx.scene.Node node, int sequenceOrder) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

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

    private void animateBackButton(Button button, int sequenceOrder) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), button);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

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

    public Slider getBgMusicSlider() {
        return bgMusicSlider;
    }

    public Slider getClickVolumeSlider() {
        return clickVolumeSlider;
    }

    public Slider getWinLoseVolumeSlider() {
        return winLoseVolumeSlider;
    }

    public Slider getMainVolumeSlider() {
        return mainVolumeSlider;
    }

    public Slider getPieceVolumeSlider() {
        return pieceVolumeSlider;
    }

    public Button getBackButton() {
        return backButton;
    }
}