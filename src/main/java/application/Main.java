package application;

import controllers.TestLayout;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) throws InterruptedException {
        Space inbox = new SequentialSpace();
        inbox.put("Hello World!", false);
        Object[] tuple = inbox.get(new FormalField(String.class), new ActualField(false));
        System.out.println(tuple[0] + " " + tuple[1]);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(TestLayout.class.getResource("/test-layout.fxml"));
        try {
            AnchorPane layout = loader.load();
            TestLayout test = loader.getController();
            test.knap1.setOnAction(e -> {
                test.testfunktion();
            });

            primaryStage.setScene(new Scene(layout));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        primaryStage.setTitle("Hello World!");
        primaryStage.show();
    }
}