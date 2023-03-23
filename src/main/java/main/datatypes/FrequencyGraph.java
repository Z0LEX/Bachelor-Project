package main.datatypes;

import java.util.function.Function;

import javafx.scene.chart.XYChart;

public class FrequencyGraph implements Graph {

    public static final double DETAIL = 0.005;
    private XYChart<Double, Double> graph;
    private double range;

    public FrequencyGraph(XYChart<Double, Double> graph, final double range) {
        this.graph = graph;
        this.range = range;
    }

    public void plot(Function<Double, Double> function) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        for (double x = 0; x <= range; x += DETAIL) {
            plotPoint(x, function.apply(x), series);
        }
        graph.getData().add(series);
    }

    public void plotPoint(double x, double y,
                          XYChart.Series<Double, Double> series) {
        XYChart.Data<Double, Double> data = new XYChart.Data<>(x, y);
        series.getData().add(data);
    }

    public void clear() {
        graph.getData().clear();
    }

}