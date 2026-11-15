package be.kdg.integration2.mvpglobal.view.leaderboardscreen;

import be.kdg.integration2.mvpglobal.model.PlayerStats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LeaderBoardView extends StackPane {

    private ImageView backgroundView;
    private Button okButton;
    private TableView<PlayerStats> table;

    public LeaderBoardView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        Image backgroundImage = new Image(getClass().getResource("/images/start_background.png").toExternalForm());
        backgroundView = new ImageView(backgroundImage);
        okButton = new Button("Back");
        okButton.setPrefWidth(60);

        table = new TableView<>();
    }

    private void layoutNodes() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table, okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(25));

        getChildren().addAll(backgroundView, vbox);
        getStylesheets().add(getClass().getResource("/stylesheets/leaderboard.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/stylesheets/help.css").toExternalForm());
    }

    Button getBtnOk() {
        return okButton;
    }

    TableView<PlayerStats> getTable() {
        return table;
    }
}
