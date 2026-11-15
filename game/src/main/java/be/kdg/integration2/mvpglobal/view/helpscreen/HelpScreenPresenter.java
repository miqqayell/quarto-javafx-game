package be.kdg.integration2.mvpglobal.view.helpscreen;

import be.kdg.integration2.mvpglobal.model.MVPModel;
import be.kdg.integration2.mvpglobal.model.SoundManager;
import javafx.event.Event;

public class HelpScreenPresenter {
    private MVPModel model;
    private HelpScreenView view;
    private final SoundManager soundManager;

    public HelpScreenPresenter(MVPModel model, HelpScreenView view) {
        this.model = model;
        this.view = view;
        soundManager = model.getSoundManager();
        EventHandlers();
    }

    private void EventHandlers() {
        view.getOkButton().setOnMouseClicked(event -> {
            soundManager.playButtonClick();
            handleCloseEvent(event);
        });
    }

    private void handleCloseEvent(Event event) {
        view.getScene().getWindow().hide();
    }
}
