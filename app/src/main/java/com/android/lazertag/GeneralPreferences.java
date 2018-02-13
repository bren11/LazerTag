package com.android.lazertag;

import android.graphics.drawable.Drawable;

/**
 * Created by gar_ndsherbakov on 2/9/2018.
 */

class GeneralPreferences {

    private String name;
    private int target;
    private int[] crosshairs;
    private int[] targets;
    private int crosshair;

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
        this.target = 0;
        this.crosshair = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getCrosshairs() {
        return crosshairs;
    }

    public int getCrosshair() {
        return crosshair;
    }

    public void setCrosshair(int crosshair) {
        this.crosshair = crosshair;
    }

    public int[] getTargets() {
        return targets;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
