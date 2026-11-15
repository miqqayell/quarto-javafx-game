package be.kdg.integration2.mvpglobal.view.resultscreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class ResultScreenView extends VBox {

    private Label winnerLabel;
    private Label totalPlayTimeLabel;
    private Label movesPlayer1Label;
    private Label movesPlayer2Label;
    private Label avgDurationPlayer1Label;
    private Label avgDurationPlayer2Label;
    private Button viewGameDetailsButton;
    private Button backToMenuButton;

    public ResultScreenView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        winnerLabel = new Label();
        totalPlayTimeLabel = new Label();
        movesPlayer1Label = new Label();
        movesPlayer2Label = new Label();
        avgDurationPlayer1Label = new Label();
        avgDurationPlayer2Label = new Label("Average duration per move for Computer: 0:01");
        viewGameDetailsButton = new Button("Game Details");
        backToMenuButton = new Button("Menu");
    }

    private void layoutNodes() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(20));

        HBox hbox = new HBox();
        hbox.setSpacing(50);
        hbox.setPadding(new Insets(30));
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(viewGameDetailsButton, backToMenuButton);

        this.getChildren().addAll(
                winnerLabel,
                totalPlayTimeLabel,
                movesPlayer1Label,
                movesPlayer2Label,
                avgDurationPlayer1Label,
                avgDurationPlayer2Label,
                hbox
        );

        Image backgroundImage = new Image(getClass().getResource("/images/start_background.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        setBackground(new Background(background));

        getStylesheets().add(getClass().getResource("/stylesheets/registration.css").toExternalForm());
        winnerLabel.setStyle("-fx-text-fill: #0b730b; -fx-font-size: 34px; -fx-font-weight: bold");
    }

    public Label getWinnerLabel() {
        return winnerLabel;
    }

    public Label getTotalPlayTimeLabel() {
        return totalPlayTimeLabel;
    }

    public Label getMovesPlayer1Label() {
        return movesPlayer1Label;
    }

    public Label getMovesPlayer2Label() {
        return movesPlayer2Label;
    }

    public Label getAvgDurationPlayer1Label() {
        return avgDurationPlayer1Label;
    }

    public Label getAvgDurationPlayer2Label() {
        return avgDurationPlayer2Label;
    }

    public Button getViewGameDetailsButton() {
        return viewGameDetailsButton;
    }

    public Button getBackToMenuButton() {
        return backToMenuButton;
    }
}
