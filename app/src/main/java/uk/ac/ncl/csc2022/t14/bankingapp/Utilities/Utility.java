package uk.ac.ncl.csc2022.t14.bankingapp.Utilities;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;

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


    /**
     * Use this to add a divider with yourLayout.addView(Utility.divider(getActivity()));
     * @param context The context for the ImageView.
     * @return
     */
    public static ImageView divider(Context context) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        imageView.setLayoutParams(lp);
        imageView.setBackgroundColor(Color.BLACK);

        return imageView;
    }



}
