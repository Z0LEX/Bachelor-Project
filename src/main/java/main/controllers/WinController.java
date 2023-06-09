package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WinController {

    @FXML
    void handleCloseButton(ActionEvent event) {
        Platform.exit();
    }
}
