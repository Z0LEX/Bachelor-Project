package controllers;

import datatypes.EulerGraph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class EulerController implements Initializable {

    @FXML
    private ScatterChart<Double, Double> scatterChart;
    @FXML
    private Slider frequencySlider;

    private EulerGraph eulerGraph;

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        assert frequencySlider != null : "fx:id=\"frequencySlider\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            handleSlider(newValue.doubleValue());
        });
        eulerGraph = new EulerGraph(scatterChart);
    }

    private void plotFunction(Function<Double, Double> function) {
        eulerGraph.plot(function);
    }

    private void handleSlider(double value) {
        clear();
        eulerGraph.setFrequency(value);
        plotFunction(eulerGraph.getFunction());
    }

    private void clear() {
        eulerGraph.clear();
    }
}
