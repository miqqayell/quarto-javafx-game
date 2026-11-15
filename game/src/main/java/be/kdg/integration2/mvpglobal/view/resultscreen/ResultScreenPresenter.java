package be.kdg.integration2.mvpglobal.view.resultscreen;

import be.kdg.integration2.mvpglobal.model.HumanPlayer;
import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.chartScreen.ChartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.chartScreen.ChartScreenView;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import javafx.stage.Screen;

public class ResultScreenPresenter {
    private final MVPModel model;
    private final ResultScreenView view;
    private HumanPlayer player;
    private final SoundManager soundManager;

    public ResultScreenPresenter(MVPModel model, ResultScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getBackToMenuButton().setOnAction(e -> {
            soundManager.playButtonClick();
            MainScreenView mainView = new MainScreenView();
            view.getScene().setRoot(mainView);
            mainView.getScene().getWindow().setHeight(Screen.getPrimary().getBounds().getHeight() - 150);
            mainView.getScene().getWindow().setWidth(Screen.getPrimary().getBounds().getWidth() - 200);
            mainView.getScene().getWindow().setX(100);
            mainView.getScene().getWindow().setY(50);
            MainScreenPresenter mainPresenter = new MainScreenPresenter(model, mainView);
        });

        view.getViewGameDetailsButton().setOnAction(e -> {
            soundManager.playButtonClick();
            ChartScreenView chartView = new ChartScreenView();
            view.getScene().setRoot(chartView);
            chartView.getScene().getWindow().setHeight(Screen.getPrimary().getBounds().getHeight() / 1.5);
            chartView.getScene().getWindow().setWidth(Screen.getPrimary().getBounds().getWidth() / 2);
            chartView.getScene().getWindow().setX(Screen.getPrimary().getBounds().getWidth() / 2 - 375);
            chartView.getScene().getWindow().setY(Screen.getPrimary().getBounds().getHeight() / 2 - 300);
            ChartScreenPresenter chartPresenter = new ChartScreenPresenter(model, chartView);
        });
    }
}
