package client.listeners;

import client.application.ClientStageManager;
import javafx.application.Platform;
import org.jspace.ActualField;
import org.jspace.Space;

public class FourierMachineListener implements Runnable {
    private Space space;
    private ClientStageManager stageManager;

    public FourierMachineListener(Space space, ClientStageManager stageManager) {
        this.space = space;
        this.stageManager = stageManager;
    }

    @Override
    public void run() {
        try {
            space.get(new ActualField("Show fourier machine"));
            Platform.runLater(() -> {
                stageManager.setScene("/client-fourier-machine.fxml");
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
