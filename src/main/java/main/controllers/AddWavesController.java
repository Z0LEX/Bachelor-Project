package main.controllers;

import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.application.AddWavesAmplitudeViewer;
import main.components.CombinationLock;
import main.components.Wave;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Arrays;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddWavesController implements Initializable {
    private ArrayList<Wave> waves = new ArrayList<>(4);

    private Wave sumWave;
    private Wave resultWave;

    private CombinationLock lock = new CombinationLock(4, 2, 6, 3);

    private Label lockNumber1;
    private Label lockNumber2;
    private Label lockNumber3;
    private Label lockNumber4;

    private int[] solutionArray = new int[4];
    private int[] suggestionArray = new int[4];

    private Stage stage;

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
    private boolean gameWon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lockButton.setOpacity(0);

        lockButton.setOnAction(actionEvent -> {
            AddWavesAmplitudeViewer addWavesAmplitudeViewer = new AddWavesAmplitudeViewer(stage);
            addWavesAmplitudeViewer.startAddWavesAmplitudeViewer(stage);
        });

        solutionArray[0] = lock.getFirst();
        solutionArray[1] = lock.getSecond();
        solutionArray[2] = lock.getThird();
        solutionArray[3] = lock.getForth();
        Arrays.sort(solutionArray);


        // Initialize the with a complicated wave to find
        Wave wave1 = new Wave(lock.getFirst(), 1);
        Wave wave2 = new Wave(lock.getSecond(), 1);
        Wave wave3 = new Wave(lock.getThird(), 1);
        Wave wave4 = new Wave(lock.getForth(), 1);
        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));
        resultWave = new Wave(Wave.sumWaves(waveResult), lineChartResult);

        waves.add(new Wave(0, 1, lineChart1));
        waves.add(new Wave(0, 1, lineChart2));
        waves.add(new Wave(0, 1, lineChart3));
        waves.add(new Wave(0, 1, lineChart4));

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
        wave.setFrequency(frequency);
        wave.clear();
        wave.plotFunction(wave.getFunction());
        updateTitle(wave, frequency);
        getSumWave();

        checkSolution();
    }

    private void updateTitle(Wave wave, double frequency) {
        String title = (int) frequency + " Hz";
        if ((int) frequency == 0) {
            // Some JavaFX bug will cause the following to draw the graph incorrectly around 0
//            title += " (DC)";
        }
        wave.getLineChart().setTitle(title);
    }

    private void checkSolution() {
        suggestionArray[0] = Integer.parseInt(lock.getController().getWheel1Number().getText());
        suggestionArray[1] = Integer.parseInt(lock.getController().getWheel2Number().getText());
        suggestionArray[2] = Integer.parseInt(lock.getController().getWheel3Number().getText());
        suggestionArray[3] = Integer.parseInt(lock.getController().getWheel4Number().getText());
        Arrays.sort(suggestionArray);

        if (Arrays.equals(suggestionArray, solutionArray)) {
            gameWon = true;
        } else gameWon = false;

        if (gameWon == true) {
            lockButton.setDisable(false);
            lockButton.setOpacity(1);
        } else {
            lockButton.setDisable(true);
            lockButton.setOpacity(0);
        }
    }

    private void getSumWave() {
        Function<Double, Double> sum = Wave.sumWaves(waves);
        sumWave.setFunction(sum);
        sumWave.getGraph().overwritePlot(sumWave.getFunction(), 0);

        resultWave.getSeries().getNode().getStyleClass().add("result-series");
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
