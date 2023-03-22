package main.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.PhaseShiftController;

import java.io.IOException;

public class PhaseShiftViewer {
    private Scene scene;
    private Parent root;

    public PhaseShiftViewer() {
        createPhaseShiftViewer();
    }

    private void createPhaseShiftViewer() {
        try {
            FXMLLoader phaseShiftLoader = new FXMLLoader(PhaseShiftController.class.getResource("/phase-shift.fxml"));
            root = phaseShiftLoader.load();
            PhaseShiftController phaseShiftController = phaseShiftLoader.getController();
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