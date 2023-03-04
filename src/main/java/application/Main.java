package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Title");
        stage.setResizable(false);
        GraphViewer graphViewer = new GraphViewer();
        graphViewer.startGraphViewer(stage);
    }
}