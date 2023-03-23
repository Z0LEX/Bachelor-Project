package client.listeners;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import main.components.Wave;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import java.util.ArrayList;
import java.util.function.Function;

public class PhaseShiftListener implements Runnable {

    private LineChart<Double, Double> lineChart;
    private Space space;
    private ArrayList<Wave> waveSum;

    public PhaseShiftListener(LineChart<Double, Double> lineChart, Space space, ArrayList<Wave> waveSum) {
        this.lineChart = lineChart;
        this.space = space;
        this.waveSum = waveSum;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] phaseShifts = space.get(new ActualField("Phase shift"), new FormalField(Integer.class), new FormalField(Double.class));
                int index = (int) phaseShifts[1];
                Double newPhase = (Double) phaseShifts[2];
                Wave wave = waveSum.get(index);
                wave.setPhaseShift(newPhase);

                Function<Double, Double> sumFunction = Wave.sumWaves(waveSum);
                Platform.runLater(() -> {
                    lineChart.getData().clear();
                    new Wave(sumFunction, lineChart);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
