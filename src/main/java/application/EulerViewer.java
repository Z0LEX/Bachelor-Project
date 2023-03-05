package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EulerViewer {
    private Scene scene;
    private Parent root;

    public EulerViewer() {
        createEulerViewer();
    }

    private void createEulerViewer() {
        try {
            root = FXMLLoader.load(getClass().getClassLoader()
                    .getResource("euler-viewer.fxml"));
            scene = new Scene(root);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void startEulerViewer(Stage stage) {
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
