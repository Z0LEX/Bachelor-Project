package client.controllers;

import client.application.ClientStageManager;
import client.application.SpaceAwareController;
import client.listeners.PhaseShiftListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import org.jspace.Space;

import java.net.URL;
import java.util.ResourceBundle;

public class PhaseShiftClientController implements SpaceAwareController, Initializable {

    @FXML
    public LineChart<Double, Double> lineGraphs;
    private Space space;
    private ClientStageManager stageManager;


    @Override
    public void setSpace(Space space) {
        this.space = space;
    }

    public void startListener() {
        Thread phaseShfitListenerThread = new Thread(new PhaseShiftListener(lineGraphs, space, stageManager));
        phaseShfitListenerThread.setDaemon(true);
        phaseShfitListenerThread.start();
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
