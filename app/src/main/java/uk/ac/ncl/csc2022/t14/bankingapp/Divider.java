package uk.ac.ncl.csc2022.t14.bankingapp;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Sam on 19/02/2015.
 * Use as a divider/ separator between different sections on a screen. Can be used
 * by yourLayout.addView(new Divider(context));
 */
public class Divider extends ImageView {

    LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);


    public Divider(Context context) {
        super(context);
        setLayoutParams(lp);
        setBackgroundColor(Color.BLACK);
    }
}
