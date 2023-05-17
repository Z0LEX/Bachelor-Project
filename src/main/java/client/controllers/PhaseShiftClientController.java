package client.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;

public class PhaseShiftClientController {

    @FXML
    public LineChart<Double, Double> lineGraphs;

    @FXML
    public VBox root;
    @FXML
    public AnchorPane graphContainer;
}
