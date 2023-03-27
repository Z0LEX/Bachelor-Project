package main.components;

import main.controllers.WaveController;
import main.datatypes.FrequencyGraph;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

// Wave component using the design in wave.fxml
public class Wave {
    private double frequency;
    private static double offset = 0;
    private static double phaseShift = Math.PI / 2;
    private static double amplitude = 1;
    private double range = 1;
    private Function<Double, Double> function;
    private FrequencyGraph graph;
    private AnchorPane root;
    private LineChart<Double, Double> lineChart;
    private WaveController waveController;

    public Wave(double frequency) {
        this.frequency = frequency;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();
            lineChart = waveController.getLineGraph();
            graph = new FrequencyGraph(lineChart, range);
            function = x -> getWave(x, frequency);
            plotFunction(function);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Wave(double frequency, LineChart<Double, Double> lineChart) {
        this.frequency = frequency;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();
            graph = new FrequencyGraph(lineChart, range);
            function = x -> getWave(x, frequency, 0);
            plotFunction(function);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Warning: Using this constructor leaves the frequency null
    public Wave(Function<Double, Double> function, LineChart<Double, Double> lineChart) {
        this.function = function;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();
            graph = new FrequencyGraph(lineChart, range);
            plotFunction(function);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Wave(Function<Double, Double> function) {
        this.function = function;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();
            lineChart = waveController.getLineGraph();
            graph = new FrequencyGraph(lineChart, range);
            plotFunction(function);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void plotFunction(Function<Double, Double> function) {
        graph.plot(function);
    }

    public static double getWave(double x, double f) {
        if (f == 0) {
            return 0;
        }
        return amplitude * Math.sin(2 * Math.PI * f * x + phaseShift) + offset;
    }
    public static double getWave(double x, double f, double phaseShift) {
        if (f == 0) {
            return 0;
        }
        return amplitude * Math.sin(2 * Math.PI * f * x + phaseShift) + offset;
    }

    public AnchorPane getRoot() {
        return root;
    }

    public Function<Double, Double> getFunction() {
        return function;
    }

    public void clear() {
        graph.clear();
    }

    public static Function<Double, Double> sumWaves(ArrayList<Wave> waves) {
        return x -> {
            double sum = 0;
            for (Wave wave : waves) {
                sum += wave.getFunction().apply(x);
            }
            return sum;
        };
    }

    public double getFrequency() {
        return frequency;
    }

    public void setLineChart(LineChart<Double, Double> lineChart) {
        this.lineChart = lineChart;
        graph = new FrequencyGraph(lineChart, range);
    }

    public void setFunction(Function<Double, Double> function) {
        this.function = function;
        graph.plot(function);
    }

    public void setPhaseShift(double phaseShift) {
        Wave.phaseShift = phaseShift;
        setFunction(x -> getWave(x, frequency, phaseShift));
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
        setFunction(x -> getWave(x, frequency, phaseShift));
    }

    public FrequencyGraph getGraph() {
        return graph;
    }

    public void setOffset(double offset) {
        this.offset = offset;
        setFunction(x -> getWave(x, frequency));
    }
}
