package main.application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import client.application.ClientApplication;
import main.datatypes.Print;

import java.util.ArrayList;

public class Main extends Application {
    // Screen resolution of touchscreen
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private int screenIndex = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Title");
        stage.setResizable(false);
        // Set stage dimensions to a fixed size if necessary
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.centerOnScreen();

        Server server = new Server();

        AddWavesViewer addWavesViewer = new AddWavesViewer(stage);
        Parent addWavesRoot = addWavesViewer.getRoot();

        AddWavesAmplitudeViewer addWavesAmplitudeViewer = new AddWavesAmplitudeViewer(stage);
        Parent addWavesAmplitudeRoot = addWavesAmplitudeViewer.getRoot();

        PhaseShiftViewer phaseShiftViewer = new PhaseShiftViewer(stage);
        Parent phaseShiftRoot = phaseShiftViewer.getRoot();

        FourierMachineViewer fourierMachineViewer = new FourierMachineViewer(stage);
        Parent fourierMachineRoot = fourierMachineViewer.getRoot();

        DrawGraphViewer drawGraphViewer = new DrawGraphViewer();
        Parent drawGraphRoot = drawGraphViewer.getRoot();

        ArrayList<Parent> screens = new ArrayList<>();
        screens.add(addWavesRoot);
        screens.add(addWavesAmplitudeRoot);
        screens.add(drawGraphRoot);
        screens.add(fourierMachineRoot);
        screens.add(phaseShiftRoot);

        BorderPane tempScene = setupTempScene(screens);

        stage.setScene(new Scene(tempScene));
        stage.show();

        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(stage);
        Stage clientStage = new Stage();
        stages.add(clientStage);

        setCloseAllStagesOnExit(stages);
        new ClientApplication().start(clientStage);
    }

    public BorderPane setupTempScene(ArrayList<Parent> screens) {
        // Temp scene
        BorderPane pane = new BorderPane();
        pane.setCenter(screens.get(screenIndex));
        Button nextScreenButton = new Button("Next");
        nextScreenButton.alignmentProperty().set(Pos.CENTER);
        nextScreenButton.setOnAction(actionEvent -> {
            if (screenIndex < screens.size() - 1) {
                screenIndex++;
                pane.setCenter(screens.get(screenIndex));
            }
        });
        Button prevScreenButton = new Button("Previous");
        prevScreenButton.setOnAction(actionEvent -> {
            if (screenIndex > 0) {
                screenIndex--;
                pane.setCenter(screens.get(screenIndex));
            }
        });
        pane.setLeft(prevScreenButton);
        pane.setRight(nextScreenButton);
        return pane;
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