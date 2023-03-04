package datatypes;

import java.util.ArrayList;
import java.util.function.Function;
import javafx.scene.chart.XYChart;

public class Graph {

    private XYChart<Double, Double> graph;
    private ArrayList<Function> functions;
    private double range;

    public Graph(final XYChart<Double, Double> graph, final double range) {
        this.graph = graph;
        this.range = range;
        functions = new ArrayList<>();
    }

    public void plot(final Function<Double, Double> function) {
        functions.add(function);
        final XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        for (double x = 0; x <= range; x = x + 0.01) {
            plotPoint(x, function.apply(x), series);
        }
        graph.getData().add(series);
    }

    public void combinePlots() {
        final XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();

        for (double x = 0; x <= range; x = x + 0.01) {
            Double tempX = 0.;
            for (Function<Double, Double> function : functions) {
                tempX += function.apply(x);
            }
            if (!functions.isEmpty()) {
                plotPoint(x, tempX, series);
            }
        }
        graph.getData().add(series);
    }

    private void plotPoint(final double x, final double y,
                           final XYChart.Series<Double, Double> series) {
        series.getData().add(new XYChart.Data<Double, Double>(x, y));
    }

    public void clear() {
        graph.getData().clear();
        functions.clear();
    }
}