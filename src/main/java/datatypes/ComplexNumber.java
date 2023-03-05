package datatypes;
public class ComplexNumber {

    private final double re;
    private final double im;

    public static final ComplexNumber I = new ComplexNumber(0, 1);

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double re() {
        return re;
    }

    public double im() {
        return im;
    }

    public ComplexNumber add(ComplexNumber other) {
        double realSum = this.re + other.re;
        double imaginarySum = this.im + other.im;
        return new ComplexNumber(realSum, imaginarySum);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        double re = this.re * other.re - this.im * other.im;
        double im = this.re * other.im + this.im * other.re;
        return new ComplexNumber(re, im);
    }

    public ComplexNumber multiply(double factor) {
        double realProduct = this.re * factor;
        double imaginaryProduct = this.im * factor;
        return new ComplexNumber(realProduct, imaginaryProduct);
    }

    public static ComplexNumber exp(double theta) {
        double re = Math.cos(theta);
        double im = Math.sin(theta);
        return new ComplexNumber(re, im);
    }

    public ComplexNumber divide(double divisor) {
        double realQuotient = this.re / divisor;
        double imaginaryQuotient = this.im / divisor;
        return new ComplexNumber(realQuotient, imaginaryQuotient);
    }

    @Override
    public String toString()
    {
        String re = this.re+"";
        String im = "";
        if(this.im < 0)
            im = this.im+"i";
        else
            im = "+"+this.im+"i";
        return re+im;
    }


}