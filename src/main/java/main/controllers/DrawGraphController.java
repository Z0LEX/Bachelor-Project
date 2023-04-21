package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import main.components.Wave;
import main.datatypes.FrequencyGraph;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

public class DrawGraphController implements Initializable {

    @FXML
    private ScatterChart<Double, Double> scatterChart;

    private FrequencyGraph graph;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = new FrequencyGraph(scatterChart, 1);
        Wave wave = new Wave(3, 5);
        ArrayList<Integer> missingPoints = new ArrayList();
        missingPoints.add(20);
        missingPoints.add(30);
        missingPoints.add(60);
        graph.plotIncomplete(wave.getFunction(), 0.015, missingPoints);
    }
}
