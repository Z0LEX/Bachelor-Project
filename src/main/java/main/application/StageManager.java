package main.application;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.controllers.AddWavesController;
import main.controllers.PhaseShiftController;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class StageManager {

    private Stage stage;
    private Scene scene;
    private BorderPane root = new BorderPane();
    private Map<String, Parent> containers = new HashMap<>();
    private Map<String, Object> controllers = new HashMap<>();

    public StageManager(Stage stage, String... fxmlPaths) {
        this.stage = stage;
        scene = new Scene(root);
        stage.setScene(scene);

        for (String fxmlPath : fxmlPaths) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                loader.setControllerFactory(param -> {
                    try {
                        Object controller = param.getDeclaredConstructor().newInstance();
                        if (controller instanceof StageAwareController) {
                            ((StageAwareController) controller).setStageManager(this);
                        }
                        controllers.put(fxmlPath, controller);
                        return controller;
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
                containers.put(fxmlPath, loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        stage.show();
    }

    public void setScene(String fxmlPath) {
        Parent container = containers.get(fxmlPath);
        if (container == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                containers.put(fxmlPath, loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        root.setCenter(container);
    }

    public void test() {
        System.out.println(controllers.toString());
    }
}
