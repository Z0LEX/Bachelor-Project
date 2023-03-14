package components;

import controllers.CombinationLockController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CombinationLock {
    private HBox root;
    private CombinationLockController controller;

    public CombinationLock() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/combination-lock.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CombinationLockController getController() {
        return controller;
    }

    public HBox getRoot() {
        return root;
    }
}
