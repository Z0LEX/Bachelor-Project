package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MultiplicationInputController {

    @FXML
    private Label sign;

    @FXML
    private Label integerNumber;

    @FXML
    private Label tenthDecimal;

    @FXML
    private Label hundredthsDecimal;

    @FXML
    void wheel1Down(ActionEvent event) {
        toggleSign();
    }

    @FXML
    void wheel2Down(ActionEvent event) {
        decrementNumber(integerNumber);
    }

    @FXML
    void wheel3Down(ActionEvent event) {
        decrementNumber(tenthDecimal);
    }

    @FXML
    void wheel4Down(ActionEvent event) {
        decrementNumber(hundredthsDecimal);
    }

    @FXML
    void wheel1Up(ActionEvent event) {
        toggleSign();
    }

    @FXML
    void wheel2Up(ActionEvent event) {
        incrementNumber(integerNumber);
    }

    @FXML
    void wheel3Up(ActionEvent event) {
        incrementNumber(tenthDecimal);
    }

    @FXML
    void wheel4Up(ActionEvent event) {
        incrementNumber(hundredthsDecimal);
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

    private void toggleSign() {
        if (sign.getText().equals("+")) {
            sign.setText("-");
        } else {
            sign.setText("+");
        }
    }

    public Label getSign() {
        return sign;
    }

    public Label getIntegerNumber() {
        return integerNumber;
    }

    public Label getTenthDecimal() {
        return tenthDecimal;
    }

    public Label getHundredthsDecimal() {
        return hundredthsDecimal;
    }
}
