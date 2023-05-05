package main.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.AddWavesController;

import java.io.IOException;

public class StageManager {

    private Stage stage;
    private Scene currentScene;

    public StageManager(Stage stage) {
        this.stage = stage;
    }

    // TODO: Should start the FXML loaders for all viewers - phase shift client waits for data
    public void switchScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(root);
            if (currentScene == null) {
                stage.setScene(newScene);
            } else {
                stage.getScene().setRoot(root);
            }
            currentScene = newScene;
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
