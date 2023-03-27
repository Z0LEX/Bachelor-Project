package client.listeners;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

public class PhaseShiftListener implements Runnable {

    private LineChart<Double, Double> lineChartSum;
    private LineChart<Double, Double> lineChartResult;
    private Space space;

    public PhaseShiftListener(LineChart<Double, Double> lineChartSum, LineChart<Double, Double> lineChartResult, Space space) {
        this.lineChartSum = lineChartSum;
        this.lineChartResult = lineChartResult;
        this.space = space;

        // Initialize the result chart given from host
        try {
            Object[] phaseData = space.get(new ActualField("Phase shift result"), new FormalField(double[][].class));
            double[][] data = (double[][]) phaseData[1];
            XYChart.Series<Double, Double> series = arrayToSeries(data);
            Platform.runLater(() -> {
                lineChartResult.getData().clear();
                lineChartResult.getData().add(0, series);
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Update the sum graph (top) with new values given from host
    @Override
    public void run() {
        while (true) {
            try {
                Object[] phaseData = space.get(new ActualField("Phase shift"), new FormalField(double[][].class));
                double[][] data = (double[][]) phaseData[1];
                XYChart.Series<Double, Double> series = arrayToSeries(data);
                Platform.runLater(() -> {
                    lineChartSum.getData().clear();
                    lineChartSum.getData().add(0, series);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private XYChart.Series<Double, Double> arrayToSeries(double[][] data) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        for (double[] point : data) {
            series.getData().add(new XYChart.Data<>(point[0], point[1]));
        }
        return series;
    }
}

