package main.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.FourierMachineController;

import java.io.IOException;

public class FourierMachineViewer {
    private Scene scene;
    private Parent root;

    public FourierMachineViewer(Stage stage) {
        createFourierMachineViewer();
    }

    private void createFourierMachineViewer() {
        try {
            FXMLLoader fourierMachineLoader = new FXMLLoader(FourierMachineController.class.getResource("/fourier-machine.fxml"));
            root = fourierMachineLoader.load();
            FourierMachineController fourierMachineController = fourierMachineLoader.getController();
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
