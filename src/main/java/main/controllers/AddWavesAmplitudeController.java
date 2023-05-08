package main.controllers;

import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.application.FourierMachineViewer;
import main.application.StageAwareController;
import main.application.StageManager;
import main.components.CombinationLock;
import main.components.Wave;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.datatypes.Print;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddWavesAmplitudeController implements Initializable, StageAwareController {
    private ArrayList<Wave> waves = new ArrayList<>(4);

    private Stage stage;

    private Wave sumWave;
    private Wave resultWave;

    private CombinationLock lock = new CombinationLock(4, 6, 8, 5);

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

    private boolean gameWon;
    private StageManager stageManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Image image = new Image(getClass().getResourceAsStream("/assets/images/lockImage.png"));
        //ImageView imageView = new ImageView(image);
        //imageView.fitWidthProperty().bind(lockButton.widthProperty());
        //imageView.fitHeightProperty().bind(lockButton.heightProperty());
        //lockButton.setGraphic(imageView);

        lockButton.setOnAction(actionEvent -> {
//            Print print = new Print("You've solved the problem\nThe code is: 4685");
            stageManager.setScene("/fourier-machine.fxml");
//            FourierMachineViewer fourierMachineViewer = new FourierMachineViewer(stage);
//            fourierMachineViewer.startFourierMachineViewer(stage);
        });

        // Make button with continue invisible
        lockButton.setOpacity(0);

        // Initialize with a complicated wave to find
        Wave wave1 = new Wave(0, lock.getFirst());
        Wave wave2 = new Wave(1, lock.getSecond());
        Wave wave3 = new Wave(2, lock.getThird());
        Wave wave4 = new Wave(3, lock.getForth());
        ArrayList<Wave> waveResult = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));
        resultWave = new Wave(Wave.sumWaves(waveResult), lineChartResult);

        waves.add(new Wave(0, 0, lineChart1));
        waves.add(new Wave(1, 0, lineChart2));
        waves.add(new Wave(2, 0, lineChart3));
        waves.add(new Wave(3, 0, lineChart4));

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

    public void updateWave(double amplitude, int index) {
        Wave wave = waves.get(index);
        wave.clear();
        wave.setAmplitude(amplitude);
        wave.plotFunction(wave.getFunction());
        getSumWave();

        if (
                lock.getFirst() == Integer.parseInt(lock.getController().getWheel1Number().getText()) &&
                        lock.getSecond() == Integer.parseInt(lock.getController().getWheel2Number().getText()) &&
                        lock.getThird() == Integer.parseInt(lock.getController().getWheel3Number().getText()) &&
                        lock.getForth() == Integer.parseInt(lock.getController().getWheel4Number().getText())
        ) {
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

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
