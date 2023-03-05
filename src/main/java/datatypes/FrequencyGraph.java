package datatypes;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.scene.chart.XYChart;


public class FrequencyGraph implements Graph {

    public static final double DETAIL = 0.01;
    private XYChart<Double, Double> graph;
    private ArrayList<Function> functions;
    private double range;

    public FrequencyGraph(XYChart<Double, Double> graph, final double range) {
        this.graph = graph;
        this.range = range;
        functions = new ArrayList<>();
    }

    public void plot(Function<Double, Double> function) {
        functions.add(function);
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        for (double x = 0; x <= range; x += DETAIL) {
            plotPoint(x, function.apply(x), series);
        }
        graph.getData().add(series);
    }

    public void combinePlots() {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        Function<Double, Double> sum = sumFunction;
        EulerGraph.setFunction(sum);
//        clear();
        for (double x = 0; x <= range; x += DETAIL) {
            plotPoint(x, sum.apply(x), series);
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
        functions.clear();
    }

    private Function<Double, Double> sumFunction = x -> {
        double sum = 0;
        for (Function<Double, Double> function : functions) {
            sum += function.apply(x);
        }
        return sum;
    };
}