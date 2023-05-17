package client.listeners;

import client.controllers.PhaseShiftClientController;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

public class PhaseShiftListener implements Runnable {
    private PhaseShiftClientController controller;

    private LineChart<Double, Double> lineGraphs;
    private Space space;

    public PhaseShiftListener(PhaseShiftClientController controller, Space space) {
        this.controller = controller;
        this.lineGraphs = controller.lineGraphs;
        this.space = space;

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
                Object[] show = space.get(new ActualField("Show"));
                controller.graphContainer.setVisible(true);
                controller.root.setStyle("-fx-background-color: lightgray");
                Object[] phaseData = space.get(new ActualField("Phase shift"), new FormalField(double[][].class));
                double[][] data = (double[][]) phaseData[1];
                XYChart.Series<Double, Double> series = arrayToSeries(data);
                Platform.runLater(() -> {
                    lineGraphs.getData().set(1, series);
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

