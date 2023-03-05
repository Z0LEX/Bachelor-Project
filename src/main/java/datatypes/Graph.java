package datatypes;

import javafx.scene.chart.XYChart;

import java.util.function.Function;

public interface Graph {

    void plot(Function<Double, Double> function);

    void plotPoint(double x, double y, XYChart.Series<Double, Double> series);

    void clear();
}

