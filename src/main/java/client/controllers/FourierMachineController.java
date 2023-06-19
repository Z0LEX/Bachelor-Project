package client.controllers;

import client.application.ClientStageManager;
import client.application.SpaceAwareController;
import client.listeners.FourierMachineListener;
import javafx.fxml.Initializable;
import org.jspace.Space;

import java.net.URL;
import java.util.ResourceBundle;

public class FourierMachineController implements SpaceAwareController, Initializable {

    private Space space;
    private ClientStageManager stageManager;

    @Override
    public void setSpace(Space space) {
        this.space = space;
    }

    @Override
    public void startListener() {
        Thread fourierMachineThread = new Thread(new FourierMachineListener(space, stageManager));
        fourierMachineThread.setDaemon(true);
        fourierMachineThread.start();
    }

    @Override
    public void setStageManager(ClientStageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startListener();
    }
}
