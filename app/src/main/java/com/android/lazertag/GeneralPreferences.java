package com.android.lazertag;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import java.util.prefs.Preferences;

/**
 * Created by gar_ndsherbakov on 2/9/2018.
 */

class GeneralPreferences {

    private int[] crosshairs;
    private int[] targets;

    private static GeneralPreferences prefs;

    public static GeneralPreferences getInstance() {
        if(prefs == null) {
            prefs = new GeneralPreferences();
        }
        return  prefs;
    }

    private GeneralPreferences() {
        this.crosshairs = new int[]{R.drawable.gisforgitgud, R.drawable.pentacle, R.drawable.tryangle, R.drawable.zelda, R.drawable.spin};
        this.targets = new int[]{R.drawable.pentacle,R.drawable.tryangle,R.drawable.zelda};
    }

    public int[] getCrosshairs() {
        return crosshairs;
    }

    public int[] getTargets() {
        return targets;
    }
}
