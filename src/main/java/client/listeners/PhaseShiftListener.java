package client.listeners;

import javafx.scene.chart.LineChart;
import org.jspace.Space;

public class PhaseShiftListener implements Runnable {

    private LineChart<Double, Double> lineChart;
    private Space space;

    public PhaseShiftListener(LineChart<Double, Double> lineChart, Space space) {
        this.lineChart = lineChart;
        this.space = space;
    }

    @Override
    public void run() {
        while (true) {
            // Get from space
        }
    }
}
