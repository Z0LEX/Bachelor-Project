package main.application;

import main.controllers.AddWavesAmplitudeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AddWavesAmplitudeViewer {
    private Scene scene;
    private Parent root;

    private Stage stage;

    public AddWavesAmplitudeViewer(Stage stage) {
        this.stage=stage;
        createAddWavesAmplitudeViewer();

    }

    private void createAddWavesAmplitudeViewer() {
        try {
            FXMLLoader addWavesAmplitudeLoader = new FXMLLoader(AddWavesAmplitudeController.class.getResource("/add-waves-amplitude.fxml"));
            root = addWavesAmplitudeLoader.load();
            AddWavesAmplitudeController addWavesAmplitudeController = addWavesAmplitudeLoader.getController();
            addWavesAmplitudeController.setStage(stage);
            scene = new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startAddWavesAmplitudeViewer(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
