package com.android.lazertag;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by nicho on 1/6/2018.
 */

public class CrossHairPicker {
    Drawable crossHair;

    public CrossHairPicker(Context context, String Hair) {
        if (Hair.equals("GLogo")) {
            crossHair = context.getResources().getDrawable(R.drawable.gisforgitgud, context.getTheme());
        } else if (Hair.equals("Pentacle")) {
            crossHair = context.getResources().getDrawable(R.drawable.pentacle, context.getTheme());
        } else if (Hair.equals("Tryangle")) {
            crossHair = context.getResources().getDrawable(R.drawable.tryangle, context.getTheme());
        } else if (Hair.equals("Zelda")) {
            crossHair = context.getResources().getDrawable(R.drawable.zelda, context.getTheme());
        } else if (Hair.equals("nope")) {
            crossHair = context.getResources().getDrawable(R.drawable.ic_action_info, context.getTheme());
        }
    }

    public Drawable getCrossHair() {
        return crossHair;
    }

}
