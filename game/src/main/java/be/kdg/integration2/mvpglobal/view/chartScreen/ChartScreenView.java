package be.kdg.integration2.mvpglobal.view.chartScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ChartScreenView extends StackPane {
    private VBox chartContainer;
    private BarChart<String, Number> barChart;
    private Button backButton;
    private Label chartTitleLabel;

    public ChartScreenView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Move Number");
        yAxis.setLabel("Duration (seconds)");

        xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#3e2723"));
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#3e2723"));

        xAxis.setTickLabelFont(Font.font("Comic Sans MS", FontWeight.BOLD, 13));
        yAxis.setTickLabelFont(Font.font("Comic Sans MS", FontWeight.BOLD, 13));

        xAxis.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 22px; -fx-font-weight: bold;");
        yAxis.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 22px; -fx-font-weight: bold;");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);

        chartTitleLabel = new Label("Move Duration Overview");

        backButton = new Button("Menu");
        backButton.getStyleClass().add("button");
    }

    private void layoutNodes() {
        chartContainer = new VBox(20, chartTitleLabel, barChart);
        chartContainer.setAlignment(Pos.TOP_CENTER);
        chartContainer.setPadding(new Insets(30));
        chartTitleLabel.setAlignment(Pos.TOP_CENTER);
        chartTitleLabel.setPadding(new Insets(40, 0, 0, 0));

        Image bgImage = new Image(getClass().getResource("/images/start_background.png").toExternalForm());
        BackgroundImage bg = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        this.setBackground(new Background(bg));

        StackPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(backButton, new Insets(0, 50, 30, 0));

        this.getChildren().addAll(chartContainer, backButton);
        chartTitleLabel.setStyle("-fx-text-fill: #231e1e; -fx-font-family: \"Comic Sans MS\"; -fx-font-size: 34px; -fx-font-weight: bold;");
        getStylesheets().add(getClass().getResource("/stylesheets/chart.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/stylesheets/registration.css").toExternalForm());
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }

    public Button getBackButton() {
        return backButton;
    }
}

