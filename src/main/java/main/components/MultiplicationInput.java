package main.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import main.controllers.MultiplicationInputController;

import java.io.IOException;

public class MultiplicationInput {
    private HBox root;
    private MultiplicationInputController controller;

    public MultiplicationInput() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/multiplication-input.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HBox getRoot() {
        return root;
    }
}
