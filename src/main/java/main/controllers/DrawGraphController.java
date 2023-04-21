package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import main.components.Wave;
import main.datatypes.FrequencyGraph;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DrawGraphController implements Initializable {

    @FXML
    private ScatterChart<Double, Double> scatterChart;

    private XYChart.Series<Double, Double> series;
    private FrequencyGraph graph;
    private double detail = 0.015;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        series = new XYChart.Series<>();
        graph = new FrequencyGraph(scatterChart, 1);
        ValueAxis<Double> yAxis = (ValueAxis<Double>) scatterChart.getYAxis();
        double upperBound = yAxis.getUpperBound();
        Wave wave = new Wave(3, upperBound-1);
        // Create a list of points to be removed from the plot. There are Math.floor(1/detail) points.
        ArrayList<Integer> missingPoints = new ArrayList();
        missingPoints.add(18);
        missingPoints.add(29);
        missingPoints.add(60);
        graph.plotIncomplete(wave.getFunction(), detail, missingPoints);

        scatterChart.setOnMouseClicked(event -> {
            int leftOffset = 28;
            int topOffset = 14;
            double lowerBound = -upperBound;
            double clickedX = event.getX();
            double clickedY = event.getY();
            double x = scatterChart.getXAxis().getValueForDisplay(clickedX - leftOffset).doubleValue();
            double y = scatterChart.getYAxis().getValueForDisplay(clickedY - topOffset).doubleValue();
            // If clicked outside the plot area, do nothing
            if (x < 0 || x > 1 || y < lowerBound || y > upperBound) {
                return;
            }
            if (!scatterChart.getData().contains(series)) {
                scatterChart.getData().add(series);
            }
            series.getData().add(new XYChart.Data<>(x, y));
            System.out.println("Clicked on (" + clickedX + "," + clickedY + ")");
        });
    }
}
