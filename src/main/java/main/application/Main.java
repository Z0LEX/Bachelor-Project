package main.application;

import client.application.ClientApplication;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class Main extends Application {
    // Screen resolution of touchscreen
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private StageManager stageManager;
    public Stage clientStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setTitle("Title");
        stage.setResizable(false);
        // Set stage dimensions to a fixed size if necessary
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        ObservableList<Screen> screens = Screen.getScreens();
        if (screens.size() > 1) {
            stage.setMaximized(true);
            stage.initStyle(StageStyle.DECORATED);
        }
        stage.centerOnScreen();


        // Initialize StageManager with all FXML paths
        stageManager = new StageManager(stage, "/add-waves.fxml", "/add-waves-amplitude.fxml", "/fourier-machine-multiplication.fxml", "/fourier-machine.fxml", "/phase-shift.fxml", "/draw-graph.fxml", "/win.fxml");
        stageManager.setScene("/add-waves.fxml");

        Server server = new Server();

        ArrayList<Stage> stages = new ArrayList<>();
        clientStage = new Stage();
        stages.add(stage);
        stages.add(clientStage);

        setCloseAllStagesOnExit(stages);

        new ClientApplication().start(clientStage);
    }

    // Loop through all stages and add listener to each that closes all stages
    private void setCloseAllStagesOnExit(ArrayList<Stage> stages) {
        for (Stage s : stages) {
            s.setOnCloseRequest(event -> {
                for (Stage newStage : stages) {
                    newStage.close();
                }
            });
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}