package client.application;

import org.jspace.Space;

public interface SpaceAwareController {
    void setSpace(Space space);

    void startListener();

    void setStageManager(ClientStageManager stageManager);
}
