import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by nsherb on 2/9/18.
 */



class GeneralPreferences {

    private String name;
    private Drawable crossHair;
    private Drawable target;

    private static final GeneralPreferences ourInstance = new GeneralPreferences();

    static GeneralPreferences getInstance() {
        return ourInstance;
    }

    private GeneralPreferences() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getCrossHair() {
        return crossHair;
    }

    public void setCrossHair(Drawable crossHair) {
        this.crossHair = crossHair;
    }

    public Drawable getTarget() {
        return target;
    }

    public void setTarget(Drawable target) {
        this.target = target;
    }

}
