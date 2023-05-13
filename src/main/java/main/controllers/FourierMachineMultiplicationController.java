package main.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.application.StageAwareController;
import main.application.StageManager;
import main.components.CombinationLock;
import main.components.Wave;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FourierMachineMultiplicationController implements Initializable, StageAwareController {
    public static final double result1 = 0.5;
    public static final double result2 = 0.1666;
    public static final double result3 = 0.9666;
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
    private Button lockButton;

    @FXML
    private Text outputText;
    @FXML
    private Text inputText;
    @FXML
    private Text testText;

    @FXML
    private TextField resultInput;

    private CombinationLock lock = new CombinationLock(1, 3, 5, 8);

    private int[] solutionArray = new int[4];
    private int[] suggestionArray = new int[4];
    private boolean gameWon;
    private StageManager stageManager;
    private int leftOffset = 48;
    private int topOffset = 13;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        addStaticPoint(outputWave, outputGraph, outputPane, result1);
        addStaticPoint(outputWave, outputGraph, outputPane, result2);
        addStaticPoint(outputWave, outputGraph, outputPane, result3);


        Circle inputPoint = new Circle(5, Color.BLUE);
        inputGraph.getYAxis().boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> updatePointPosition(inputGraph, inputWave, inputPoint, 0)));
            timeline.play();

            updateText(inputWave, inputText, 0);
        });
        inputPane.getChildren().add(inputPoint);

        Circle testPoint = new Circle(5, Color.YELLOW);
        testGraph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> updatePointPosition(testGraph, testWave, testPoint, 0)));
            timeline.play();
        });
        testPane.getChildren().add(testPoint);

        Circle outputPoint = new Circle(3, Color.GREEN);
        outputGraph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> updatePointPosition(outputGraph, outputWave, outputPoint, 0)));
            timeline.play();
        });
        outputPane.getChildren().add(outputPoint);

        xAxisSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double x = newValue.doubleValue();
            updatePointPosition(inputGraph, inputWave, inputPoint, x);
            updatePointPosition(testGraph, testWave, testPoint, x);
            updatePointPosition(outputGraph, outputWave, outputPoint, x);

            updateText(inputWave, inputText, x);
            updateText(testWave, testText, x);
            updateText(outputWave, outputText, x);
        });

        resultInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            
        });

        resultInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                resultInput.getParent().requestFocus();
            }
        });
    }

    private void addStaticPoint(Wave wave, LineChart<Double, Double> graph, AnchorPane pane, double x) {
        Circle point = new Circle(5, Color.GREEN);
        graph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> updatePointPosition(graph, wave, point, x)));
            timeline.play();
        });
        pane.getChildren().add(point);
    }

    private void updateText(Wave wave, Text text, double x) {
        Double result = wave.getFunction().apply(x);
        text.setText(String.format("%.2f", result));
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
