package main.components;

import main.controllers.CombinationLockController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CombinationLock {
    private HBox root;
    private CombinationLockController controller;
    private int first;
    private int second;
    private int third;
    private int forth;


    public CombinationLock(int first, int second, int third, int fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.forth = fourth;



        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/combination-lock.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CombinationLockController getController() {
        return controller;
    }

    public HBox getRoot() {
        return root;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() {
        return third;
    }

    public int getForth() {
        return forth;
    }



}
