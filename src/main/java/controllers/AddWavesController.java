package controllers;

import components.Wave;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddWavesController implements Initializable {
    private ArrayList<Wave> waves = new ArrayList<>();

    private Function<Double, Double> sum;

    private Wave sumWave;
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
    private Button clearButton;
    @FXML
    private VBox waveContainer;
    @FXML
    private SplitPane splitPane;

    @FXML
    void handleOneHertz(ActionEvent event) {
        addWave(1);
    }

    @FXML
    void handleTwoHertz(ActionEvent event) {
        addWave(2);

    }

    @FXML
    void handleThreeHertz(ActionEvent event) {
        addWave(3);

    }

    @FXML
    void handleFourHertz(ActionEvent event) {
        addWave(4);

    }

    @FXML
    void handleFiveHertz(ActionEvent event) {
        addWave(5);

    }

    @FXML
    void handleClear(ActionEvent event) {
        for (Wave wave : waves) {
            waveContainer.getChildren().remove(wave.getRoot());
        }
        waves.clear();
        sumWave.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the bottom splitpane with a complicated wave
        Wave wave1 = new Wave(1);
        Wave wave2 = new Wave(3);
        Wave wave3 = new Wave(5);
        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(wave1, wave2 ,wave3));
        Wave result = new Wave(sumWaves(waveResult));
        splitPane.getItems().remove(1);
        splitPane.getItems().add(result.getRoot());
    }

    public void addWave(double frequency) {
        Wave newWave = new Wave(frequency);
        waves.add(newWave);
        waveContainer.getChildren().add(newWave.getRoot());

        sum = sumWaves(waves);
        sumWave = new Wave(sum);
        splitPane.getItems().remove(0);
        splitPane.getItems().add(0, sumWave.getRoot());
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
