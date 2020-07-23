package alistar.miniwidgets.loading;

public interface Loading {

    void setProgress(float progress);

    void setRotationAnimation(boolean rotationAnimation);

    void setLoading(boolean loading);

    void setUserRotationAnimationWithProgress(boolean userRotationAnimationWithProgress);

    void setAnimatedProgress(boolean animateProgress);

    void setProgressColor(int progressColor);

    void setBackgroundStrokeColor(int backgroundStrokeColor);

}
