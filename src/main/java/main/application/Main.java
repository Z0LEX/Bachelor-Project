package main.application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import client.application.ClientApplication;

import java.util.ArrayList;

public class Main extends Application {

    private int screenIndex = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Title");
        stage.setResizable(false);
        stage.centerOnScreen();

        Server server = new Server();

        // TODO: Remove GraphViewer, most functionality has been moved to AddWaves
        GraphViewer graphViewer = new GraphViewer();
        Parent graphRoot = graphViewer.getRoot();
        AddWavesViewer addWavesViewer = new AddWavesViewer();
        Parent addWavesRoot = addWavesViewer.getRoot();

        ArrayList<Parent> screens = new ArrayList<>();
        screens.add(addWavesRoot);
        screens.add(graphRoot);

        // Temp scene
        BorderPane pane = new BorderPane();
        pane.setCenter(addWavesRoot);
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

        stage.setScene(new Scene(pane));
        stage.show();

        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(stage);
        Stage clientStage = new Stage();
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