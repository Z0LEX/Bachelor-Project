package main.controllers;

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
import javafx.scene.text.Text;

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
    private Text purpleText;

    @FXML
    private Text redText;

    @FXML
    private Text blueText;

    @FXML
    private Slider frequencySlider;

    @FXML
    private HBox lockContainer;

    @FXML
    private Button lockButton;

    @FXML
    private BarChart barChart;

    private CombinationLock lock = new CombinationLock(1, 3, 5, 8);

    private XYChart.Series<Double, Double> aboveZeroSeries;
    private XYChart.Series<Double, Double> belowZeroSeries;

    private int[] solutionArray = new int[4];
    private int[] suggestionArray = new int[4];
    private boolean gameWon;
    private StageManager stageManager;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lockContainer.getChildren().add(lock.getRoot());




        lockButton.setOpacity(0);

        lockButton.setOnAction(actionEvent -> {
            stageManager.setScene("/phase-shift.fxml");
        });

        final String f1 = "1";
        final String f2 = "2";
        final String f3 = "3";
        final String f4 = "4";
        final String f5 = "5";
        final String f6 = "6";
        final String f7 = "7";
        final String f8 = "8";
        final String f9 = "9";

        XYChart.Series frequencies = new XYChart.Series();
        frequencies.getData().add(new XYChart.Data(f1, 0));
        frequencies.getData().add(new XYChart.Data(f2, 0));
        frequencies.getData().add(new XYChart.Data(f3, 0));
        frequencies.getData().add(new XYChart.Data(f4, 0));
        frequencies.getData().add(new XYChart.Data(f5, 0));
        frequencies.getData().add(new XYChart.Data(f6, 0));
        frequencies.getData().add(new XYChart.Data(f7, 0));
        frequencies.getData().add(new XYChart.Data(f8, 0));
        frequencies.getData().add(new XYChart.Data(f9, 0));

        barChart.getData().add(frequencies);

        String.valueOf(lock.getFirst());

        XYChart.Series solution1 = new XYChart.Series();
        //solution1.getData().add(new XYChart.Data( String.valueOf(lock.getFirst()), 1));

        XYChart.Series solution2 = new XYChart.Series();
        //solution2.getData().add(new XYChart.Data(String.valueOf(lock.getSecond()), 1));

        XYChart.Series solution3 = new XYChart.Series();
        //solution3.getData().add(new XYChart.Data(String.valueOf(lock.getThird()), 1));

        XYChart.Series solution4 = new XYChart.Series();
        //solution4.getData().add(new XYChart.Data(String.valueOf(lock.getFourth()), 1));

        barChart.getData().addAll(solution1, solution2, solution3, solution4);

        // Update solution array
        solutionArray[0] = lock.getFirst();
        solutionArray[1] = lock.getSecond();
        solutionArray[2] = lock.getThird();
        solutionArray[3] = lock.getFourth();


        lock.getController().getWheel1Number().textProperty().addListener((observableValue, s, newValue) -> {
            suggestionArray[0] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel2Number().textProperty().addListener((observableValue, s, newValue) -> {
            suggestionArray[1] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel3Number().textProperty().addListener((observableValue, s, newValue) -> {
            suggestionArray[2] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel4Number().textProperty().addListener((observableValue, s, newValue) -> {
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

        updateTextFields();

        frequencySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newFrequency = newValue.doubleValue();
            updateTestWave(newFrequency, testWave);
            updateOutputWave(inputWave, testWave);
            updateTextFields();

            System.out.println(newValue.intValue());

            if (newValue.intValue() == lock.getFirst() && solution1.getData().isEmpty()) {
                solution1.getData().add(new XYChart.Data(String.valueOf(lock.getFirst()), 1));
            }
            else {
                if (!solution1.getData().isEmpty()) {
                    barChart.getData().remove(solution1);
                }
            }
/*
            if (newValue.intValue() == lock.getFirst() && !barChart.getData().remove(solution1)) {
                barChart.getData().add(solution1);
            } else {
                if (barChart.getData().contains(solution1)) {
                    barChart.getData().remove(solution1);
                }
            }
            if (newValue.intValue() == lock.getSecond() && !barChart.getData().contains(solution2)) {
                barChart.getData().add(solution2);
            } else {
                if (barChart.getData().contains(solution2)) {
                    barChart.getData().remove(solution2);
                }
            }
            if (newValue.intValue() == lock.getThird() && !barChart.getData().contains(solution3)) {
                barChart.getData().add(solution3);
            } else {
                if (barChart.getData().contains(solution3)) {
                    barChart.getData().remove(solution3);
                }
            }
            if (newValue.intValue() == lock.getFourth() && !barChart.getData().contains(solution4)) {
                barChart.getData().add(solution4);
            } else {
                if (barChart.getData().contains(solution4)) {
                    barChart.getData().remove(solution4);
                }
            }

 */


            barChart.setBarGap(0.01);
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

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
