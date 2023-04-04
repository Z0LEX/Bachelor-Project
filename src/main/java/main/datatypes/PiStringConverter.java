package main.datatypes;

import javafx.util.StringConverter;

public class PiStringConverter extends StringConverter<Double> {

    public static final double ZERO = 1e-6;

    @Override
    public String toString(Double value) {
        if (value.equals(-Math.PI)) {
            return "-π";
        } else if (value.equals(Math.PI)) {
            return "π";
        } else if (Math.abs(value) < ZERO) {
            return "0";
        } else if (Math.abs(Math.abs(value) - 3*Math.PI/4) < ZERO) {
            String sign = value < 0 ? "-" : "";
            return sign + "3π/4";
        } else {
            int denominator = (int) Math.round(1.0 / (value / Math.PI));
            String numerator = denominator < 0 ? "-π/" : "π/";
            return numerator + Math.abs(denominator);
        }
    }

    @Override
    public Double fromString(String value) {
        if (value.equals("π")) {
            return Math.PI;
        } else {
            return Double.valueOf(value);
        }
    }
}