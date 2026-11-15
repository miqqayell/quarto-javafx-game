package be.kdg.integration2.mvpglobal.view.mainscreen;

import be.kdg.integration2.mvpglobal.model.*;

import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.boardscreen.BoardScreenPresenter;
import be.kdg.integration2.mvpglobal.view.boardscreen.BoardScreenView;
import be.kdg.integration2.mvpglobal.view.helpscreen.HelpScreenPresenter;
import be.kdg.integration2.mvpglobal.view.helpscreen.HelpScreenView;
import be.kdg.integration2.mvpglobal.view.leaderboardscreen.LeaderBoardPresenter;
import be.kdg.integration2.mvpglobal.view.leaderboardscreen.LeaderBoardView;
import be.kdg.integration2.mvpglobal.view.settingsscreen.SettingsScreenPresenter;
import be.kdg.integration2.mvpglobal.view.settingsscreen.SettingsScreenView;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class MainScreenPresenter {
    private MVPModel model;
    private MainScreenView view;
    private Stage helpScreenStage = null;
    private double lastWidth;
    private double lastHeight;
    private final SoundManager soundManager;

    public MainScreenPresenter(MVPModel model, MainScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        EventHandlers();
        resizeImage();
    }

    private void EventHandlers() {
        view.getScene().getWindow().setOnCloseRequest(event -> handleCloseEvent(event));
        view.getQuitButton().setOnAction(e -> System.exit(0));
        view.getContinueGameButton().setOnAction(e -> {
            soundManager.playButtonClick();
        });
        view.getSettingsButton().setOnAction(e -> {
            soundManager.playButtonClick();
            SettingsScreenView settingsView = new SettingsScreenView();
            Scene currentScene = view.getScene();
            currentScene.setRoot(settingsView);
            Window currentWindow = currentScene.getWindow();
            currentWindow.setWidth(Screen.getPrimary().getBounds().getWidth() / 1.75);
            currentWindow.setHeight(Screen.getPrimary().getBounds().getHeight() / 2 + 100);
            currentWindow.centerOnScreen();
            SettingsScreenPresenter settingsPresenter = new SettingsScreenPresenter(model, settingsView);
        });
        view.getHelpButton().setOnAction(e -> {
            soundManager.playButtonClick();
            if (helpScreenStage != null && helpScreenStage.isShowing()) {
                helpScreenStage.toFront();
                helpScreenStage.requestFocus();
                helpScreenStage.setHeight(Screen.getPrimary().getBounds().getHeight() - 200);
                helpScreenStage.centerOnScreen();
                return;
            }
            HelpScreenView helpScreenView = new HelpScreenView();
            HelpScreenPresenter helpScreenPresenter = new HelpScreenPresenter(model, helpScreenView);
            helpScreenStage = new Stage();
            Scene helpScreenScene = new Scene(helpScreenView);
            helpScreenStage.setTitle("Rules");
            helpScreenStage.getIcons().add(new Image("/images/logo.png"));
            helpScreenStage.setScene(helpScreenScene);
            helpScreenStage.setHeight(Screen.getPrimary().getBounds().getHeight() - 200);
            helpScreenStage.setOnHidden(event -> helpScreenStage = null);
            helpScreenStage.show();
        });

        view.getLeaderboardButton().setOnAction(e -> {
            soundManager.playButtonClick();
            try {
                if(model.getDb().getConnection() != null) {
                    LeaderBoardView leaderboardView = new LeaderBoardView();
                    view.getScene().setRoot(leaderboardView);
                    leaderboardView.getScene().getWindow().setHeight(Screen.getPrimary().getBounds().getHeight() / 2);
                    leaderboardView.getScene().getWindow().setWidth(Screen.getPrimary().getBounds().getWidth() / 1.75);
                    leaderboardView.getScene().getWindow().centerOnScreen();
                    LeaderBoardPresenter leaderboardPresenter = new LeaderBoardPresenter(model, leaderboardView);
                }
            } catch (SQLException ex) {
                System.out.println("Error cannot connect to database! (Leaderboard)");
            }
        });

        view.getNewGameButton().setOnAction(e -> {
            soundManager.playButtonClick();
            try {
                if(!model.getPlayer().getUsername().contains("Guest") && model.getDb().getConnection() != null) {
                    model.startGameSession();
                    model.getGameSession().setGameID(model.getDb().getLastGameId());
                    model.getGameSession().getMove().setStartMove(LocalDateTime.now());
                    BoardScreenView boardView = new BoardScreenView();
                    BoardScreenPresenter boardPresenter = new BoardScreenPresenter(model, boardView);

                    boardView.updateBoard(model.getCells());
                    boardPresenter.updateAvailablePieces(model.getAvailablePieceIds());
                    view.getScene().getWindow().hide();

                    Scene scene = new Scene(boardView, view.getScene().getWidth(), view.getScene().getHeight());
                    Stage boardScreenStage = new Stage();
                    boardScreenStage.setTitle("Quarto");
                    boardScreenStage.getIcons().add(new Image("/images/logo.png"));
                    boardScreenStage.setResizable(false);
                    boardScreenStage.setScene(scene);
                    boardScreenStage.show();
                } else {
                    model.startGameSession();
                    model.getGameSession().setGameID(-1);
                    model.getGameSession().getMove().setStartMove(LocalDateTime.now());
                    BoardScreenView boardView = new BoardScreenView();
                    BoardScreenPresenter boardPresenter = new BoardScreenPresenter(model, boardView);
                    boardView.updateBoard(model.getCells());
                    boardPresenter.updateAvailablePieces(model.getAvailablePieceIds());
                    view.getScene().getWindow().hide();
                    Scene scene = new Scene(boardView, view.getScene().getWidth(), view.getScene().getHeight());
                    Stage boardScreenStage = new Stage();
                    boardScreenStage.setTitle("Quarto");
                    boardScreenStage.getIcons().add(new Image("/images/logo.png"));
                    boardScreenStage.setResizable(false);
                    boardScreenStage.setScene(scene);
                    boardScreenStage.show();
                }
            } catch (SQLException ex) {
                System.out.println("Error cannot connect to database! (New Game)");
            }
        });
    }

    private void resizeImage() {
        view.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > lastWidth) {
                view.getBackgroundView().setFitWidth(newVal.doubleValue());
                lastWidth = newVal.doubleValue();
            }
        });
        view.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > lastHeight) {
                view.getBackgroundView().setFitHeight(newVal.doubleValue());
                lastHeight = newVal.doubleValue();
            }
        });
    }

    private void handleCloseEvent(Event event) {
        final Alert stopWindow = new Alert(Alert.AlertType.CONFIRMATION);
        stopWindow.setHeaderText("You're closing the application.");
        stopWindow.setContentText("Are you sure? Unsaved data may be lost.");
        stopWindow.setTitle("WARNING!");
        stopWindow.getButtonTypes().clear();
        ButtonType noButton = new ButtonType("No");
        ButtonType yesButton = new ButtonType("Yes");
        stopWindow.getButtonTypes().addAll(yesButton, noButton);
        stopWindow.showAndWait();
        if (stopWindow.getResult() == null || stopWindow.getResult().equals(noButton)) {
            event.consume();
        } else {
            Platform.exit();
        }
    }
}
