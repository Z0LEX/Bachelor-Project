package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class WaveController {


    @FXML
    private LineChart<Double, Double> lineGraph;

    public LineChart<Double, Double> getLineGraph() {
        return lineGraph;
    }
}