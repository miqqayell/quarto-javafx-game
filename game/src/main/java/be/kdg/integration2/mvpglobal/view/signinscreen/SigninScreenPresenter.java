package be.kdg.integration2.mvpglobal.view.signinscreen;

import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenPresenter;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenView;
import be.kdg.integration2.mvpglobal.view.signupscreen.SignupScreenPresenter;
import be.kdg.integration2.mvpglobal.view.signupscreen.SignupScreenView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class SigninScreenPresenter {
    private MVPModel model;
    private SigninScreenView view;
    private final SoundManager soundManager;

    public SigninScreenPresenter(MVPModel model, SigninScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        EventHandlers();
    }

    private void EventHandlers() {
        view.getSubmitButton().setOnAction(e -> {
            soundManager.playButtonClick();
            try {
                validateData();
            } catch (SQLException ex) {
                System.out.println("Error cannot connect to database! (Validate Data)");
            }
        });

        view.getBackButton().setOnAction(e -> {
            soundManager.playButtonClick();
            view.getScene().getWindow().hide();
            RegistrationScreenView view = new RegistrationScreenView();
            Scene scene = new Scene(view);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Quarto");
            stage.getIcons().add(new Image("/images/logo.png"));
            stage.show();
            RegistrationScreenPresenter presenter = new RegistrationScreenPresenter(model, view);
            stage.setOnCloseRequest(event -> {
                event.consume();
                showCloseConfirmation(stage);
            });
        });

        view.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                stage.setOnCloseRequest(event -> {
                    event.consume();
                    showCloseConfirmation(stage);
                });
            }
        });
    }

    private void validateData() throws SQLException {
        if(model.getDb().getConnection() != null){
        String username = view.getUsernameInput().getText();
        String password = view.getPasswordInput().getText();

        if (username.isEmpty() || password.isEmpty()) {
            updateView();
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
            return;
        }

        if (!model.getDb().checkUsername(username)) {
            updateView();
            showAlert(Alert.AlertType.ERROR, "Username not found. Please try again.");
            return;
        }

        if (!model.getDb().checkPassword(username, password)) {
            updateView();
            showAlert(Alert.AlertType.ERROR, "Incorrect password. Please try again.");
            return;
        }

        int playerId = model.getDb().getPlayerIdByUsername(username);
        String country = model.getDb().getPlayerCountryByID(playerId);
        model.storePlayer(username, password, country, playerId);

        MainScreenView mainView = new MainScreenView();
        Scene scene = view.getScene();
        if (scene != null) {
            scene.setRoot(mainView);
            Stage stage = (Stage) scene.getWindow();
            stage.setHeight(Screen.getPrimary().getBounds().getHeight() - 150);
            stage.setWidth(Screen.getPrimary().getBounds().getWidth() - 200);
            stage.setX(100);
            stage.setY(50);

            MainScreenPresenter mainPresenter = new MainScreenPresenter(model, mainView);
            }
        }
    }

    private void showCloseConfirmation(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Terminate registration");
        alert.setHeaderText("Are you sure you want to terminate the registration?");
        alert.setContentText("Your data will not be saved.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Invalid data!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateView() {
        view.getUsernameInput().clear();
        view.getPasswordInput().clear();
        view.getUsernameInput().setStyle("-fx-background-color: #fd8a8a;");
        view.getPasswordInput().setStyle("-fx-background-color: #fd8a8a;");
    }
}