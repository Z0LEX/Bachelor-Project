package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class WinController {
    @FXML
    void handleCloseButton(ActionEvent event) {
        closeApplication();
    }

    @FXML
    void handleRestartButton(ActionEvent event) {
        String directory = Paths.get("")
                .toAbsolutePath()
                .toString();
        ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList("cmd.exe", "/C", directory + "/run.bat"));
        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        closeApplication();
    }

    private void closeApplication() {
        Platform.exit();
    }
}
