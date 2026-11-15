package be.kdg.integration2.mvpglobal.view.boardscreen;

import be.kdg.integration2.mvpglobal.model.Cell;
import javafx.animation.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class BoardScreenView extends BorderPane {
    private static final double CELL_SIZE = 125;
    private final Map<String, Image> imageCache = new HashMap<>();
    private GridPane boardGrid;
    private Button[][] boardButtons;
    private Label instructionLabel;
    private FlowPane piecesPane;
    private Label selectionLabel;
    private VBox selectionContainer;
    private FlowPane selectionPane;
    private Button backButton;

    public BoardScreenView() {
        initializeNodes();
        layoutNodes();
        setupAnimations();
    }

    public void initializeNodes() {
        boardGrid = new GridPane();
        boardButtons = new Button[4][4];
        instructionLabel = new Label();
        piecesPane = new FlowPane();
        selectionLabel = new Label();
        selectionContainer = new VBox();
        selectionPane = new FlowPane();
        backButton = new Button("Back");
    }

    public void layoutNodes() {
        instructionLabel.setStyle(
                "-fx-font-family: \"Comic Sans MS\"; -fx-text-fill: #231e1e; -fx-font-size:34px; -fx-font-weight:bold;"
        );
        instructionLabel.setAlignment(Pos.CENTER);
        instructionLabel.setPadding(new Insets(60, 0, 0, 0));
        BorderPane.setAlignment(instructionLabel, Pos.CENTER);
        createBoardGrid();
        createSelectionPane();
        createAvailablePiecesPane();

        double gapWidth = Screen.getPrimary().getBounds().getWidth() * 0.05;

        HBox center = new HBox(gapWidth, selectionContainer, boardGrid, piecesPane);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(10));

        setTop(instructionLabel);
        setCenter(center);

        GridPane grid = new GridPane();
        getStylesheets().add(getClass().getResource("/stylesheets/board.css").toExternalForm());

        backButton.setId("backButton");
        grid.setPadding(new Insets(0, 0, 30, 30));
        grid.setConstraints(this.backButton, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
        grid.add(this.backButton, 0, 4);
        setBottom(grid);


        Image backgroundImage = new Image(getClass().getResource("/images/start_background.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        setBackground(new Background(background));
    }

    private void setupAnimations() {
        FadeTransition bgFade = new FadeTransition(Duration.seconds(1.5), this);
        bgFade.setFromValue(0);
        bgFade.setToValue(1);
        bgFade.play();

        FadeTransition labelFade = new FadeTransition(Duration.seconds(1), instructionLabel);
        labelFade.setFromValue(0);
        labelFade.setToValue(1);

        TranslateTransition labelSlide = new TranslateTransition(Duration.seconds(1), instructionLabel);
        labelSlide.setFromY(-50);
        labelSlide.setToY(0);

        ParallelTransition labelAnimation = new ParallelTransition(labelFade, labelSlide);
        labelAnimation.play();

        animateBoardGrid();
        FadeTransition selectionFade = new FadeTransition(Duration.seconds(0.8), selectionContainer);
        selectionFade.setFromValue(0);
        selectionFade.setToValue(1);

        ScaleTransition selectionScale = new ScaleTransition(Duration.seconds(0.8), selectionContainer);
        selectionScale.setFromX(0.8);
        selectionScale.setFromY(0.8);
        selectionScale.setToX(1);
        selectionScale.setToY(1);

        PauseTransition selectionDelay = new PauseTransition(Duration.millis(300));
        SequentialTransition selectionAnimation = new SequentialTransition(
                selectionDelay,
                new ParallelTransition(selectionFade, selectionScale)
        );
        selectionAnimation.play();
        FadeTransition piecesFade = new FadeTransition(Duration.seconds(0.8), piecesPane);
        piecesFade.setFromValue(0);
        piecesFade.setToValue(1);

        TranslateTransition piecesSlide = new TranslateTransition(Duration.seconds(0.8), piecesPane);
        piecesSlide.setFromX(50);
        piecesSlide.setToX(0);

        PauseTransition piecesDelay = new PauseTransition(Duration.millis(600));
        SequentialTransition piecesAnimation = new SequentialTransition(
                piecesDelay,
                new ParallelTransition(piecesFade, piecesSlide)
        );
        piecesAnimation.play();
    }

    private void animateBoardGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button cell = boardButtons[i][j];
                cell.setOpacity(0);
                cell.setTranslateY(-500);
                int delay = (i * 4 + j) * 100;
                TranslateTransition translate = new TranslateTransition(Duration.seconds(0.3), cell);
                translate.setToY(0);
                FadeTransition fade = new FadeTransition(Duration.seconds(0.1), cell);
                fade.setToValue(1.0);
                ParallelTransition dropIn = new ParallelTransition(translate, fade);
                SequentialTransition cellAnimation = new SequentialTransition(
                        new PauseTransition(Duration.millis(delay)),
                        dropIn
                );
                cellAnimation.play();
                cell.setOnMouseEntered(e -> animateCellHover(cell, 1.05));
                cell.setOnMouseExited(e -> animateCellHover(cell, 1.0));
                cell.setOnDragEntered(e -> {
                    if (e.getGestureSource() != cell) animateCellHover(cell, 1.05);
                });
                cell.setOnDragExited(e -> animateCellHover(cell, 1.0));
                cell.setOnDragOver(e -> {
                    if (e.getGestureSource() != cell) {
                        e.acceptTransferModes(TransferMode.COPY);
                        e.consume();
                    }
                });
            }
        }
    }

    private void animateCellHover(Button cell, double scaleTo) {
        ScaleTransition hover = new ScaleTransition(Duration.millis(150), cell);
        hover.setToX(scaleTo);
        hover.setToY(scaleTo);
        hover.play();
    }

    private void createBoardGrid() {
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);
        boardGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button cell = new Button();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                cell.setMaxSize(CELL_SIZE, CELL_SIZE);
                cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                cell.setStyle("-fx-background-radius: 15px; -fx-background-radius: 15px; -fx-border-width: 5px; " +
                        "-fx-border-color: #4B3B30; -fx-background-color: #A67B5B; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 2, 2);");

                boardButtons[i][j] = cell;
                boardGrid.add(cell, j, i);
            }
        }
    }

    private void createSelectionPane() {
        selectionLabel = new Label("This is your piece");
        selectionLabel.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-text-fill: #231e1e; -fx-font-size:20px; -fx-font-weight:bold;");
        selectionLabel.setAlignment(Pos.CENTER);

        selectionPane = new FlowPane(8, 8);
        selectionPane.setPrefWrapLength(CELL_SIZE + 50);
        selectionPane.setPrefSize(CELL_SIZE + 25, CELL_SIZE + 25);
        selectionPane.setStyle("-fx-background-radius:20px; -fx-background-color: #C19A6B; -fx-border-radius: 20px; -fx-border-color: #4B3B30; -fx-border-width: 5px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 3, 3);");
        selectionPane.setAlignment(Pos.CENTER);

        selectionContainer = new VBox(5, selectionLabel, selectionPane);
        selectionContainer.setAlignment(Pos.CENTER);
        selectionContainer.setOpacity(0);
    }

    private void createAvailablePiecesPane() {
        piecesPane = new FlowPane(4, 4);
        piecesPane.setPrefWrapLength(400);
        piecesPane.setMaxHeight(400);
        piecesPane.setAlignment(Pos.CENTER);
        piecesPane.setStyle("-fx-background-radius:20px; -fx-background-color: #C19A6B; -fx-border-radius: 20px; -fx-border-color: #4B3B30; -fx-border-width: 5px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 3, 3);");
        piecesPane.setOpacity(0);
    }

    public void showGivenPiece(String pieceId) {
        double seconds = 0.1;
        selectionLabel.setText("This is your piece");
        selectionPane.getChildren().clear();
        Node n = createDraggableOrPlaceholder(pieceId);
        n.setUserData(pieceId);
        n.setOpacity(0);
        n.setScaleX(0.5);
        n.setScaleY(0.5);
        selectionPane.setOpacity(0);
        selectionPane.setScaleX(0.8);
        selectionPane.setScaleY(0.8);
        selectionPane.getChildren().add(n);
        FadeTransition pieceFade = new FadeTransition(Duration.seconds(seconds), n);
        pieceFade.setFromValue(0);
        pieceFade.setToValue(1);
        ScaleTransition pieceScale = new ScaleTransition(Duration.seconds(seconds), n);
        pieceScale.setFromX(0.5);
        pieceScale.setFromY(0.5);
        pieceScale.setToX(1);
        pieceScale.setToY(1);
        pieceScale.setInterpolator(Interpolator.EASE_OUT);
        FadeTransition paneFade = new FadeTransition(Duration.seconds(seconds), selectionPane);
        paneFade.setFromValue(0);
        paneFade.setToValue(1);
        ScaleTransition paneScale = new ScaleTransition(Duration.seconds(seconds), selectionPane);
        paneScale.setFromX(0.8);
        paneScale.setFromY(0.8);
        paneScale.setToX(1);
        paneScale.setToY(1);
        paneScale.setInterpolator(Interpolator.EASE_OUT);
        ParallelTransition fullAnimation = new ParallelTransition(
                new ParallelTransition(pieceFade, pieceScale),
                new ParallelTransition(paneFade, paneScale)
        );
        fullAnimation.play();
    }

    public Node createDraggableOrPlaceholder(String pid) {
        Image img = imageCache.get(pid);
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(CELL_SIZE - 40);
            iv.setFitHeight(CELL_SIZE - 40);
            iv.setPreserveRatio(true);
            iv.setOnDragDetected(e -> {
                Dragboard db = iv.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cc = new ClipboardContent();
                cc.putString(pid);
                db.setContent(cc);
                e.consume();


                ScaleTransition shrink = new ScaleTransition(Duration.millis(100), iv);
                shrink.setToX(0.9);
                shrink.setToY(0.9);
                shrink.play();
            });
            return iv;
        } else {
            Label lbl = new Label(pid.replace("-", "\n"));
            lbl.setPrefSize(CELL_SIZE, CELL_SIZE);
            lbl.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-text-fill: #231e1e; -fx-border-color:black; -fx-font-size:10px; -fx-alignment:center;");
            lbl.setOnDragDetected(e -> {
                Dragboard db = lbl.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cc = new ClipboardContent();
                cc.putString(pid);
                db.setContent(cc);
                e.consume();


                ScaleTransition shrink = new ScaleTransition(Duration.millis(100), lbl);
                shrink.setToX(0.9);
                shrink.setToY(0.9);
                shrink.play();
            });
            return lbl;
        }
    }

    public void promptPlaceGiven() {
        instructionLabel.setText("Place the piece which was given to you");


        FadeTransition flash = new FadeTransition(Duration.millis(500), instructionLabel);
        flash.setFromValue(1.0);
        flash.setToValue(0.7);
        flash.setAutoReverse(true);
        flash.setCycleCount(4);
        flash.play();
    }

    public void updateBoard(Cell[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Button cell = boardButtons[i][j];
                String pid = board[i][j].getValueId();

                ScaleTransition shrink = new ScaleTransition(Duration.millis(100), cell);
                shrink.setToX(0.9);
                shrink.setToY(0.9);

                ScaleTransition grow = new ScaleTransition(Duration.millis(200), cell);
                grow.setToX(1.0);
                grow.setToY(1.0);

                SequentialTransition updateAnimation = new SequentialTransition(
                        shrink,
                        new PauseTransition(Duration.millis(50)),
                        grow
                );

                if (pid != null) {
                    Image img = imageCache.get(pid);
                    if (img != null) {
                        ImageView iv = new ImageView(img);
                        iv.setFitWidth(CELL_SIZE - 40);
                        iv.setFitHeight(CELL_SIZE - 40);
                        iv.setPreserveRatio(true);

                        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), iv);
                        fadeIn.setFromValue(0);
                        fadeIn.setToValue(1);

                        cell.setGraphic(iv);
                        cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        updateAnimation.getChildren().add(fadeIn);
                    } else {
                        cell.setText(pid.replace("-", "\n"));
                        cell.setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                } else {
                    cell.setGraphic(null);
                    cell.setText(null);
                }
            }
        }
    }

    GridPane getBoardGrid() {
        return boardGrid;
    }

    void setBoardGrid(GridPane boardGrid) {
        this.boardGrid = boardGrid;
    }

    public Button getBackButton() {
        return backButton;
    }

    Button[][] getBoardButtons() {
        return boardButtons;
    }

    void setBoardButtons(Button[][] boardButtons) {
        this.boardButtons = boardButtons;
    }

    Label getInstructionLabel() {
        return instructionLabel;
    }

    void setInstructionLabel(Label instructionLabel) {
        this.instructionLabel = instructionLabel;
    }

    FlowPane getPiecesPane() {
        return piecesPane;
    }

    void setPiecesPane(FlowPane piecesPane) {
        this.piecesPane = piecesPane;
    }

    Label getSelectionLabel() {
        return selectionLabel;
    }

    void setSelectionLabel(Label selectionLabel) {
        this.selectionLabel = selectionLabel;
    }

    VBox getSelectionContainer() {
        return selectionContainer;
    }

    void setSelectionContainer(VBox selectionContainer) {
        this.selectionContainer = selectionContainer;
    }

    FlowPane getSelectionPane() {
        return selectionPane;
    }

    void setSelectionPane(FlowPane selectionPane) {
        this.selectionPane = selectionPane;
    }

    Map<String, Image> getImageCache() {
        return imageCache;
    }
}