package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Slider;
import main.application.Server;
import main.components.Wave;
import main.datatypes.FrequencyGraph;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class WaveWithFrequencyController implements Initializable {
    private int range = 1;

    @FXML
    private LineChart<Double, Double> lineGraph;

    @FXML
    private Slider frequencySlider;

    private FrequencyGraph graph;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = new FrequencyGraph(lineGraph, range);

        // Initialize the bottom splitpane with a complicated wave
        Wave wave1 = new Wave(1);
        Wave wave2 = new Wave(3);
        Wave wave3 = new Wave(5);
        Wave wave4 = new Wave(8);
        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));

        // Sum the waves and plot the function
        graph.plot(Wave.sumWaves(waveResult));

        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            handleSlider(newValue.doubleValue());
        });
    }

    private void handleSlider(double value) {
        try {
            Server.space.put("Frequency value on client: " + value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
