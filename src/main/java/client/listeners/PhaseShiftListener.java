package client.listeners;

import client.application.ClientStageManager;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

public class PhaseShiftListener implements Runnable {

    private LineChart<Double, Double> lineGraphs;
    private Space space;
    private ClientStageManager stageManager;

    public PhaseShiftListener(LineChart<Double, Double> lineGraphs, Space space, ClientStageManager stageManager) {
        this.lineGraphs = lineGraphs;
        this.space = space;
        this.stageManager = stageManager;

        // Initialize the result chart given from host
        try {
            Object[] phaseData = space.get(new ActualField("Phase shift result"), new FormalField(double[][].class));
            double[][] data = (double[][]) phaseData[1];
            XYChart.Series<Double, Double> resultSeries = arrayToSeries(data);
            // Add listener to linegraphs series. Change line color for result.
            lineGraphs.getData().addListener((ListChangeListener<XYChart.Series<Double, Double>>) change -> {
                while (change.next()) {
                    for (XYChart.Series<Double, Double> series : change.getAddedSubList()) {
                        if (series == resultSeries) {
                            Node line = series.getNode().lookup(".chart-series-line");
                            line.setStyle("-fx-stroke: #CC5500; -fx-stroke-width: 5px");
                        }
                    }
                }
            });
            Platform.runLater(() -> {
                lineGraphs.getData().add(0, resultSeries);
                lineGraphs.getData().add(1, new XYChart.Series<>());
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Update the sum graph with new values given from host
    @Override
    public void run() {
        while (true) {
            try {
                Object[] phaseData = space.get(new ActualField("Phase shift"), new FormalField(double[][].class));
                double[][] data = (double[][]) phaseData[1];
                XYChart.Series<Double, Double> series = arrayToSeries(data);
                Platform.runLater(() -> {
                    lineGraphs.getData().set(1, series);
                });
                Object[] showPhaseShifts = space.query(new ActualField("Show phase shift"));
                Platform.runLater(() -> {
                    stageManager.setScene("/client-phase-shift.fxml");
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

