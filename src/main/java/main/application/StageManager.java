package main.application;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Arrays;


public class StageManager {

    private Scene scene;
    private BorderPane root = new BorderPane();
    private Map<String, Parent> containers = new HashMap<>();
    private Map<String, Object> controllers = new HashMap<>();
    private String[] paths;
    private String activeScene = "";

    public StageManager(Stage stage, String... fxmlPaths) {
        // testing purposes
        paths = Arrays.copyOf(fxmlPaths, fxmlPaths.length);

        for (String fxmlPath : fxmlPaths) {
            try {
                // Get the loader for the controller class associated with the given FXML path string
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                // Create a new instance of the controller and set its stage manager
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
                // Load the FXML file and add the root to the map
                Parent parent = loader.load();
                containers.put(fxmlPath, parent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Add next and prev buttons
        setupTempScene();

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setScene(String fxmlPath) {
        // Temp variable for testing
        activeScene = fxmlPath;

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

    public void setupTempScene() {
        // Temp scene
        Button nextScreenButton = new Button("Next");
        nextScreenButton.alignmentProperty().set(Pos.CENTER);
        nextScreenButton.setOnAction(actionEvent -> {
            // Find the index of the active scene
            int activeIndex = getActiveIndex();
            // Change the scene to the parent associated with that scene and change active scene
            if (activeIndex < paths.length - 1) {
                activeScene = paths[activeIndex + 1];
                root.setCenter(containers.get(activeScene));
            }
        });
        Button prevScreenButton = new Button("Previous");
        prevScreenButton.setOnAction(actionEvent -> {
            int activeIndex = getActiveIndex();
            if (activeIndex > 0) {
                activeScene = paths[activeIndex - 1];
                root.setCenter(containers.get(activeScene));
            }
        });
        root.setLeft(prevScreenButton);
        root.setRight(nextScreenButton);
    }

    private int getActiveIndex() {
        int activeIndex = -1;
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].equals(activeScene)) {
                activeIndex = i;
            }
        }
        return activeIndex;
    }
}
