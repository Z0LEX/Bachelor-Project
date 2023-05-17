package main.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import main.application.StageAwareController;
import main.application.StageManager;
import main.components.CombinationLock;
import main.components.Wave;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FourierMachineController implements Initializable, StageAwareController {

    @FXML
    private LineChart<Double, Double> inputGraph;

    @FXML
    private AreaChart<Double, Double> outputArea;

    @FXML
    private LineChart<Double, Double> testGraph;

    @FXML
    private StackedBarChart<String, Number> frequencyBarChart;

    @FXML
    private Slider frequencySlider;

    @FXML
    private HBox lockContainer;

    @FXML
    private Button lockButton;

    private CombinationLock lock = new CombinationLock(1, 3, 5, 8);

    private XYChart.Series<Double, Double> aboveZeroSeries;
    private XYChart.Series<Double, Double> belowZeroSeries;

    private int[] solutionArray = new int[4];
    private int[] suggestionArray = new int[4];
    private boolean gameWon;
    private StageManager stageManager;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data("0", 1));

        XYChart.Series series2 = new XYChart.Series();
        series2.getData().add(new XYChart.Data("0", 1));

        XYChart.Series series3 = new XYChart.Series();
        series3.getData().add(new XYChart.Data("0", 1));

        XYChart.Series series4 = new XYChart.Series();
        series4.getData().add(new XYChart.Data("0", 1));

        CategoryAxis xAxis = (CategoryAxis) frequencyBarChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList(
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        ));

        frequencyBarChart.getData().addAll(series1, series2, series3, series4);

        lockContainer.getChildren().add(lock.getRoot());

        lockButton.setOpacity(0);

        lockButton.setOnAction(actionEvent -> {
            stageManager.setScene("/phase-shift.fxml");
        });


        // Update solution array
        solutionArray[0] = lock.getFirst();
        solutionArray[1] = lock.getSecond();
        solutionArray[2] = lock.getThird();
        solutionArray[3] = lock.getFourth();


        lock.getController().getWheel1Number().textProperty().addListener((observableValue, s, newValue) -> {
            series1.getData().clear();
            series1.getData().add(new XYChart.Data(newValue, 1));

            suggestionArray[0] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel2Number().textProperty().addListener((observableValue, s, newValue) -> {
            series2.getData().clear();
            series2.getData().add(new XYChart.Data(newValue, 1));

            suggestionArray[1] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel3Number().textProperty().addListener((observableValue, s, newValue) -> {
            series3.getData().clear();
            series3.getData().add(new XYChart.Data(newValue, 1));

            suggestionArray[2] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel4Number().textProperty().addListener((observableValue, s, newValue) -> {
            series4.getData().clear();
            series4.getData().add(new XYChart.Data(newValue, 1));

            suggestionArray[3] = Integer.parseInt(newValue);
            checkSolution();
        });

        // Create input wave
        Wave wave1 = new Wave(lock.getFirst(), 1);
        Wave wave2 = new Wave(lock.getSecond(), 1);
        Wave wave3 = new Wave(lock.getThird(), 1);
        Wave wave4 = new Wave(lock.getFourth(), 1);
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

        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newFrequency = newValue.doubleValue();
            updateTestWave(newFrequency, testWave);
            updateOutputWave(inputWave, testWave);
        });
    }

    private void checkSolution() {
        suggestionArray[0] = Integer.parseInt(lock.getController().getWheel1Number().getText());
        suggestionArray[1] = Integer.parseInt(lock.getController().getWheel2Number().getText());
        suggestionArray[2] = Integer.parseInt(lock.getController().getWheel3Number().getText());
        suggestionArray[3] = Integer.parseInt(lock.getController().getWheel4Number().getText());
        Arrays.sort(suggestionArray);
        if (Arrays.equals(suggestionArray, solutionArray)) {
            gameWon = true;
        } else {
            gameWon = false;
        }
        if (gameWon == true) {
            lockButton.setDisable(false);
            lockButton.setOpacity(1);
        } else {
            lockButton.setDisable(true);
            lockButton.setOpacity(0);
        }
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

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
