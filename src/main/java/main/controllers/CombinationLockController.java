package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CombinationLockController {

    @FXML
    private Label wheel1Number;

    @FXML
    private Label wheel2Number;

    @FXML
    private Label wheel3Number;

    @FXML
    private Label wheel4Number;

    @FXML
    void wheel1Down(ActionEvent event) {
        decrementNumber(wheel1Number);
    }

    @FXML
    void wheel2Down(ActionEvent event) {
        decrementNumber(wheel2Number);
    }

    @FXML
    void wheel3Down(ActionEvent event) {
        decrementNumber(wheel3Number);
    }

    @FXML
    void wheel4Down(ActionEvent event) {
        decrementNumber(wheel4Number);
    }

    @FXML
    void wheel1Up(ActionEvent event) {
        incrementNumber(wheel1Number);
    }

    @FXML
    void wheel2Up(ActionEvent event) {
        incrementNumber(wheel2Number);
    }

    @FXML
    void wheel3Up(ActionEvent event) {
        incrementNumber(wheel3Number);
    }

    @FXML
    void wheel4Up(ActionEvent event) {
        incrementNumber(wheel4Number);
    }

    private void incrementNumber(Label numberLabel) {
        int currentNumber = Integer.parseInt(numberLabel.getText());
        if (currentNumber == 9) {
            currentNumber = 0;
        } else {
            currentNumber++;
        }

        numberLabel.setText(Integer.toString(currentNumber));
    }

    private void decrementNumber(Label numberLabel) {
        int currentNumber = Integer.parseInt(numberLabel.getText());
        if (currentNumber == 0) {
            currentNumber = 9;
        } else {
            currentNumber--;
        }
        numberLabel.setText(Integer.toString(currentNumber));
    }

    public Label getWheel1Number() {
        return wheel1Number;
    }

    public Label getWheel2Number() {
        return wheel2Number;
    }

    public Label getWheel3Number() {
        return wheel3Number;
    }

    public Label getWheel4Number() {
        return wheel4Number;
    }
}
