package main.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.DrawGraphController;

import java.io.IOException;

public class DrawGraphViewer {

    private Scene scene;
    private Parent root;

    public DrawGraphViewer() {
        createDrawGraph();
    }

    private void createDrawGraph() {
        try {
            FXMLLoader drawGraphLoader = new FXMLLoader(DrawGraphController.class.getResource("/draw-graph.fxml"));
            root = drawGraphLoader.load();
            DrawGraphController drawGraphController = drawGraphLoader.getController();
            scene = new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startDrawGraphViewer(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
