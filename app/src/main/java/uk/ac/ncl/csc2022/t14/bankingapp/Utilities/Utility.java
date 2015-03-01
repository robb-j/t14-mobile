package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

/**
 * Created by Sam on 01/03/2015.
 * Useful utilities to be used throughout the app.
 */
public class Utility {

    /**
     * Used to display a double value as currency in GBP.
     * @param d The double to convert to currency.
     * @return
     */
    public static String doubleToCurrency(double d) {
        if (d < 0) {

            return String.format("-£%.2f", Math.abs(d));
        } else {

            return String.format("£%.2f", d);
        }
    }
}
