package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphViewer {

    private Scene scene;
    public GraphViewer() {
        createGraphViewer();
    }

    private void createGraphViewer() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader()
                    .getResource("graph-viewer.fxml"));
            scene = new Scene(root);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void startGraphViewer(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
