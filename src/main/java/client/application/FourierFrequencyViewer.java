package client.application;

import client.controllers.FourierFrequencyController;
import client.listeners.FrequencyListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jspace.RemoteSpace;

import java.io.IOException;

public class FourierFrequencyViewer {
    private Scene scene;
    private RemoteSpace clientSpace;
    private AnchorPane root;

    public FourierFrequencyViewer(RemoteSpace clientSpace) {
        this.clientSpace = clientSpace;
        try {
            FXMLLoader fourierFrequencyLoader = new FXMLLoader(FourierFrequencyController.class.getResource("/client-fourier-frequency.fxml"));
            root = fourierFrequencyLoader.load();
            FourierFrequencyController fourierFrequencyController = fourierFrequencyLoader.getController();
            scene = new Scene(root);

            Thread frequencyListenerThread = new Thread(new FrequencyListener(fourierFrequencyController.lineChart, this.clientSpace));
            frequencyListenerThread.setDaemon(true);
            frequencyListenerThread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startFourierFrequencyViewer(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
