package main.datatypes;

import java.util.function.Function;


public class EulerGraph {

    public static final double DETAIL = 0.01;
    private static final double RANGE = 1;
    private Function<Double, Double> function;
    private double cyclesPrSec = 0;

    public EulerGraph(Function<Double, Double> function, Double frequency) {
        this.function = function;
        cyclesPrSec = frequency;
    }

    public ComplexNumber computeWeight() {
        ComplexNumber weight = new ComplexNumber(0, 0);
        for (double t = 0; t < RANGE; t = t + DETAIL) {
            Double gt = function.apply(t);
            ComplexNumber z = new ComplexNumber(gt, 0).multiply(ComplexNumber.exp(-2 * Math.PI * cyclesPrSec * t));
            weight = weight.add(z);
        }
        weight = weight.divide(RANGE / DETAIL);
        return weight;
    }


    public void setCyclesPrSec(double cyclesPrSec) {
        this.cyclesPrSec = cyclesPrSec;
    }

    public Function<Double, Double> getFunction() {
        return function;
    }

}