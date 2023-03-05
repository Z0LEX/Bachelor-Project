package datatypes;

import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.function.Function;


public class EulerGraph implements Graph {

    private XYChart<Double, Double> graph;
    public static Function<Double, Double> function;
    private double range = 1;

    private double frequency = 1;

    public EulerGraph(XYChart<Double, Double> graph) {
        this.graph = graph;
    }

    public void plot(Function<Double, Double> function) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        for (double x = 0; x < range; x = x + 0.001) {
            Double gt = function.apply(x);
            ComplexNumber z = new ComplexNumber(gt, 0).multiply(ComplexNumber.exp(-2 * Math.PI * frequency * x));
            System.out.println(z.toString() + " Frequency: " + frequency);
            plotPoint(z.re(), z.im(), series);
        }
        graph.getData().add(series);
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