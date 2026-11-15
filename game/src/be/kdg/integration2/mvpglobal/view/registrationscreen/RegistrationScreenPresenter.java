package be.kdg.integration2.mvpglobal.view.registrationscreen;

import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import be.kdg.integration2.mvpglobal.view.signinscreen.SigninScreenPresenter;
import be.kdg.integration2.mvpglobal.view.signinscreen.SigninScreenView;
import be.kdg.integration2.mvpglobal.view.signupscreen.SignupScreenPresenter;
import be.kdg.integration2.mvpglobal.view.signupscreen.SignupScreenView;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RegistrationScreenPresenter {
    private MVPModel model;
    private RegistrationScreenView view;
    private final SoundManager soundManager;

    public RegistrationScreenPresenter(MVPModel model, RegistrationScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        windowsHandler();
    }

    public void windowsHandler() {
        view.getExitButton().setOnAction(e -> System.exit(0));

        view.getPlayGuestButton().setOnAction(e -> {
            soundManager.playButtonClick();
            int playerId = -1;
            String country = "Guest";
            model.storePlayer("Guest", "Guest", "Guest", playerId);
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
        });

        view.getRegisterButton().setOnAction(e -> {
            soundManager.playButtonClick();
            SignupScreenView signupView = new SignupScreenView();
            view.getScene().setRoot(signupView);
            signupView.getScene().getWindow().setHeight(325);
            signupView.getScene().getWindow().setWidth(550);
            signupView.getScene().getWindow().setX((Screen.getPrimary().getBounds().getWidth() / 2) - 250);
            signupView.getScene().getWindow().setY((Screen.getPrimary().getBounds().getHeight() / 2) - 250);
            SignupScreenPresenter signupPresenter = new SignupScreenPresenter(model, signupView);
        });

        view.getLoginButton().setOnAction(e -> {
            soundManager.playButtonClick();
            SigninScreenView signinView = new SigninScreenView();
            view.getScene().setRoot(signinView);
            signinView.getScene().getWindow().setHeight(250);
            signinView.getScene().getWindow().setWidth(550);
            signinView.getScene().getWindow().setX((Screen.getPrimary().getBounds().getWidth() / 2) - 250);
            signinView.getScene().getWindow().setY((Screen.getPrimary().getBounds().getHeight() / 2) - 250);
            SigninScreenPresenter signupPresenter = new SigninScreenPresenter(model, signinView);
        });
    }
}
