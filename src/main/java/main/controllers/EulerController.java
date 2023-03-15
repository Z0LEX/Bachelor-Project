package main.controllers;

import main.datatypes.EulerGraph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class EulerController implements Initializable {

    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
    private Slider frequencySlider;

    private EulerGraph eulerGraph;

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        assert lineChart != null : "fx:id=\"lineChart\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert frequencySlider != null : "fx:id=\"frequencySlider\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            handleSlider(newValue.doubleValue());
        });
        eulerGraph = new EulerGraph(lineChart);
    }

    private void plotFunction(Function<Double, Double> function) {
        eulerGraph.plot(function);
    }

    private void handleSlider(double value) {
        clear();
        eulerGraph.setCyclesPrSec(value);
        plotFunction(eulerGraph.getFunction());
    }

    private void clear() {
        eulerGraph.clear();
    }
}
