package controllers;

import components.CombinationLock;
import components.Wave;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddWavesController implements Initializable {
    private ArrayList<Wave> waves = new ArrayList<>(4);

    private Wave sumWave;

    private CombinationLock lock = new CombinationLock();

    private Label lockNumber1;
    private Label lockNumber2;
    private Label lockNumber3;
    private Label lockNumber4;
    @FXML
    private HBox lockContainer;
    @FXML
    private VBox waveContainer;
    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane graph1;

    @FXML
    private AnchorPane graph2;

    @FXML
    private AnchorPane graph3;

    @FXML
    private AnchorPane graph4;

    @FXML
    private AnchorPane resultGraphBottom;

    @FXML
    private AnchorPane resultGraphTop;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 4; i++) {
            Wave wave = new Wave(0);
            waves.add(wave);
            updateWaveContainer(wave, i);
        }

        // Initialize the bottom splitpane with a complicated wave
        Wave wave1 = new Wave(1);
        Wave wave2 = new Wave(3);
        Wave wave3 = new Wave(5);
        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(wave1, wave2, wave3));
        Wave result = new Wave(sumWaves(waveResult));
        updateSplitPane(result, 1);

        lockContainer.getChildren().add(lock.getRoot());
        lockNumber1 = lock.getController().getWheel1Number();
        lockNumber2 = lock.getController().getWheel2Number();
        lockNumber3 = lock.getController().getWheel3Number();
        lockNumber4 = lock.getController().getWheel4Number();
        lockNumber1.textProperty().addListener((observable, oldValue, newValue) -> {
            int index = 0;
            Wave wave = addWave(Double.parseDouble(newValue), index);
            updateWaveContainer(wave, index);
        });
        lockNumber2.textProperty().addListener((observable, oldValue, newValue) -> {
            int index = 1;
            Wave wave = addWave(Double.parseDouble(newValue), index);
            updateWaveContainer(wave, index);
        });
        lockNumber3.textProperty().addListener((observable, oldValue, newValue) -> {
            int index = 2;
            Wave wave = addWave(Double.parseDouble(newValue), index);
            updateWaveContainer(wave, index);
        });
        lockNumber4.textProperty().addListener((observable, oldValue, newValue) -> {
            int index = 3;
            Wave wave = addWave(Double.parseDouble(newValue), index);
            updateWaveContainer(wave, index);
        });
    }

    public Wave addWave(double frequency, int index) {
        Wave newWave = new Wave(frequency);
        waves.set(index, newWave);

        getSumWave();
        return newWave;
    }

    private void updateWaveContainer(Wave wave, int index) {
        waveContainer.getChildren().set(index, wave.getRoot());
    }

    private void updateSplitPane(Wave wave, int index) {
        splitPane.getItems().set(index, wave.getRoot());
    }

    private void getSumWave() {
        Function<Double, Double> sum = sumWaves(waves);
        sumWave = new Wave(sum);
        updateSplitPane(sumWave, 0);
    }


    private Function<Double, Double> sumWaves(ArrayList<Wave> waves) {
        return x -> {
            double sum = 0;
            for (Wave wave : waves) {
                sum += wave.getFunction().apply(x);
            }
            return sum;
        };
    }
}
