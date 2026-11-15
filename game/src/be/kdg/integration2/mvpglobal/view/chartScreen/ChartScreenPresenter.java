package be.kdg.integration2.mvpglobal.view.chartScreen;

import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import javafx.scene.chart.XYChart;
import javafx.stage.Screen;

import java.util.*;

public class ChartScreenPresenter {
    private MVPModel model;
    private ChartScreenView view;
    private final SoundManager soundManager;

    public ChartScreenPresenter(MVPModel model, ChartScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getBackButton().setOnAction(e -> {
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

    private void updateView() {
        List<Integer> moveDurations = model.getGameSession().getMovesDurationStorage();

        if (moveDurations == null || moveDurations.isEmpty()) return;

        Set<Integer> outlierIndices = findOutlierIndices(moveDurations);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int i = 0; i < moveDurations.size(); i++) {
            int duration = moveDurations.get(i);
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(String.valueOf(i + 1), duration);
            series.getData().add(dataPoint);

            boolean isOutlier = outlierIndices.contains(i);

            dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    if (isOutlier) {
                        newNode.setStyle("-fx-bar-fill: red;");
                    } else {
                        newNode.setStyle("-fx-bar-fill: steelblue;");
                    }
                }
            });
        }

        view.getBarChart().getData().clear();
        view.getBarChart().getData().add(series);
    }

    /* Calculate outliers
       Lower Bound = Q1 - 1.5 * IQR
       Upper Bound = Q3 + 1.5 * IQR
    */

    private Set<Integer> findOutlierIndices(List<Integer> data) {
        List<Integer> sorted = new ArrayList<>(data);
        Collections.sort(sorted);

        double q1 = getPercentile(sorted, 25);
        double q3 = getPercentile(sorted, 75);
        double iqr = q3 - q1;
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        Set<Integer> outliers = new HashSet<>();
        for (int i = 0; i < data.size(); i++) {
            int value = data.get(i);
            if (value < lowerBound || value > upperBound) {
                outliers.add(i);
            }
        }

        return outliers;
    }

    /* Q1 = 25%
       Q3 = 75%
    */

    private double getPercentile(List<Integer> sortedList, double percentile) {
        int n = sortedList.size();
        double index = (percentile / 100.0) * (n - 1);
        int lower = (int) Math.floor(index);
        int upper = (int) Math.ceil(index);

        if (lower == upper) return sortedList.get(lower);

        double weight = index - lower;
        return sortedList.get(lower) * (1 - weight) + sortedList.get(upper) * weight;
    }
}
