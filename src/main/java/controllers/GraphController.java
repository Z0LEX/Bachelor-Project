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
    private final static double AMPLITUDE = 1;
    private final static int OFFSET = 0;
    public static final double PHASE_SHIFT = Math.PI / 2;

    @FXML
    private LineChart<Double, Double> lineGraph;

    @FXML
    private Button oneHertz;
    @FXML
    private Button twoHertz;

    @FXML
    private Button threeHertz;

    @FXML
    private Button fourHertz;

    @FXML
    private Button fiveHertz;
    @FXML
    private Button combine;
    @FXML
    private Button clearButton;

    @FXML
    private Slider frequencySlider;
    private FrequencyGraph frequencyGraph;

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert combine != null : "fx:id=\"combine\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert threeHertz != null : "fx:id=\"threeHertz\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert lineGraph != null : "fx:id=\"lineGraph\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert oneHertz != null : "fx:id=\"oneHertz\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert fourHertz != null : "fx:id=\"fourHertz\" was not injected: check your FXML file 'graph-viewer.fxml'.";
        assert fiveHertz != null : "fx:id=\"fiveHertz\" was not injected: check your FXML file 'graph-viewer.fxml'.";
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
        plotFunction(x -> getFrequency(1, x, OFFSET));
    }

    @FXML
    private void handleTwoHertz(final ActionEvent event) {
        plotFunction(x -> getFrequency(2, x, OFFSET));
    }

    @FXML
    private void handleThreeHertz(final ActionEvent event) {
        plotFunction(x -> getFrequency(3, x, OFFSET));
    }

    @FXML
    private void handleFourHertz(final ActionEvent event) {
        plotFunction(x -> getFrequency(4, x, OFFSET));
    }

    @FXML
    private void handleFiveHertz(final ActionEvent event) {
        plotFunction(x -> getFrequency(5, x, OFFSET));
    }

    @FXML
    private void handleCombineButton(final ActionEvent event) {
        combinePlots();
//        frequencyGraph.clear();
        combine.setDisable(true);
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        clear();
    }

    private void handleSlider(double value) {
        clear();
        plotFunction(x -> getFrequency(value, x, OFFSET));
    }

    private void clear() {
        frequencyGraph.clear();
        oneHertz.setDisable(false);
        twoHertz.setDisable(false);
        combine.setDisable(false);
    }

    private static double getFrequency(double f, double x, int offset) {
        return AMPLITUDE * Math.sin(2 * Math.PI * f * x + PHASE_SHIFT) + offset;
    }
}