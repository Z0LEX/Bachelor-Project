package main.application;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import client.application.ClientApplication;
public class Main extends Application {

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

        // Temp scene
        BorderPane pane = new BorderPane();
        pane.setCenter(addWavesRoot);
        stage.setScene(new Scene(pane));
        stage.show();


        ClientApplication clientApplication = new ClientApplication();
        clientApplication.start(new Stage());
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}