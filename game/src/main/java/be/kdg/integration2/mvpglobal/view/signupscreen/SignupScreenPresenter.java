package be.kdg.integration2.mvpglobal.view.signupscreen;

import be.kdg.integration2.mvpglobal.model.EventManager;
import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenPresenter;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class SignupScreenPresenter {
    private MVPModel model;
    private SignupScreenView view;
    private final SoundManager soundManager;

    public SignupScreenPresenter(MVPModel model, SignupScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        EventHandlers();
    }

    private void EventHandlers() {
        view.getSubmitButton().setOnAction(e -> {
            soundManager.playButtonClick();
            try {
                validatePassword();
            } catch (SQLException ex) {
                System.out.println("Error cannot connect to database! (Validate Password)");
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

    private void validatePassword() throws SQLException {
        if(model.getDb().getConnection() != null) {
            String firstPassword = view.getPasswordInput().getText();
            String secondPassword = view.getConfirmPasswordInput().getText();
            boolean isValid = firstPassword != null && firstPassword.length() >= 5 && firstPassword.length() <= 30;
            String username = view.getUsernameInput().getText();
            String country = view.getCountryInput().getText();
            if (firstPassword.equals(secondPassword) && isValid && model.validateUsername(username) && !Objects.equals(username, "Guest")) {
                model.getDb().insertPlayer(username, firstPassword, country);
                int playerId = model.getDb().getPlayerIdByUsername(username);
                model.storePlayer(username, firstPassword, country, playerId);
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
            } else {
                if(view.getUsernameInput().getText().equals( "Guest"))
                    EventManager.triggerError("You cannot register as a guest. Please enter a valid username!");
                if (!firstPassword.equals(secondPassword) || firstPassword == null || firstPassword.isEmpty() || !isValid) {
                    view.getPasswordInput().setStyle("-fx-background-color: #fd8a8a;");
                    view.getConfirmPasswordInput().setStyle("-fx-background-color: #fd8a8a;");
                    view.getPasswordInput().clear();
                    view.getConfirmPasswordInput().clear();
                    validationAlert("Passwords must match and be at least 5 characters long!");
                }
                if (!model.validateUsername(username)) {
                    view.getUsernameInput().setStyle("-fx-background-color: #fd8a8a;");
                    view.getUsernameInput().clear();
                    validationAlert("Username must be at least 3 characters long!");
                }
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

    private void validationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Your data does not meet the requirements! Enter valid data!");
        alert.setContentText(message);
        alert.setTitle("Invalid input!");
        alert.getButtonTypes().clear();
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().add(cancel);
        alert.showAndWait();
    }

}