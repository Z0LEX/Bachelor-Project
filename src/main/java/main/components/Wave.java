package main.components;

import javafx.scene.chart.XYChart;
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
    private double frequency = 0;
    private double offset = 0;
    private double phaseShift = 0;
    private double amplitude = 1;
    private double range = 1;
    private Function<Double, Double> function;
    private FrequencyGraph graph;
    private AnchorPane root;
    private LineChart<Double, Double> lineChart;
    private WaveController waveController;

    public Wave(double frequency, double amplitude) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();
            lineChart = waveController.getLineGraph();
            graph = new FrequencyGraph(lineChart, range);
            function = x -> getWave(x, frequency, amplitude, phaseShift);
            plotFunction(function);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Wave(double frequency, double amplitude, LineChart<Double, Double> lineChart) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.lineChart = lineChart;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();
            graph = new FrequencyGraph(lineChart, range);
            function = x -> getWave(x, frequency, amplitude, 0);
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

    public double getWave(double x, double f, double a, double phaseShift) {
//        if (f == 0) {
//            return 0;
//        }
        return a * Math.cos(2 * Math.PI * f * x + phaseShift) + offset;
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

    public static Function<Double, Double> multiplyWaves(Wave wave1, Wave wave2) {
        return x -> {
            double sum = 0;
            sum = wave1.getFunction().apply(x) * wave2.getFunction().apply(x);
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
    }

    public void setPhaseShift(double phaseShift) {
        this.phaseShift = phaseShift;
        setFunction(x -> getWave(x, frequency, amplitude, phaseShift));
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
        setFunction(x -> getWave(x, frequency, amplitude, phaseShift));
    }

    public FrequencyGraph getGraph() {
        return graph;
    }

    public void setOffset(double offset) {
        this.offset = offset;
        setFunction(x -> getWave(x, frequency, amplitude, phaseShift));
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
        setFunction(x -> getWave(x, frequency, amplitude, phaseShift));
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double getPhaseShift() {
        return phaseShift;
    }

    public XYChart.Series<Double, Double> getSeries() {
        return graph.getSeries();
    }

    public LineChart<Double, Double> getLineChart() {
        return lineChart;
    }
}
