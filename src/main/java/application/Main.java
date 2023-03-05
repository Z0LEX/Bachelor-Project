package application;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
        EulerViewer eulerViewer = new EulerViewer();
        Parent graphRoot = graphViewer.getRoot();
        Parent eulerRoot = eulerViewer.getRoot();
        BorderPane pane = new BorderPane();
        pane.setLeft(graphRoot);
        pane.setRight(eulerRoot);
        stage.setScene(new Scene(pane));
        stage.show();
    }
}