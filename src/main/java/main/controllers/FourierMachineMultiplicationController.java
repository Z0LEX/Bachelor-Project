package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.application.StageAwareController;
import main.application.StageManager;
import main.components.CombinationLock;
import main.components.Wave;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FourierMachineMultiplicationController implements Initializable, StageAwareController {
    @FXML
    private AnchorPane inputPane;

    @FXML
    private AnchorPane testPane;

    @FXML
    private AnchorPane outputPane;

    @FXML
    private LineChart<Double, Double> inputGraph;

    @FXML
    private LineChart<Double, Double> outputGraph;

    @FXML
    private LineChart<Double, Double> testGraph;

    @FXML
    private Slider xAxisSlider;

    @FXML
    private HBox lockContainer;

    @FXML
    private Button lockButton;

    private CombinationLock lock = new CombinationLock(1, 3, 5, 8);

    private int[] solutionArray = new int[4];
    private int[] suggestionArray = new int[4];
    private boolean gameWon;
    private StageManager stageManager;
    private int leftOffset = 28;
    private int topOffset = 13;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lockContainer.getChildren().add(lock.getRoot());

        lockButton.setOpacity(0);

        lockButton.setOnAction(actionEvent -> {
            stageManager.setScene("/phase-shift.fxml");
        });


        // Update solution array
        solutionArray[0] = lock.getFirst();
        solutionArray[1] = lock.getSecond();
        solutionArray[2] = lock.getThird();
        solutionArray[3] = lock.getFourth();


        lock.getController().getWheel1Number().textProperty().addListener((observableValue, s, newValue) -> {
            suggestionArray[0] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel2Number().textProperty().addListener((observableValue, s, newValue) -> {
            suggestionArray[1] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel3Number().textProperty().addListener((observableValue, s, newValue) -> {
            suggestionArray[2] = Integer.parseInt(newValue);
            checkSolution();
        });

        lock.getController().getWheel4Number().textProperty().addListener((observableValue, s, newValue) -> {
            suggestionArray[3] = Integer.parseInt(newValue);
            checkSolution();
        });

        // Create input wave
        Wave wave1 = new Wave(lock.getFirst(), 1);
        Wave wave2 = new Wave(lock.getSecond(), 1);
        Wave wave3 = new Wave(lock.getThird(), 1);
        Wave wave4 = new Wave(lock.getFourth(), 1);
        ArrayList<Wave> inputWaves = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));
        Wave inputWave = new Wave(Wave.sumWaves(inputWaves), inputGraph);

        // Testwave
        Wave testWave = new Wave(5, 1, testGraph);

        // Resulting output wave
        Wave outputWave = new Wave(Wave.multiplyWaves(inputWave, testWave));

        updateOutput(outputWave);

        Circle problem1Input = new Circle(5, Color.YELLOWGREEN);
        inputGraph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> updatePointPosition(inputGraph, inputWave, problem1Input, 0.5));
        inputPane.getChildren().add(problem1Input);

        Circle problem1Test = new Circle(5, Color.YELLOWGREEN);
        testGraph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> updatePointPosition(testGraph, testWave, problem1Test, 0.5));
        testPane.getChildren().add(problem1Test);

        Circle inputPoint = new Circle(5, Color.RED);
        inputGraph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> updatePointPosition(inputGraph, inputWave, inputPoint, 0));
        inputPane.getChildren().add(inputPoint);

        Circle testPoint = new Circle(5, Color.RED);
        testGraph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> updatePointPosition(testGraph, testWave, testPoint, 0));
        testPane.getChildren().add(testPoint);

        Circle resultPoint = new Circle(5, Color.RED);
        outputGraph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> updatePointPosition(outputGraph, outputWave, resultPoint, 0));
        outputPane.getChildren().add(resultPoint);

        xAxisSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double x = newValue.doubleValue();
            updatePointPosition(inputGraph, inputWave, inputPoint, x);
            updatePointPosition(testGraph, testWave, testPoint, x);
            updatePointPosition(outputGraph, outputWave, resultPoint, x);
        });

    }

    private void updatePointPosition(LineChart<Double, Double> graph, Wave wave, Circle point, double x) {
        ValueAxis xAxis = (ValueAxis) graph.getXAxis();
        ValueAxis yAxis = (ValueAxis) graph.getYAxis();
        double displayPositionX = xAxis.getDisplayPosition(x);
        double displayPositionY = yAxis.getDisplayPosition(wave.getFunction().apply(x));
        point.setCenterX(displayPositionX + leftOffset);
        point.setCenterY(displayPositionY + topOffset);
    }

    private void checkSolution() {
        suggestionArray[0] = Integer.parseInt(lock.getController().getWheel1Number().getText());
        suggestionArray[1] = Integer.parseInt(lock.getController().getWheel2Number().getText());
        suggestionArray[2] = Integer.parseInt(lock.getController().getWheel3Number().getText());
        suggestionArray[3] = Integer.parseInt(lock.getController().getWheel4Number().getText());
        Arrays.sort(suggestionArray);
        if (Arrays.equals(suggestionArray, solutionArray)) {
            gameWon = true;
        } else {
            gameWon = false;
        }
        if (gameWon == true) {
            lockButton.setDisable(false);
            lockButton.setOpacity(1);
        } else {
            lockButton.setDisable(true);
            lockButton.setOpacity(0);
        }
    }

    private void updateOutputWave(Wave inputWave, Wave testWave) {
        Wave outputWave = new Wave(Wave.multiplyWaves(inputWave, testWave));
        outputGraph.getData().clear();

        updateOutput(outputWave);
    }

    private void updateOutput(Wave outputWave) {
        XYChart.Series<Double, Double> series = outputWave.getSeries();
        outputGraph.getData().add(series);
    }

    private void updateTestWave(double newFrequency, Wave wave) {
        wave.clear();
        wave.setFunction(x -> wave.getWave(x, newFrequency, wave.getAmplitude(), wave.getPhaseShift()));
        wave.plotFunction(wave.getFunction());
    }

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
