package be.kdg.integration2.mvpglobal.view.leaderboardscreen;

import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.PlayerStats;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;

import java.util.List;

public class LeaderBoardPresenter {

    private MVPModel model;
    private LeaderBoardView view;
    private final SoundManager soundManager;

    public LeaderBoardPresenter(MVPModel model, LeaderBoardView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        EventHandlers();

        TableView<PlayerStats> table = view.getTable();
        table.getColumns().addAll(
                createColumn("Rank", "rank", 50),
                createColumn("Name", "name", 100),
                createColumn("Games", "games", 50),
                createColumn("W/L", "winLoss", 50),
                createColumn("Win %", "winPercentage", 50),
                createColumn("Avg. N moves", "avgMoves", 80),
                createColumn("Move duration", "moveDuration", 80)
        );

        List<PlayerStats> leaderboard = model.getDb().getLeaderboardStats("wins", false);
        table.getItems().setAll(leaderboard);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<PlayerStats, String> createColumn(String title, String property, double minWidth) {
        TableColumn<PlayerStats, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setMinWidth(minWidth);
        return column;
    }

    private void EventHandlers() {
        view.getScene().getWindow().setOnCloseRequest(event -> handleCloseEvent(event));
        view.getBtnOk().setOnAction(event -> {
            soundManager.playButtonClick();
            MainScreenView mainView = new MainScreenView();
            view.getScene().setRoot(mainView);
            mainView.getScene().getWindow().setHeight(Screen.getPrimary().getBounds().getHeight() - 150);
            mainView.getScene().getWindow().setWidth(Screen.getPrimary().getBounds().getWidth() - 200);
            mainView.getScene().getWindow().setX(100);
            mainView.getScene().getWindow().setY(50);
            MainScreenPresenter mainPresenter = new MainScreenPresenter(model, mainView);
        });
    }

    private void handleCloseEvent(Event event){
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
            view.getScene().getWindow().hide();
        }
    }
}