package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Slider;
import main.application.Server;
import main.components.Wave;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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


        slider1.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave1, 0);
        });
        slider2.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave2, 1);
        });
        slider3.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave3, 2);
        });
        slider4.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double newPhase = newValue.doubleValue();
            handleSlider(newPhase, wave4, 3);
        });

    }

    private void handleSlider(double newPhase, Wave wave, int i) {
        wave.clear();
        wave.setFunction(x -> Wave.getWave(x, wave.getFrequency(), newPhase));
        sendToClient(newPhase, i);
    }

    private void sendToClient(double newPhase, int i) {
        try {
            System.out.println("put in space: " + newPhase);
            Server.space.put("Phase shift", i, newPhase);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
