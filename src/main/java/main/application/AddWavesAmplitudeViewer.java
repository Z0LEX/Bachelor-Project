package main.application;

import main.controllers.AddWavesAmplitudeController;
import main.controllers.AddWavesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AddWavesAmplitudeViewer {
    private Scene scene;
    private Parent root;

    public AddWavesAmplitudeViewer() {
        createAddWavesAmplitudeViewer();
    }

    private void createAddWavesAmplitudeViewer() {
        try {
            FXMLLoader addWavesAmplitudeLoader = new FXMLLoader(AddWavesAmplitudeController.class.getResource("/add-waves-amplitude.fxml"));
            root = addWavesAmplitudeLoader.load();
            AddWavesAmplitudeController addWavesAmplitudeController = addWavesAmplitudeLoader.getController();
            scene = new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startAddWavesViewer(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
