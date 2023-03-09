package components;

import controllers.WaveController;
import datatypes.FrequencyGraph;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.function.Function;

// Wave component using the design in wave.fxml
public class Wave {
    private double frequency;
    private double offset = 0;
    private double phaseShift = Math.PI / 2;
    private double amplitude = 1;
    private double range = 5;
    private Function<Double, Double> function;
    private FrequencyGraph graph;
    private AnchorPane root;
    private WaveController waveController;

    public Wave(double frequency) {
        this.frequency = frequency;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();

            graph = new FrequencyGraph(waveController.getLineGraph(), range);
            function = x -> getWave(x, frequency);
            plotFunction(function);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Warning: Using this constructor leaves the frequency null
    public Wave(Function<Double, Double> function) {
        this.function = function;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wave.fxml"));
            root = fxmlLoader.load();
            waveController = fxmlLoader.getController();

            graph = new FrequencyGraph(waveController.getLineGraph(), range);
            plotFunction(function);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void plotFunction(Function<Double, Double> function) {
        graph.plot(function);
    }

    private double getWave(double x, double f) {
        return amplitude * Math.sin(2 * Math.PI * f * x + phaseShift) + offset;
    }

    public AnchorPane getRoot() {
        return root;
    }

    public WaveController getWaveController() {
        return waveController;
    }

    public Function<Double, Double> getFunction() {
        return function;
    }

    public void clear() {
        graph.clear();
    }
}
