package main.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.WaveWithFrequencyController;

import java.io.IOException;

public class WaveWithFrequency {

    private Scene scene;
    private Parent root;

    public WaveWithFrequency() {
        createWaveWithFrequency();
    }

    private void createWaveWithFrequency() {
        try {
            FXMLLoader WWFLoader = new FXMLLoader(WaveWithFrequencyController.class.getResource("/wave-with-frequency.fxml"));
            root = WWFLoader.load();
            WaveWithFrequencyController WWFController = WWFLoader.getController();
            scene = new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startWaveWithFrequency(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
