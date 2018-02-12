package com.android.lazertag;

/**
 * Created by gar_ndsherbakov on 2/9/2018.
 */

class GeneralPreferences {
    private static final GeneralPreferences ourInstance = new GeneralPreferences();

    static GeneralPreferences getInstance() {
        return ourInstance;
    }

    private GeneralPreferences() {
    }
}
