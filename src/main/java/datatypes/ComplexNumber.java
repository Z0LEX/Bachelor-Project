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

    public ComplexNumber multiply(ComplexNumber other) {
        double re = this.re * other.re - this.im * other.im;
        double im = this.re * other.im + this.im * other.re;
        return new ComplexNumber(re, im);
    }

    public static ComplexNumber exp(double theta) {
        double re = Math.cos(theta);
        double im = Math.sin(theta);
        return new ComplexNumber(re, im);
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