package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Slider;
import main.application.Server;
import main.application.StageAwareController;
import main.application.StageManager;
import main.components.Wave;
import main.datatypes.PiStringConverter;
import main.datatypes.Print;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

public class PhaseShiftController implements Initializable, StageAwareController {
    @FXML
    private LineChart<Double, Double> lineChart1;

    @FXML
    private LineChart<Double, Double> lineChart2;

    @FXML
    private LineChart<Double, Double> lineChart3;

    @FXML
    private LineChart<Double, Double> lineChart4;

    @FXML
    private Slider slider1;

    @FXML
    private Slider slider2;

    @FXML
    private Slider slider3;

    @FXML
    private Slider slider4;

    private Wave wave1;
    private Wave wave2;
    private Wave wave3;
    private Wave wave4;

    private boolean hasWon = false;
    private static final double tolerance = 0.01;

    private ArrayList<Wave> waves = new ArrayList<>();
    private ArrayList<Wave> resultWaves = new ArrayList<>();
    private double[] solutionArray = new double[4];
    private double[] suggestionArray = new double[4];
    private StageManager stageManager;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wave1 = new Wave(6, 1, lineChart1);
        wave2 = new Wave(2, 1, lineChart2);
        wave3 = new Wave(5, 1, lineChart3);
        wave4 = new Wave(3, 1, lineChart4);

        waves.add(wave1);
        waves.add(wave2);
        waves.add(wave3);
        waves.add(wave4);

        Wave waveResult1 = new Wave(6, 1);
        double solution1 = (3 * Math.PI) / 4;
        waveResult1.setPhaseShift(solution1);
        solutionArray[0] = solution1;

        Wave waveResult2 = new Wave(2, 1);
        double solution2 = Math.PI / 2;
        waveResult2.setPhaseShift(solution2);
        solutionArray[1] = solution2;

        Wave waveResult3 = new Wave(5, 1);
        double solution3 = Math.PI / 4;
        waveResult3.setPhaseShift(solution3);
        solutionArray[2] = solution3;

        Wave waveResult4 = new Wave(3, 1);
        double solution4 = -Math.PI / 2;
        waveResult4.setPhaseShift(solution4);
        solutionArray[3] = solution4;

        resultWaves.add(waveResult1);
        resultWaves.add(waveResult2);
        resultWaves.add(waveResult3);
        resultWaves.add(waveResult4);
        sendResultToClient();

        slider1.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[0] = newPhase;
            handleSlider(newPhase, wave1);
            checkSolution();
        });
        slider2.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[1] = newPhase;
            handleSlider(newPhase, wave2);
            checkSolution();
        });
        slider3.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[2] = newPhase;
            handleSlider(newPhase, wave3);
            checkSolution();
        });
        slider4.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[3] = newPhase;
            handleSlider(newPhase, wave4);
            checkSolution();
        });

        slider1.setLabelFormatter(new PiStringConverter());
        slider2.setLabelFormatter(new PiStringConverter());
        slider3.setLabelFormatter(new PiStringConverter());
        slider4.setLabelFormatter(new PiStringConverter());

        sendToClient();
    }


    private void handleSlider(double newPhase, Wave wave) {
        wave.clear();
        wave.setFunction(x -> wave.getWave(x, wave.getFrequency(), wave.getAmplitude(), newPhase));
        wave.plotFunction(wave.getFunction());
        sendToClient();
    }

    private void sendToClient() {
        Function<Double, Double> sumFunction = Wave.sumWaves(waves);

        Wave wave = new Wave(sumFunction);
        double[][] data = wave.getGraph().getDataAsArray();
        try {
            Server.space.put("Phase shift", data);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResultToClient() {
        Function<Double, Double> sumFunction = Wave.sumWaves(resultWaves);
        Wave wave = new Wave(sumFunction);
        double[][] data = wave.getGraph().getDataAsArray();
        try {
            Server.space.put("Phase shift result", data);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkSolution() {
        if (areArraysEqual(suggestionArray, solutionArray)) {
            if (!hasWon) {
                hasWon = true;
                stageManager.setScene("/win.fxml");
                new Print("You've solved\nthe problem\n\nThe code is: 4685");
            }
        }
    }
    private boolean areArraysEqual(double[] array1, double[] array2) {
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!isDoubleEqual(array1[i], array2[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean isDoubleEqual(double a, double b) {
        return Math.abs(a - b) < tolerance;
    }

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
