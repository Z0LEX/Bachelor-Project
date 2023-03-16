package client.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

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

    private RemoteSpace clientSpace;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Client title");
        stage.setResizable(false);
        ObservableList<Screen> screens = Screen.getScreens();
        if (screens.size() > 1) {
            Rectangle2D bounds = screens.get(1).getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
        }
//        stage.setFullScreen(true);
        stage.centerOnScreen();


        String clientURI = PROTOCOL + ip + PORT + "/space?keep";
        System.out.println(clientURI);
        String test = "NOT SET";
        try {
            clientSpace = new RemoteSpace(clientURI);
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

        Thread frequencyListenerThread = new Thread(new FrequencyListener(label, clientSpace));
        frequencyListenerThread.setDaemon(true);
        frequencyListenerThread.start();

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

class FrequencyListener implements Runnable {

    private Label label;
    private Space space;

    public FrequencyListener(Label label, Space space) {
        this.label = label;
        this.space = space;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] objects = space.get(new FormalField(String.class));
                Platform.runLater(() -> {
                    label.setText((String) objects[0]);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
