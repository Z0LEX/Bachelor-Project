package datatypes;

import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.function.Function;


public class EulerGraph implements Graph {

    public static final double DETAIL = 0.004;
    private XYChart<Double, Double> graph;
    private static Function<Double, Double> function;
    private static ArrayList<FunctionObserver> observers = new ArrayList<>();

    private double range = 1;

    private double cyclesPrSec = 0;

    public EulerGraph(XYChart<Double, Double> graph) {
        this.graph = graph;
        registerObserver(newFunction -> {
            clear();
            plot(function);
        });
    }

    public void plot(Function<Double, Double> function) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        ComplexNumber weight = new ComplexNumber(0, 0);
        for (double t = 0; t < range; t = t + DETAIL) {
            Double gt = function.apply(t);
            ComplexNumber z = new ComplexNumber(gt, 0).multiply(ComplexNumber.exp(-2 * Math.PI * cyclesPrSec * t));
            weight = weight.add(z);
            plotPoint(z.re(), z.im(), series);
        }
        XYChart.Series<Double, Double> weightSeries = new XYChart.Series<Double, Double>();
        weight = weight.multiply(1 / (range / DETAIL));
        plotWeight(weight.re(), weight.im(), weightSeries);
        graph.getData().addAll(series, weightSeries);
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
//        Circle point = new Circle(1);
//        point.setFill(Color.ORANGE);
//        data.setNode(point);
        series.getData().add(data);
    }

    public void clear() {
        graph.getData().clear();
    }

    public void setCyclesPrSec(double cyclesPrSec) {
        this.cyclesPrSec = cyclesPrSec;
    }

    public static void setFunction(Function<Double, Double> newFunction) {
        function = newFunction;
        for (FunctionObserver observer : observers) {
            observer.onFunctionChanged(newFunction);
        }
    }

    public Function<Double, Double> getFunction() {
        return function;
    }

    public static void registerObserver(FunctionObserver observer) {
        observers.add(observer);
    }

}

interface FunctionObserver {
    void onFunctionChanged(Function<Double, Double> newFunction);
}