package alistar.miniwidgets.loading;

public interface Loading {

    void setProgress(float progress);

    float getProgress();

    void setRotationAnimation(boolean rotationAnimation);

    void setLoading(boolean loading);

    void setUserRotationAnimationWithProgress(boolean userRotationAnimationWithProgress);

    void setAnimatedProgress(boolean animateProgress);

    void setProgressColor(int progressColor);

    int getProgressColor();

    void setBackgroundStrokeColor(int backgroundStrokeColor);

    int getBackgroundStrokeColor();

    void setBackgroundStrokeWidth(float backgroundStrokeWidth);

    float getBackgroundStrokeWidth();

    void setProgressStrokeWidth(float progressStrokeWidth);

    float getProgressStrokeWidth();

}
