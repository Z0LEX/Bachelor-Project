package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import datatypes.FrequencyGraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;


public class GraphController implements Initializable {
    private final static int AMPLITUDE = 1;

    @FXML
    private LineChart<Double, Double> lineGraph;

    @FXML
    private Button oneHertz;
    @FXML
    private Button twoHertz;

    @FXML
    private Button combine;

    @FXML
    private Button squaredButton;

    @FXML
    private Button squaredButton2;

    @FXML
    private Button cubedButton;

    @FXML
    private Button clearButton;

    @FXML
    private Slider frequencySlider;
    private FrequencyGraph frequencyGraph;

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert combine != null : "fx:id=\"combine\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert cubedButton != null : "fx:id=\"cubedButton\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert lineGraph != null : "fx:id=\"lineGraph\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert oneHertz != null : "fx:id=\"oneHertz\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert squaredButton != null : "fx:id=\"squaredButton\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert squaredButton2 != null : "fx:id=\"squaredButton2\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert twoHertz != null : "fx:id=\"twoHertz\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert frequencySlider != null : "fx:id=\"frequencySlider\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            handleSlider(newValue.doubleValue());
        });
        frequencyGraph = new FrequencyGraph(lineGraph, 5);
    }

    private void plotFunction(Function<Double, Double> function) {
        frequencyGraph.plot(function);
    }

    private void combinePlots() {
        frequencyGraph.combinePlots();
    }

    @FXML
    private void handleOneHertz(final ActionEvent event) {
        plotFunction(x -> getFrequency(1, x, 0));
        oneHertz.setDisable(true);
    }

    @FXML
    private void handleTwoHertz(final ActionEvent event) {
        plotFunction(x -> getFrequency(2, x, 0));
        twoHertz.setDisable(true);
    }

    @FXML
    private void handleCombineButton(final ActionEvent event) {
        combinePlots();
        combine.setDisable(true);
    }

    @FXML
    private void handleSquared(final ActionEvent event) {
        plotFunction(x -> getFrequency(3, x, 0));
    }

    @FXML
    private void handleSquaredPlus2(final ActionEvent event) {
        plotFunction(x -> Math.pow(x, 2) + 2);
    }

    @FXML
    private void handleCubed(final ActionEvent event) {
        plotFunction(x -> Math.pow(x, 3));
    }


    @FXML
    private void handleClear(final ActionEvent event) {
        clear();
    }

    private void handleSlider(double value) {
        clear();
        plotFunction(x -> getFrequency(value, x, 1));
    }

    private void clear() {
        frequencyGraph.clear();
        oneHertz.setDisable(false);
        twoHertz.setDisable(false);
        combine.setDisable(false);
    }

    private static double getFrequency(int f, double x, int offset) {
        return AMPLITUDE * Math.sin(2 * Math.PI * f * x) + offset;
    }

    private static double getFrequency(double f, double x, int offset) {
        return AMPLITUDE * Math.sin(2 * Math.PI * f * x) + offset;
    }
}