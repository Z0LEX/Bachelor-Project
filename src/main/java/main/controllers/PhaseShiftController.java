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
import java.util.Arrays;
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

    private ArrayList<Wave> waves = new ArrayList<>();
    private ArrayList<Wave> resultWaves = new ArrayList<>();
    private String[] solutionArray = new String[4];
    private String[] suggestionArray = new String[4];
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
        waveResult1.setPhaseShift(Math.PI);
        solutionArray[0] = String.valueOf(Math.PI).substring(0, 6);
        Wave waveResult2 = new Wave(2, 1);
        waveResult2.setPhaseShift((3 * Math.PI) / 4);
        solutionArray[1] = String.valueOf((3 * Math.PI) / 4).substring(0, 6);
        Wave waveResult3 = new Wave(5, 1);
        waveResult3.setPhaseShift(Math.PI / 2);
        solutionArray[2] = String.valueOf((Math.PI / 2)).substring(0, 6);
        Wave waveResult4 = new Wave(3, 1);
        waveResult4.setPhaseShift(-Math.PI / 2);
        solutionArray[3] = String.valueOf(-Math.PI / 2).substring(0, 6);

        resultWaves.add(waveResult1);
        resultWaves.add(waveResult2);
        resultWaves.add(waveResult3);
        resultWaves.add(waveResult4);
        sendResultToClient();

        slider1.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[0] = String.valueOf(newPhase).substring(0, 6);
            handleSlider(newPhase, wave1);
            checkSolution();
        });
        slider2.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[1] = String.valueOf(newPhase).substring(0, 6);
            handleSlider(newPhase, wave2);
            checkSolution();
        });
        slider3.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[2] = String.valueOf(newPhase).substring(0, 6);
            handleSlider(newPhase, wave3);
            checkSolution();
        });
        slider4.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            suggestionArray[3] = String.valueOf(newPhase).substring(0, 6);
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
        if (Arrays.equals(suggestionArray, solutionArray)) {
            if (!hasWon) {
                hasWon = true;
                //Yay we wonnered
                System.out.println("Wonnered!");
                new Print("You've solved the problem\nThe code is: 4685");
                stageManager.setScene("/draw-graph.fxml");
            }
        }
    }

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
