package client.listeners;

import client.datatypes.CenterOfMass;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

public class FrequencyListener implements Runnable {

    private LineChart<Double, Double> lineChart;
    private Space space;

    public FrequencyListener(LineChart<Double, Double> lineChart, Space space) {
        this.lineChart = lineChart;
        this.space = space;
    }

    @Override
    public void run() {
        CenterOfMass centerOfMass = new CenterOfMass(lineChart);
        while (true) {
            try {
                Object[] waveInfoToClient = space.get(new ActualField("Wave center of mass"), new FormalField(Double.class));
                Double weightRe = (Double) waveInfoToClient[1];
                Platform.runLater(() -> {
                    centerOfMass.clear();
                    centerOfMass.plot(weightRe);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
