package main.controllers;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.components.CombinationLock;
import main.components.Wave;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddWavesController implements Initializable {
    private ArrayList<Wave> waves = new ArrayList<>(4);

    private Wave sumWave;
    private Wave resultWave;

    private CombinationLock lock = new CombinationLock();

    private Label lockNumber1;
    private Label lockNumber2;
    private Label lockNumber3;
    private Label lockNumber4;
    @FXML
    private Button lockButton;
    @FXML
    private HBox lockContainer;
    @FXML
    private LineChart<Double, Double> lineChartResult;
    @FXML
    private LineChart<Double, Double> lineChart1;
    @FXML
    private LineChart<Double, Double> lineChart2;
    @FXML
    private LineChart<Double, Double> lineChart3;
    @FXML
    private LineChart<Double, Double> lineChart4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image image = new Image(getClass().getResourceAsStream("/assets/lockImage.png"));
        lockButton.setGraphic(new ImageView(image));
        // Initialize the with a complicated wave to find
        Wave wave1 = new Wave(1);
        Wave wave2 = new Wave(3);
        Wave wave3 = new Wave(5);
        Wave wave4 = new Wave(8);
        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));
        resultWave = new Wave(Wave.sumWaves(waveResult), lineChartResult);
        waves.add(new Wave(0, lineChart1));
        waves.add(new Wave(0, lineChart2));
        waves.add(new Wave(0, lineChart3));
        waves.add(new Wave(0, lineChart4));

        // Initialize the with sumWave
        sumWave = new Wave(Wave.sumWaves(waves), lineChartResult);

        resultWave.getSeries().getNode().getStyleClass().add("result-series");

        // Add the combinationlock to the lockcontainer
        lockContainer.getChildren().add(lock.getRoot());

        // For each lock number label add new listener that updates the graph with the value of the label
        lockNumber1 = lock.getController().getWheel1Number();
        lockNumber2 = lock.getController().getWheel2Number();
        lockNumber3 = lock.getController().getWheel3Number();
        lockNumber4 = lock.getController().getWheel4Number();

        lockNumber1.textProperty().addListener((observable, oldValue, newValue) -> updateWave(Double.parseDouble(newValue), 0));
        lockNumber2.textProperty().addListener((observable, oldValue, newValue) -> updateWave(Double.parseDouble(newValue), 1));
        lockNumber3.textProperty().addListener((observable, oldValue, newValue) -> updateWave(Double.parseDouble(newValue), 2));
        lockNumber4.textProperty().addListener((observable, oldValue, newValue) -> updateWave(Double.parseDouble(newValue), 3));
    }
    public void updateWave(double frequency, int index) {
        Wave wave = waves.get(index);
        wave.clear();
        wave.setFrequency(frequency);
        wave.plotFunction(wave.getFunction());
        getSumWave();
    }

    private void getSumWave() {
        Function<Double, Double> sum = Wave.sumWaves(waves);
        sumWave.setFunction(sum);
        sumWave.getGraph().overwritePlot(sumWave.getFunction(), 0);

        resultWave.getSeries().getNode().getStyleClass().add("result-series");
    }
}
