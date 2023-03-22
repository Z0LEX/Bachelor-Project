package client.datatypes;

import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CenterOfMass {
    public static final double DETAIL = 0.01;
    public static final double RANGE = 1;

    private XYChart<Double, Double> graph;

    public CenterOfMass(XYChart<Double, Double> graph) {
        this.graph = graph;
    }

    public void plot(double weightRe) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        plotWeight(0, weightRe, series);
        graph.getData().add(series);
    }

    private void plotWeight(double re, double im, XYChart.Series<Double, Double> series) {
        XYChart.Data<Double, Double> weightData = new XYChart.Data<>(re, im);
        Circle weight = new Circle(5);
        weight.setFill(Color.DARKRED);
        weightData.setNode(weight);
        series.getData().add(weightData);
    }

    public void clear() {
        graph.getData().clear();
    }
}
