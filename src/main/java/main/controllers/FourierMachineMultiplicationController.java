package main.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.application.Server;
import main.application.StageAwareController;
import main.application.StageManager;
import main.components.MultiplicationInput;
import main.components.Wave;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FourierMachineMultiplicationController implements Initializable, StageAwareController {
    public static final double result1 = 0.1666;
    public static final double result2 = 0.5;
    public static final double result3 = 0.9666;
    private static final double tolerance = 0.01;
    public static final Color RESULT_COLOR = Color.rgb(204, 85, 0);
    public static final Color INPUT_COLOR = Color.BLUE;
    public static final Color TEST_COLOR = Color.RED;
    private final Color CORRECT_COLOR = Color.GREEN;
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
    private Button checkButton;
    @FXML
    private Text outputText;
    @FXML
    private HBox inputContainer;

    private MultiplicationInput multiplcationInput = new MultiplicationInput();
    private StageManager stageManager;
    private int leftOffset = 48;
    private int topOffset = 13;
    private Text inputText = new Text("0.00");
    private Text testText = new Text("0.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupText(inputText, INPUT_COLOR);
        setupText(testText, TEST_COLOR);

        inputContainer.getChildren().add(multiplcationInput.getRoot());
        lockButton.setOnAction(actionEvent -> {
            stageManager.setScene("/fourier-machine.fxml");
            try {
                Server.space.put("Show fourier machine");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Create input wave
        Wave wave1 = new Wave(1, 1);
        Wave wave2 = new Wave(3, 1);
        Wave wave3 = new Wave(5, 1);
        Wave wave4 = new Wave(8, 1);
        ArrayList<Wave> inputWaves = new ArrayList<>(Arrays.asList(wave1, wave2, wave3, wave4));
        Wave inputWave = new Wave(Wave.sumWaves(inputWaves), inputGraph);

        Wave testWave = new Wave(5, 1, testGraph);

        // Resulting output wave
        Wave outputWave = new Wave(Wave.multiplyWaves(inputWave, testWave));
        updateOutput(outputWave);

        // Create point for input graph
        Circle inputPoint = new Circle(5, INPUT_COLOR);

        // Add point and text to pane
        inputPane.getChildren().add(inputPoint);
        inputPane.getChildren().add(inputText);

        // Bind the text position to the point
        setTextPosition(inputText, inputPoint);

        // Update the point position and set the text value
        setInitialPosition(inputGraph, inputWave, inputPoint, 0);
        updateText(inputWave, inputText, 0);


        Circle testPoint = new Circle(5, TEST_COLOR);
        testPane.getChildren().add(testPoint);
        testPane.getChildren().add(testText);
        setTextPosition(testText, testPoint);
        setInitialPosition(testGraph, testWave, testPoint, 0);
        updateText(testWave, testText, 0);


        Circle outputPoint = new Circle(3, RESULT_COLOR);
        outputPane.getChildren().add(outputPoint);
        setInitialPosition(outputGraph, outputWave, outputPoint, 0);

        Circle resultPoint1 = addStaticPoint(outputWave, outputGraph, outputPane, result1);
        Circle resultPoint2 = addStaticPoint(outputWave, outputGraph, outputPane, result2);
        Circle resultPoint3 = addStaticPoint(outputWave, outputGraph, outputPane, result3);

        xAxisSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double x = newValue.doubleValue();
            // Change the point position according to the x-value
            updatePointPosition(inputGraph, inputWave, inputPoint, x);
            updatePointPosition(testGraph, testWave, testPoint, x);
            updatePointPosition(outputGraph, outputWave, outputPoint, x);

            // Update the text accordingly
            updateText(inputWave, inputText, x);
            updateText(testWave, testText, x);
            updateText(outputWave, outputText, x);
        });

        checkButton.setOnAction(actionEvent -> {
            double inputNumber = getInputNumber();
            double sliderValue = xAxisSlider.getValue();
            double resultNumber = outputWave.getFunction().apply(sliderValue);
            // Check if the x-axis is at one of the result positions
            if (isDoubleEqual(sliderValue, result1)) checkResult(resultPoint1, inputNumber, resultNumber);
            if (isDoubleEqual(sliderValue, result2)) checkResult(resultPoint2, inputNumber, resultNumber);
            if (isDoubleEqual(sliderValue, result3)) checkResult(resultPoint3, inputNumber, resultNumber);
        });
    }

    private void setupText(Text testText, Color testColor) {
        testText.setFill(Color.WHITE);
        testText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        testText.setStroke(testColor);
        testText.setStrokeWidth(0.5);
    }

    private void setTextPosition(Text text, Circle point) {
        text.translateXProperty().bind(point.centerXProperty().subtract(text.getBoundsInLocal().getWidth() / 2));
        text.translateYProperty().bind(point.centerYProperty().subtract(point.getRadius() + text.getBoundsInLocal().getHeight() - 15));
    }

    private void checkResult(Circle resultPoint, double inputNumber, Double resultNumber) {
        // Check if user has entered the correct number
        if (isDoubleEqual(inputNumber, resultNumber)) {
            resultPoint.setFill(Color.GREEN);
        }
        // Get a list of circles with the green success color, if all 3 are green the problem is solved
        List<Circle> circles = outputPane.getChildren().stream().filter(node -> node instanceof Circle).map(node -> (Circle) node).filter(circle -> circle.getFill() == CORRECT_COLOR).collect(Collectors.toList());
        if (circles.size() == 3) {
            checkButton.setDisable(true);
            checkButton.setOpacity(0);
            lockButton.setOpacity(1);
            lockButton.setDisable(false);
        }
    }

    // Query the numbers input by the user
    private double getInputNumber() {
        String sign = multiplcationInput.getSign();
        String integer = multiplcationInput.getInteger();
        String tenths = multiplcationInput.getTenths();
        String hundreths = multiplcationInput.getHundreths();

        String number = sign + integer + "." + tenths + hundreths;
        return Double.parseDouble(number);
    }

    private void setInitialPosition(LineChart<Double, Double> graph, Wave wave, Circle point, double x) {
        graph.boundsInLocalProperty().addListener((observable, oldBounds, newBounds) -> {
            // Create a timer that delays the point position update
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> updatePointPosition(graph, wave, point, x)));
            timeline.play();
        });
    }

    // Create a static point to a pane based on graph
    private Circle addStaticPoint(Wave wave, LineChart<Double, Double> graph, AnchorPane pane, double x) {
        Circle point = new Circle(5, RESULT_COLOR);
        setInitialPosition(graph, wave, point, x);
        pane.getChildren().add(point);
        return point;
    }

    private void updateText(Wave wave, Text text, double x) {
        Double result = wave.getFunction().apply(x);
        text.setText(String.format("%.2f", result));
    }

    // Calculate the position of a point based on the graph
    private void updatePointPosition(LineChart<Double, Double> graph, Wave wave, Circle point, double x) {
        ValueAxis xAxis = (ValueAxis) graph.getXAxis();
        ValueAxis yAxis = (ValueAxis) graph.getYAxis();
        double displayPositionX = xAxis.getDisplayPosition(x);
        double displayPositionY = yAxis.getDisplayPosition(wave.getFunction().apply(x));
        point.setCenterX(displayPositionX + leftOffset);
        point.setCenterY(displayPositionY + topOffset);
    }

    private void updateOutput(Wave outputWave) {
        XYChart.Series<Double, Double> series = outputWave.getSeries();
        outputGraph.getData().add(series);
    }

    private boolean isDoubleEqual(double a, double b) {
        return Math.abs(a - b) < tolerance;
    }

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
