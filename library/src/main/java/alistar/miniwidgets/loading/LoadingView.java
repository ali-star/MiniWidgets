package alistar.miniwidgets.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import alistar.miniwidgets.R;
import alistar.miniwidgets.utils.CircOutInterpolator;
import alistar.miniwidgets.utils.Utils;

public class LoadingView extends View implements Loading {

    private int progressColor = Color.parseColor("#3D72DE");
    private int backgroundStrokeColor = Color.parseColor("#1AB9C0CB");
    private float backgroundStrokeWidth = Utils.dipToPix(2);
    private float progressStrokeWidth = Utils.dipToPix(4);
    private Mode mode = Mode.LOADING;
    private float progress = 25;

    private Paint backgroundStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint progressStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();

    private float rotationAngle = 0;
    private boolean userRotationAnimation = true;
    private boolean userRotationAnimationWithProgress = false;
    private ValueAnimator rotateAnimator, progressAnimator;

    private CircOutInterpolator circOutInterpolator = new CircOutInterpolator();

    private boolean animateProgress = true;

    public LoadingView(Context context) {
        super(context);
        init(null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
            progressColor = typedArray.getColor(R.styleable.LoadingView_lv_progressColor, progressColor);
            backgroundStrokeColor = typedArray.getColor(R.styleable.LoadingView_lv_backgroundStrokeColor, backgroundStrokeColor);
            backgroundStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.LoadingView_lv_backgroundStrokeWidth, (int) backgroundStrokeWidth);
            progressStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.LoadingView_lv_progressStrokeWidth, (int) progressStrokeWidth);
            mode = Mode.getByValue(typedArray.getInt(R.styleable.LoadingView_lv_mode, 0));
            typedArray.recycle();
        }

        progress = mode == Mode.LOADING ? 25 : 0;

        backgroundStrokePaint.setStyle(Paint.Style.STROKE);
        backgroundStrokePaint.setStrokeWidth(backgroundStrokeWidth);
        backgroundStrokePaint.setColor(backgroundStrokeColor);
        backgroundStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        progressStrokePaint.setStyle(Paint.Style.STROKE);
        progressStrokePaint.setStrokeWidth(progressStrokeWidth);
        progressStrokePaint.setColor(progressColor);
        progressStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        rotateAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotationAngle = (float) animation.getAnimatedValue();
                setRotation(rotationAngle);
            }
        });
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setDuration(500);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float min = Math.min(w, h);
        float highStroke = Math.max(progressStrokeWidth, backgroundStrokeWidth);
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float angle = 360 * progress / 100;
        canvas.drawOval(rectF, backgroundStrokePaint);
        canvas.drawArc(rectF, -90, angle, false, progressStrokePaint);
    }

    @Override
    public void setProgress(float progress) {
        setProgress(progress, true);
    }

    private void setProgress(float progress, boolean changeRotationAnimation) {
        mode = Mode.PROGRESS;
        if (changeRotationAnimation) {
            if (userRotationAnimationWithProgress && !rotateAnimator.isRunning())
                setRotationAnimation(true);
            else if (!userRotationAnimationWithProgress)
                setRotationAnimation(false);
        }
        if (animateProgress) {
            if (progressAnimator == null) {
                progressAnimator = ValueAnimator.ofFloat(this.progress, progress);
                progressAnimator.setInterpolator(circOutInterpolator);
                progressAnimator.setDuration(500);
                progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        LoadingView.this.progress = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
            } else {
                progressAnimator.setFloatValues(this.progress, progress);
            }
            progressAnimator.start();
        } else {
            this.progress = progress;
            invalidate();
        }
    }

    @Override
    public void setRotationAnimation(boolean rotationAnimation) {
        userRotationAnimation = rotationAnimation;
        if (rotationAnimation && getVisibility() == VISIBLE && rotateAnimator != null) {
            rotateAnimator.setFloatValues(0f, 360f);
            rotateAnimator.setInterpolator(new LinearInterpolator());
            rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
            rotateAnimator.start();
        } else if (rotateAnimator != null) {
            rotateAnimator.cancel();
            rotateAnimator.setFloatValues(getRotation(), 0f);
            rotateAnimator.setRepeatCount(0);
            rotateAnimator.setInterpolator(circOutInterpolator);
            rotateAnimator.start();
        } else {
            setRotation(0);
        }
    }

    @Override
    public void setLoading(boolean loading) {
        setProgress(25, false);
        mode = Mode.LOADING;
        setRotationAnimation(true);
    }

    @Override
    public void setUserRotationAnimationWithProgress(boolean userRotationAnimationWithProgress) {
        this.userRotationAnimationWithProgress = userRotationAnimationWithProgress;
        if (mode == Mode.PROGRESS)
            setRotationAnimation(userRotationAnimationWithProgress);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE && userRotationAnimation && rotateAnimator != null)
            rotateAnimator.start();
        else if (rotateAnimator != null) {
            rotateAnimator.cancel();
            setRotation(0);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (userRotationAnimation && rotateAnimator != null)
            rotateAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (rotateAnimator != null)
            rotateAnimator.cancel();
    }

    public enum Mode {
        LOADING(0), PROGRESS(1);

        private int value;

        Mode(int value) {
            this.value = value;
        }

        static Mode getByValue(int value) {
            for (Mode mode : values())
                if (mode.value == value)
                    return mode;
            return LOADING;
        }
    }

    public boolean isRotating() {
        return userRotationAnimation;
    }

    @Override
    public void setAnimatedProgress(boolean animateProgress) {
        this.animateProgress = animateProgress;
    }

    @Override
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressStrokePaint.setColor(progressColor);
        invalidate();
    }

    @Override
    public void setBackgroundStrokeColor(int backgroundStrokeColor) {
        this.backgroundStrokeColor = backgroundStrokeColor;
        backgroundStrokePaint.setColor(backgroundStrokeColor);
        invalidate();
    }

    public void setBackgroundStrokeWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundStrokePaint.setStrokeWidth(backgroundStrokeWidth);
        invalidate();
    }

    public void setProgressStrokeWidth(float progressStrokeWidth) {
        this.progressStrokeWidth = progressStrokeWidth;
        progressStrokePaint.setStrokeWidth(progressStrokeWidth);
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat("progress", progress);
        bundle.putBoolean("userRotationAnimation", userRotationAnimation);
        bundle.putInt("mode", mode.value);
        bundle.putInt("progressColor", progressColor);
        bundle.putInt("backgroundStrokeColor", backgroundStrokeColor);
        bundle.putFloat("backgroundStrokeWidth", backgroundStrokeWidth);
        bundle.putFloat("progressStrokeWidth", progressStrokeWidth);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            progress = bundle.getFloat("progress");
            userRotationAnimation = bundle.getBoolean("userRotationAnimation");
            mode = Mode.getByValue(bundle.getInt("mode"));
            progressColor = bundle.getInt("progressColor");
            backgroundStrokeColor = bundle.getInt("backgroundStrokeColor");
            backgroundStrokeWidth = bundle.getFloat("backgroundStrokeWidth");
            progressStrokeWidth = bundle.getFloat("progressStrokeWidth");
        }
        super.onRestoreInstanceState(BaseSavedState.EMPTY_STATE);
    }
}
