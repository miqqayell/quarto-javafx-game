package be.kdg.integration2.mvpglobal.view.settingsscreen;

import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenPresenter;
import be.kdg.integration2.mvpglobal.view.mainscreen.MainScreenView;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenPresenter;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenView;
import be.kdg.integration2.mvpglobal.view.settingsscreen.SettingsScreenView;
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

public class SettingsScreenPresenter {
    private MVPModel model;
    private SettingsScreenView view;
    private final SoundManager soundManager;

    public SettingsScreenPresenter(MVPModel model, SettingsScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        EventHandlers();
        updateView();
    }

    private void EventHandlers() {
        view.getBackButton().setOnAction(e -> {
            soundManager.playButtonClick();
            mainScreen();
        });
        //
        view.getMainVolumeSlider().setOnMouseReleased(e -> {
            soundManager.setMainVolume((int)view.getMainVolumeSlider().getValue());
            System.out.println(soundManager.getMainVolume());
        });
        view.getBgMusicSlider().setOnMouseReleased(e -> {
            soundManager.setBackgroundVolume((int)view.getBgMusicSlider().getValue());
            System.out.println(soundManager.getBackgroundVolume());
        });
        view.getClickVolumeSlider().setOnMouseReleased(e -> {
            soundManager.setButtonClickVolume((int)view.getClickVolumeSlider().getValue());
            System.out.println(soundManager.getButtonClickVolume());
        });
        view.getPieceVolumeSlider().setOnMouseReleased(e -> {
            soundManager.setPiecePlacedVolume((int)view.getPieceVolumeSlider().getValue());
            System.out.println(soundManager.getPiecePlacedVolume());
        });
        view.getWinLoseVolumeSlider().setOnMouseReleased(e -> {
            soundManager.setWinLoseVolume((int)view.getWinLoseVolumeSlider().getValue());
            System.out.println(soundManager.getWinLoseVolume());
        });
    }

    private void mainScreen(){
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

    private void updateView() {
        view.getMainVolumeSlider().setValue(soundManager.getMainVolume());
        view.getBgMusicSlider().setValue(soundManager.getRawBackgroundVolume());
        view.getClickVolumeSlider().setValue(soundManager.getRawButtonClickVolume());
        view.getPieceVolumeSlider().setValue(soundManager.getRawPiecePlacedVolume());
        view.getWinLoseVolumeSlider().setValue(soundManager.getRawWinLoseVolume());
    }
}