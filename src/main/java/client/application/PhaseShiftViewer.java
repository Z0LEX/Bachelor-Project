package client.application;

import client.controllers.PhaseShiftClientController;
import client.listeners.PhaseShiftListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jspace.RemoteSpace;

import java.io.IOException;

public class PhaseShiftViewer {

    private Scene scene;
    private RemoteSpace clientSpace;
    private VBox root;

    public PhaseShiftViewer(RemoteSpace clientSpace) {
        this.clientSpace = clientSpace;
        try {
            FXMLLoader phaseShiftClientLoader = new FXMLLoader(PhaseShiftClientController.class.getResource("/client-phase-shift.fxml"));
            root = phaseShiftClientLoader.load();
            PhaseShiftClientController phaseShiftClientController = phaseShiftClientLoader.getController();
            scene = new Scene(root);

            Thread phaseShfitListenerThread = new Thread(new PhaseShiftListener(phaseShiftClientController.lineGraphs, this.clientSpace));
            phaseShfitListenerThread.setDaemon(true);
            phaseShfitListenerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startPhaseShiftClientViewer(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
