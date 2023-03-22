package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Slider;
import main.application.Server;
import main.components.Wave;
import main.datatypes.ComplexNumber;
import main.datatypes.EulerGraph;
import main.datatypes.FrequencyGraph;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

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
        // Each wave should have a unique frequency (if two have the same the range on the y-axis on client is too low)
        Wave wave1 = new Wave(1);
        Wave wave2 = new Wave(3);
        Wave wave3 = new Wave(5);
        Wave wave4 = new Wave(8);

        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));

        // Sum the waves and plot the function
        Function<Double, Double> sumFunction = Wave.sumWaves(waveResult);
        graph.plot(sumFunction);
        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newFrequency = newValue.doubleValue();
            // Compute the weight of the graph in the complex plane
            EulerGraph eulerGraph = new EulerGraph(sumFunction, newFrequency);
            ComplexNumber weight = eulerGraph.computeWeight();
            handleSlider(weight);
        });
        // Initialize client with point in (0,0)
        sendToClient(0);
    }

    private void handleSlider(ComplexNumber weight) {
        // Client only needs the real part of the weight
        sendToClient(weight.re());
    }

    private static void sendToClient(double centerOfMassY) {
        try {
            Server.space.put("Wave center of mass", centerOfMassY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}