package client.application;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientApplication extends Application {
    private static final String PORT = ":9001";
    private static final String PROTOCOL = "tcp://";
    private String ip = "127.0.0.1";

    public static void main(String[] args) {
        launch(args);
    }

    private RemoteSpace clientSpace;

    private int screenIndex = 0;

    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Client title");
//        stage.setResizable(false);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        // Only place client application on 2nd monitor if there's more than 1 screen
        ObservableList<Screen> screens = Screen.getScreens();
        if (screens.size() > 1) {
            Rectangle2D bounds = screens.get(1).getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
        }
        stage.setMaximized(true);
        stage.centerOnScreen();


        String clientURI = PROTOCOL + ip + PORT + "/space?keep";
        try {
            clientSpace = new RemoteSpace(clientURI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PhaseShiftViewer phaseShiftViewer = new PhaseShiftViewer(clientSpace);
        Parent phaseShiftRoot = phaseShiftViewer.getRoot();

        ArrayList<Parent> parents = new ArrayList<>();
        parents.add(phaseShiftRoot);

//        BorderPane tempScene = setupTempScene(parents);
//        stage.setScene(new Scene(tempScene));

        BorderPane pane = new BorderPane();
        pane.setCenter(phaseShiftRoot);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    private BorderPane setupTempScene(ArrayList<Parent> parents) {
        // Temp scene
        BorderPane pane = new BorderPane();
        pane.setCenter(parents.get(screenIndex));
        Button nextScreenButton = new Button("Next");
        nextScreenButton.alignmentProperty().set(Pos.CENTER);
        nextScreenButton.setOnAction(actionEvent -> {
            if (screenIndex < parents.size() - 1) {
                screenIndex++;
                pane.setCenter(parents.get(screenIndex));
            }
        });
        Button prevScreenButton = new Button("Previous");
        prevScreenButton.setOnAction(actionEvent -> {
            if (screenIndex > 0) {
                screenIndex--;
                pane.setCenter(parents.get(screenIndex));
            }
        });
        pane.setLeft(prevScreenButton);
        pane.setRight(nextScreenButton);
        return pane;
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
