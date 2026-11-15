package be.kdg.integration2.mvpglobal.view.boardscreen;

import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.PieceType;
import be.kdg.integration2.mvpglobal.model.enums.Color;
import be.kdg.integration2.mvpglobal.model.enums.Fill;
import be.kdg.integration2.mvpglobal.model.enums.Height;
import be.kdg.integration2.mvpglobal.model.enums.Shape;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import be.kdg.integration2.mvpglobal.view.resultscreen.ResultScreenPresenter;
import be.kdg.integration2.mvpglobal.view.resultscreen.ResultScreenView;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Screen;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

public class BoardScreenPresenter {
    private static final double CELL_SIZE = 125;

    private final MVPModel model;
    private final BoardScreenView view;
    private String currentGivenPiece;
    private final SoundManager soundManager;

    public BoardScreenPresenter(MVPModel model, BoardScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        updateView();
        startTurn();
        addEventHandlers();
    }

    private void buildPieceTypes() {
        for (Color color : Color.values())
            for (Shape shape : Shape.values())
                for (Height height : Height.values())
                    for (Fill fill : Fill.values()) {
                        model.getPieceTypes().add(new PieceType(color, shape, height, fill));
                    }
    }

    private void loadImages() {
        for (PieceType pt : model.getPieceTypes()) {
            String path = "/pieces/" + pt.getId() + ".png";
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                view.getImageCache().put(pt.getId(), new Image(is));
            }
        }
    }

    private void updateView() {
        buildPieceTypes();
        loadImages();
    }

    private void startTurn() {
        currentGivenPiece = model.getCurrentGivenPiece();
        view.showGivenPiece(currentGivenPiece);
        view.promptPlaceGiven();

        updateAvailablePieces(model.getAvailablePieceIds());

    }

    public void addEventHandlers() {
        view.getBackButton().setOnAction(e -> {
            soundManager.playButtonClick();
            MainScreenView mainView = new MainScreenView();
            Scene currentScene = view.getScene();
            currentScene.setRoot(mainView);
            mainView.getBackgroundView().fitWidthProperty().bind(mainView.getScene().widthProperty());
            mainView.getBackgroundView().fitHeightProperty().bind(mainView.getScene().heightProperty());
            mainView.getBackgroundView().setPreserveRatio(false);
            MainScreenPresenter mainPresenter = new MainScreenPresenter(model, mainView);
        });

        for (int i = 0; i < view.getBoardButtons().length; i++) {
            for (int j = 0; j < view.getBoardButtons()[i].length; j++) {

                // allow drop if empty
                Button cell = view.getBoardButtons()[i][j];
                cell.setOnDragOver(e -> {
                    boolean empty = cell.getGraphic() == null && (cell.getText() == null || cell.getText().isEmpty());
                    if (e.getDragboard().hasString() && empty) {
                        e.acceptTransferModes(TransferMode.COPY);
                    }
                    e.consume();
                });

                final int row = i, col = j;
                cell.setOnDragDropped(e -> {
                    Dragboard db = e.getDragboard();
                    if (db.hasString()) {
                        handleUserPlacement(row, col, db.getString());
                        e.setDropCompleted(true);
                    }
                    e.consume();
                });
            }
        }
        updateView();
    }

    public void promptSelectForOpponent() {
        view.getInstructionLabel().setText("Select a piece for your opponent");
        view.getPiecesPane().getChildren().forEach(node -> {
            String pid = (String) node.getUserData();
            node.setOnMouseClicked(e -> {
                if (this != null && pid != null) {
                    handleOpponentSelection(pid);
                }
            });
        });
    }

    public void handleUserPlacement(int row, int col, String pid) {
        model.getGameSession().setPlayerMovesCounter(model.getGameSession().getPlayerMovesCounter() + 1);
        if (!pid.equals(currentGivenPiece)) return;
        if (!model.placePiece(row, col, pid)) return;

        soundManager.playPiecePlace();
        view.updateBoard(model.getCells());
        updateAvailablePieces(model.getAvailablePieceIds());
        if (model.hasWinner()) {
            soundManager.playWinGame();
            String winner = model.getPlayer().getUsername() + " has won the game!";
            String gameTime = model.getGameSession().calculateGameDuration(model.getGameSession().getStartTime(),
                                                                           model.getGameSession().getEndTime());
            int playerMoves = model.getGameSession().getPlayerMovesCounter();
            int computerMoves = model.getGameSession().getComputerMovesCounter();
            String avg = model.getGameSession().calculateAvgMoveDuration();
            LocalDateTime start = model.getGameSession().getStartTime();
            LocalDateTime end = model.getGameSession().getEndTime();

            model.getGameSession().setEndTime(LocalDateTime.now());
            if(!model.getPlayer().getUsername().equals("Guest")) {
                model.getDb().insertGameInfo(model.getPlayer().getID(), start, end, "Win");
                model.getDb().insertMoveInfo(model.getDb().getLastGameId(), model.getGameSession().getMovesDurationStorage());
            }
            showResults(winner, gameTime, playerMoves, computerMoves, avg);
            model.reset();
            startTurn();
            view.updateBoard(model.getCells());

            confirmMove();
            return;
        }

        confirmMove();
        // move to opponent selection
        promptSelectForOpponent();
    }

    public void handleOpponentSelection(String pid) {
        model.getGameSession().setComputerMovesCounter(model.getGameSession().getComputerMovesCounter() + 1);
        model.getInferenceEngine().determineFacts(model.getBoard(), pid);
        model.getInferenceEngine().applyRules(model.getBoard(), pid);

        soundManager.playPiecePlace();
        view.updateBoard(model.getCells());
        updateAvailablePieces(model.getAvailablePieceIds());
        if (model.hasWinner()) {
            soundManager.playLoseGame();
            String winner = "Computer has won the game!";
            String gameTime = model.getGameSession().calculateGameDuration(model.getGameSession().getStartTime(),
                                                                           model.getGameSession().getEndTime());
            int playerMoves = model.getGameSession().getPlayerMovesCounter();
            int computerMoves = model.getGameSession().getComputerMovesCounter();
            String avg = model.getGameSession().calculateAvgMoveDuration();
            LocalDateTime start = model.getGameSession().getStartTime();
            LocalDateTime end = model.getGameSession().getEndTime();

            model.getGameSession().setEndTime(LocalDateTime.now());
            if(!model.getPlayer().getUsername().equals("Guest")) {
                model.getDb().insertGameInfo(model.getPlayer().getID(), start, end, "Lose");
                model.getDb().insertMoveInfo(model.getDb().getLastGameId(), model.getGameSession().getMovesDurationStorage());
            }
            showResults(winner, gameTime, playerMoves, computerMoves, avg);
            model.reset();
            startTurn();
            view.updateBoard(model.getCells());
            return;
        }

        model.getGameSession().getMove().setStartMove(LocalDateTime.now());

        // next cycle
        startTurn();
    }

    public void updateAvailablePieces(List<String> availableIds) {
        view.getPiecesPane().getChildren().clear();
        for (String pid : availableIds) {
            Node n = view.createDraggableOrPlaceholder(pid);
            n.setUserData(pid);
            view.getPiecesPane().getChildren().add(n);
        }
    }

    public void confirmMove() {
        model.getGameSession().getMove().setEndMove(LocalDateTime.now());
        model.getGameSession().getMovesDurationStorage().add(model.getGameSession().calculateMoveDuration());
    }

    public void showResults(String winner, String gameDuration, int playerMoves, int computerMoves, String moveDuration) {
        ResultScreenView resultScreenView = new ResultScreenView();
        resultScreenView.getWinnerLabel().setText(winner);
        resultScreenView.getTotalPlayTimeLabel().setText("Total Play Time: " + gameDuration);
        resultScreenView.getMovesPlayer1Label().setText("Number of moves made by Player: " + playerMoves);
        resultScreenView.getMovesPlayer2Label().setText("Number of moves made by Computer: " + computerMoves);
        resultScreenView.getAvgDurationPlayer1Label().setText("Average duration per move for Player: " + moveDuration);
        view.getScene().setRoot(resultScreenView);
        resultScreenView.getScene().getWindow().setHeight(Screen.getPrimary().getBounds().getHeight() / 2);
        resultScreenView.getScene().getWindow().setWidth(Screen.getPrimary().getBounds().getWidth() / 2);
        resultScreenView.getScene().getWindow().setX(Screen.getPrimary().getBounds().getWidth() / 2 - 350);
        resultScreenView.getScene().getWindow().setY(Screen.getPrimary().getBounds().getHeight() / 2 - 300);
        ResultScreenPresenter resultScreenPresenter = new ResultScreenPresenter(model, resultScreenView);
    }
}
