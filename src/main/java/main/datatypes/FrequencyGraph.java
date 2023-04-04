package main.datatypes;

import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class FrequencyGraph {

    public static final double DETAIL = 0.005;
    private XYChart<Double, Double> graph;
    private double range;
    private XYChart.Series<Double, Double> series;

    public FrequencyGraph(XYChart<Double, Double> graph, final double range) {
        this.graph = graph;
        this.range = range;
    }

    public void plot(Function<Double, Double> function) {
        series = new XYChart.Series<Double, Double>();
        for (double x = 0; x <= range; x += DETAIL) {
            plotPoint(x, function.apply(x), series);
        }
        graph.getData().add(0, series);
    }

    public void overwritePlot(Function<Double, Double> function, int index) {
        series = new XYChart.Series<Double, Double>();
        for (double x = 0; x <= range; x += DETAIL) {
            plotPoint(x, function.apply(x), series);
        }
        graph.getData().set(index, series);
    }

    public void plotPoint(double x, double y,
                          XYChart.Series<Double, Double> series) {
        XYChart.Data<Double, Double> data = new XYChart.Data<>(x, y);
        series.getData().add(data);
    }

    public void clear() {
        graph.getData().clear();
    }

    public void clear(XYChart.Series<Double, Double> data) {
        data.getData().clear();
    }

    public double[][] getDataAsArray() {
        ObservableList<XYChart.Data<Double, Double>> data = graph.getData().get(0).getData();
        int size = data.size();
        double[][] coordinates = new double[size][2];
        for (int i = 0; i < size; i++) {
            coordinates[i][0] = data.get(i).getXValue();
            coordinates[i][1] = data.get(i).getYValue();
        }
        return coordinates;
    }

    public XYChart.Series<Double, Double> getSeries() {
        return series;
    }

}