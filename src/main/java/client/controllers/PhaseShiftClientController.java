package client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import main.components.Wave;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

public class PhaseShiftClientController implements Initializable {

    @FXML
    private LineChart<Double, Double> lineGraphBottom;

    @FXML
    public LineChart<Double, Double> lineGraphTop;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        // Top wave to
//        Wave wave1 = new Wave(1);
//        wave1.setPhaseShift(0);
//        Wave wave2 = new Wave(3);
//        wave2.setPhaseShift(0);
//        Wave wave3 = new Wave(5);
//        wave3.setPhaseShift(0);
//        Wave wave4 = new Wave(8);
//        wave4.setPhaseShift(0);
//
//        ArrayList<Wave> waveSum = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));
//        Function<Double, Double> sumFunction = Wave.sumWaves(waveSum);
//
//        Wave sumWave = new Wave(sumFunction, lineGraphTop);


        Wave waveResult1 = new Wave(1);
        waveResult1.setPhaseShift(1);
        Wave waveResult2 = new Wave(3);
        waveResult2.setPhaseShift(2);
        Wave waveResult3 = new Wave(5);
        waveResult3.setPhaseShift(-3);
        Wave waveResult4 = new Wave(8);
        waveResult4.setPhaseShift(-4);

        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(waveResult1, waveResult2, waveResult3, waveResult4));

        // Sum the waves and plot the function
        Function<Double, Double> resultFunction = Wave.sumWaves(waveResult);
        Wave resultWave = new Wave(resultFunction, lineGraphBottom);
    }
}
