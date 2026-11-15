package be.kdg.integration2.mvpglobal;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenPresenter;
import be.kdg.integration2.mvpglobal.view.registrationscreen.RegistrationScreenView;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;

public class MVPMain extends Application {


    @Override
    public void start(Stage primaryStage) {
        MVPModel model = new MVPModel();
        RegistrationScreenView view = new RegistrationScreenView();
        Scene scene = new Scene(view);
        SoundManager soundManager = model.getSoundManager();
        soundManager.playBackgroundMusic();
        primaryStage.setTitle("Quarto");
        primaryStage.getIcons().add(new Image("/images/logo.png"));
        primaryStage.setScene(scene);
//        primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() / 1.25);
//        primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() / 2.5);
        primaryStage.show();
        RegistrationScreenPresenter presenter = new RegistrationScreenPresenter(model, view);
        presenter.windowsHandler();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
