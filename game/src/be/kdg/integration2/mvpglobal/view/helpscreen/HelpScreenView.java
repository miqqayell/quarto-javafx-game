package be.kdg.integration2.mvpglobal.view.helpscreen;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class HelpScreenView extends BorderPane {

    private VBox contentBox;
    private ScrollPane scrollPane;
    private Button okButton;

    public HelpScreenView() {
        initialiseNodes();
        layoutNodes();
        applyStyles();
    }

    private void initialiseNodes() {
        contentBox = new VBox(10);
        scrollPane = new ScrollPane(contentBox);
        okButton = new Button("OK");
        okButton.setPrefWidth(80);
    }

    private void layoutNodes() {
        addSection("Components", "A board with 16 squares.\n\n" +
                "16 different pieces each with 4 characteristics: light or dark, round or square, tall or short, solid or hollow.");
        addSection("Setup", "At the start of the game, the pieces are arranged at the side of the board.");
        addSection("Object of the Game", "To establish a line of four pieces, with at least one common characteristic on the board.\n\n" +
                "The line of pieces may be across the board, up and down, or along a diagonal.");
        addSection("Game Play", "The players throw dice to see who starts. The first player selects one of the 16 pieces and gives it to his opponent.\n\n" +
                "That player places the piece on any square on the board; he must then choose one of the 15 pieces remaining and give it to his opponent. In his turn, that player places the piece on an empty square, and so on...");
        addSection("Winning the game", "The game is won by the first player to call \"QUARTO!\" " +
                "A player calls \"QUARTO!\" and wins the game when, as he places the piece given to him, he creates a line of 4 light pieces, 4 dark pieces, 4 round pieces, 4 square pieces, 4 tall pieces, 4 short pieces, 4 solid pieces, or 4 hollow pieces.\n\n" +
                "Different characteristics can accumulate. He does not need to have placed the 3 other pieces himself. " +
                "He must declare his win by calling \"QUARTO!\" If this player has not noticed the alignment and passes a piece to the opponent, the latter may \"at that moment\" call \"QUARTO!\" and indicate the alignment: He wins the game.");
        addSection("End of the Game", "Game winner: A player who calls and points out a \"QUARTO!\"\n\n" +
                "Game even: All the pieces have been placed and no winner declared.");

        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        setCenter(scrollPane);
        setPadding(new Insets(15));
        BorderPane.setAlignment(okButton, Pos.CENTER);
        BorderPane.setMargin(okButton, new Insets(15, 0, 0, 0));
        setBottom(okButton);
        animateBackButton(okButton, 1);
        okButton.setId("backButton");
        getStylesheets().add(getClass().getResource("/stylesheets/board.css").toExternalForm());
    }

    private void addSection(String title, String content) {
        Label sectionTitle = new Label(title);
        sectionTitle.setFont(Font.font("Comic Sans MS", 20));
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #5a331d;");

        TextArea sectionContent = new TextArea(content);
        sectionContent.setWrapText(true);
        sectionContent.setEditable(false);
        sectionContent.setPrefHeight(100);

        sectionContent.setStyle("-fx-background-color: #f4e3c1; -fx-border-color: #a6763e; " +
                "-fx-padding: 5; -fx-text-fill: #3e2723;");

        sectionContent.setStyle("-fx-background-color: #d7b899; " +
                "-fx-border-color: #8b5e3c; -fx-border-radius: 15px; -fx-background-radius: 15px; " +
                "-fx-padding: 8; -fx-text-fill: #3e2723; " +
                "-fx-control-inner-background: #d7b899;");

        sectionContent.setStyle("-fx-background-color: #f4e3c1; -fx-border-color: #a6763e; " +
                "-fx-border-radius: 15; -fx-padding: 5; -fx-text-fill: #3e2723;");

        sectionContent.setStyle("-fx-background-color: #d7b899; " +
                "-fx-padding: 10; -fx-text-fill: #4e342e; " +
                "-fx-font-family: 'Comic Sans MS'; -fx-font-size: 14px; " +
                "-fx-control-inner-background: #d7b899;");

        contentBox.getChildren().addAll(sectionTitle, sectionContent);
    }

    private void applyStyles() {
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #d7a86e, #8b5e3c);");

        okButton.setStyle("-fx-background-color: #e0b589; -fx-border-color: #8b5e3c; -fx-border-width: 2px;" +
                "-fx-font-family: 'Comic Sans MS'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #3e2723; " +
                "-fx-background-radius: 15px; -fx-border-radius: 15px");

        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: #a6763e; " +
                "-fx-border-width: 2px; -fx-border-radius: 10;");
        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStylesheets().add(getClass().getResource("/stylesheets/help.css").toExternalForm());
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

    Button getOkButton() {
        return okButton;
    }
}

