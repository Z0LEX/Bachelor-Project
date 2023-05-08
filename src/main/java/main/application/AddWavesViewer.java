package main.application;

import main.controllers.AddWavesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AddWavesViewer {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public AddWavesViewer(Stage stage) {
        this.stage=stage;
        createAddWavesViewer();
    }

    private void createAddWavesViewer() {
        try {
            FXMLLoader addWavesLoader = new FXMLLoader(AddWavesController.class.getResource("/add-waves.fxml"));
            root = addWavesLoader.load();
            AddWavesController addWavesController = addWavesLoader.getController();
//            addWavesController.setStage(stage);
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
