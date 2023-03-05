package datatypes;

import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.function.Function;


public class EulerGraph implements Graph {

    public static final double DETAIL = 0.001;
    private XYChart<Double, Double> graph;
    public static Function<Double, Double> function;
    private double range = 1;

    private double frequency = 1;

    public EulerGraph(XYChart<Double, Double> graph) {
        this.graph = graph;
    }

    public void plot(Function<Double, Double> function) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        ComplexNumber weight = new ComplexNumber(0, 0);
        for (double t = 0; t < range; t = t + DETAIL) {
            Double gt = function.apply(t);
            ComplexNumber z = new ComplexNumber(gt, 0).multiply(ComplexNumber.exp(-2 * Math.PI * frequency * t));
            weight = weight.add(z);
            plotPoint(z.re(), z.im(), series);
        }
        weight = weight.multiply(1/(range/DETAIL));
        plotWeight(weight.re(), weight.im(), series);
        graph.getData().add(series);
    }

    private void plotWeight(double re, double im, XYChart.Series<Double, Double> series) {
        XYChart.Data<Double, Double> weightData = new XYChart.Data<>(re, im);
        Circle weight = new Circle(5);
        weight.setFill(Color.DARKRED);
        weightData.setNode(weight);
        series.getData().add(weightData);
    }

    public void plotPoint(double x, double y,
                          XYChart.Series<Double, Double> series) {
        XYChart.Data<Double, Double> data = new XYChart.Data<>(x, y);
        Circle point = new Circle(1);
        point.setFill(Color.ORANGE);
        data.setNode(point);
        series.getData().add(data);
    }

    public void clear() {
        graph.getData().clear();
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setFunction(Function<Double, Double> function) {
        this.function = function;
    }
    public Function<Double, Double> getFunction() {
        return function;
    }
}