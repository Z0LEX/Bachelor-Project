package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Slider;
import main.application.Server;
import main.components.Wave;
import main.datatypes.PiStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

public class PhaseShiftController implements Initializable {
    private double range = 1;

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

    private ArrayList<Wave> waves = new ArrayList<>();
    private ArrayList<Wave> resultWaves = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wave1 = new Wave(1, lineChart1);
        wave2 = new Wave(3, lineChart2);
        wave3 = new Wave(5, lineChart3);
        wave4 = new Wave(8, lineChart4);

        waves.add(wave1);
        waves.add(wave2);
        waves.add(wave3);
        waves.add(wave4);

        Wave waveResult1 = new Wave(1);
        waveResult1.setPhaseShift(Math.PI);
        Wave waveResult2 = new Wave(3);
        waveResult2.setPhaseShift((3*Math.PI) / 4);
        Wave waveResult3 = new Wave(5);
        waveResult3.setPhaseShift(Math.PI / 2);
        Wave waveResult4 = new Wave(8);
        waveResult4.setPhaseShift(-Math.PI / 2);

        resultWaves.add(waveResult1);
        resultWaves.add(waveResult2);
        resultWaves.add(waveResult3);
        resultWaves.add(waveResult4);
        sendResultToClient();

        slider1.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave1);
        });
        slider2.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave2);
        });
        slider3.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave3);
        });
        slider4.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave4);
        });

        slider1.setLabelFormatter(new PiStringConverter());
        slider2.setLabelFormatter(new PiStringConverter());
        slider3.setLabelFormatter(new PiStringConverter());
        slider4.setLabelFormatter(new PiStringConverter());

        sendToClient();
    }


    private void handleSlider(double newPhase, Wave wave) {
        wave.clear();
        wave.setFunction(x -> wave.getWave(x, wave.getFrequency(), newPhase));
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
}
