package client.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientApplication extends Application {
    private static final String PORT = ":9001";
    private static final String PROTOCOL = "tcp://";
    private String ip = "127.0.0.1";

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        stage.setTitle("Client title");
        stage.setResizable(false);
        Rectangle2D bounds = Screen.getScreens().get(1).getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
//        stage.setFullScreen(true);
        stage.centerOnScreen();


        String clientURI = PROTOCOL + ip + PORT + "/space?keep";
        System.out.println(clientURI);
        String test = "NOT SET";
        try {
            RemoteSpace clientSpace = new RemoteSpace(clientURI);
            System.out.println(clientSpace.getUri());
            Object[] objects = clientSpace.getp(new FormalField(String.class));
            System.out.println(objects);
            if (objects != null) {
                test = (String) objects[0];
                System.out.println(objects[0]);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Temp - add label to pane & create scene
        Label label = new Label(test);
        BorderPane root = new BorderPane();
        root.setCenter(label);
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    // Get the outgoing IP address
    private static String getIp() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}