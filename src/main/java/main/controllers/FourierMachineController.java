package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import main.components.CombinationLock;
import main.components.Wave;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FourierMachineController implements Initializable {

    @FXML
    private LineChart<Double, Double> inputGraph;

    @FXML
    private AreaChart<Double, Double> outputArea;

    @FXML
    private LineChart<Double, Double> testGraph;

    @FXML
    private Text purpleText;

    @FXML
    private Text redText;

    @FXML
    private Text blueText;

    @FXML
    private Slider frequencySlider;

    @FXML
    private HBox lockContainer;

    private CombinationLock lock = new CombinationLock(1, 3, 5 ,8);

    private XYChart.Series<Double, Double> aboveZeroSeries;
    private XYChart.Series<Double, Double> belowZeroSeries;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lockContainer.getChildren().add(lock.getRoot());

        // Create input wave
        Wave wave1 = new Wave(1, 1);
        Wave wave2 = new Wave(3, 1);
        Wave wave3 = new Wave(5, 1);
        Wave wave4 = new Wave(8, 1);
        ArrayList<Wave> inputWaves = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));
        Wave inputWave = new Wave(Wave.sumWaves(inputWaves), inputGraph);
        
        // Initial testwave
        Wave testWave = new Wave(1, 1, testGraph);

        // Resulting output wave
        Wave outputWave = new Wave(Wave.multiplyWaves(inputWave, testWave));

        // Split the datapoints into 2 series, below and above zero.
        aboveZeroSeries = new XYChart.Series<>();
        belowZeroSeries = new XYChart.Series<>();
        updateOutput(outputWave);

        updateTextFields();

        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newFrequency = newValue.doubleValue();
            updateTestWave(newFrequency, testWave);
            updateOutputWave(inputWave, testWave);
            updateTextFields();
        });
    }

    private void updateTextFields() {
        double aboveSum = getSeriesSum(aboveZeroSeries);
        blueText.setText(String.format("%.1f", aboveSum));
        double belowSum = getSeriesSum(belowZeroSeries);
        redText.setText(String.format("(%.1f)", belowSum));
        double result = aboveSum + belowSum;
        if (Math.abs(result) < 0.001) {
            result = 0;
        }
        purpleText.setText(String.format("%.1f", result));
    }

    private void updateOutputWave(Wave inputWave, Wave testWave) {
        Wave outputWave = new Wave(Wave.multiplyWaves(inputWave, testWave));
        outputArea.getData().clear();
        aboveZeroSeries.getData().clear();
        belowZeroSeries.getData().clear();

        updateOutput(outputWave);
    }

    private void updateOutput(Wave outputWave) {
        XYChart.Series<Double, Double> series = outputWave.getSeries();
        for (XYChart.Data<Double, Double> data : series.getData()) {
            if (data.getYValue() > 0) {
                aboveZeroSeries.getData().add(data);
            } else {
                belowZeroSeries.getData().add(data);
            }
        }
        outputArea.getData().addAll(aboveZeroSeries, belowZeroSeries);
        aboveZeroSeries.getNode().lookup(".chart-series-area-fill").setStyle("-fx-fill: #005A92;");
        belowZeroSeries.getNode().lookup(".chart-series-area-fill").setStyle("-fx-fill: #B22222;");
    }

    private double getSeriesSum(XYChart.Series<Double, Double> series) {
        double sum = 0;
        for (XYChart.Data<Double, Double> d : series.getData()) {
            sum += d.getYValue();
        }
        return sum;
    }

    private void updateTestWave(double newFrequency, Wave wave) {
        wave.clear();
        wave.setFunction(x -> wave.getWave(x, newFrequency, wave.getAmplitude(), wave.getPhaseShift()));
        wave.plotFunction(wave.getFunction());
    }
}
