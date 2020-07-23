package alistar.miniwidgets.utils;

import android.view.animation.Interpolator;

public class CircOutInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        return (float) ((float) 1 - Math.pow(1 - input, 3));
    }

}
