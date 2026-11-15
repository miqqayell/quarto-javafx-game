package be.kdg.integration2.mvpglobal.model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class responsible for managing event-related alerts in the application.
 * <p>
 * Provides static methods to display error alerts using JavaFX on the JavaFX Application thread.
 */
public class EventManager {

    /**
     * Displays an error alert dialog on the JavaFX Application thread with the specified title and message.
     *
     * @param title   the title of the error alert dialog
     * @param message the content message of the error alert dialog
     */
    public static void showErrorAlert(String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Triggers an error alert with a default title of "Error" and a generic error message.
     */
    public static void triggerError() {
        showErrorAlert("Error", "An error has occurred!");
    }

    /**
     * Triggers an error alert with a default title of "Error" and the specified message.
     *
     * @param message the content message of the error alert dialog
     */
    public static void triggerError(String message) {
        showErrorAlert("Error", message);
    }

    /**
     * Triggers an error alert with the specified title and message.
     *
     * @param title   the title of the error alert dialog
     * @param message the content message of the error alert dialog
     */
    public static void triggerError(String title, String message) {
        showErrorAlert(title, message);
    }
}
