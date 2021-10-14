package alistar.miniwidgets.button;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import alistar.miniwidgets.R;
import alistar.miniwidgets.imageview.SVGImageView;
import alistar.miniwidgets.imageview.SvgUtils;
import alistar.miniwidgets.loading.Loading;
import alistar.miniwidgets.loading.LoadingView;
import alistar.miniwidgets.utils.CircOutInterpolator;
import alistar.miniwidgets.utils.Utils;

public class MiniButton extends FrameLayout implements Button, Loading {

    private final Paint shadowPaint = new Paint();
    private final Paint strokePaint = new Paint();
    private final Paint textPaint = new Paint();
    private final Path path = new Path();
    private final RectF baseRectF = new RectF();
    private final RectF animationRectF = new RectF();
    private final Rect textRect = new Rect();
    private int shadowSize = 0;
    private int shadowDy = Utils.dipToPix(4);
    private int textSize = Utils.spToPix(14);
    private int textColor = Color.parseColor("#222222");
    private int cornerRadius = Utils.dipToPix(8);
    private Typeface typeface;
    private String text = null;
    private String textHolder = null;
    private boolean firstTouch = false;
    private AlphaRecView alphaRecView;
    private int firstX = 0;
    private int firstY = 0;
    private int rippleColor = Color.parseColor("#4DFFFFFF");
    private int backgroundColor = Color.TRANSPARENT;
    private int shadowColor = Color.parseColor("#4Dfe3249");
    private int iconReference = 0;
    private int strokeSize = 0;
    private int strokeColor = Color.BLACK;
    private int iconColor = Color.BLACK;
    private int iconSize = Utils.dipToPix(36);
    private final Paint clipPaint = new Paint();
    private final Path baseClipPath = new Path();
    private final Path animationClipPath = new Path();
    private final PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private ValueAnimator openAnimator, closeAnimator;
    private From animateFrom = From.CENTER;
    private From openFrom = From.CENTER;
    private From closeFrom = From.CENTER;
    private int animatedValue = 100;
    private ValueAnimator changeColorAnimator;
    private boolean allowFastClick = false;
    private long lastTimeClicked;
    private boolean showDot = false;
    private LoadingView loadingView;
    private boolean isLoading = false;
    float currentTouchX, currentTouchY;
    private boolean isLongCLickListenerSet = false;
    private boolean longClicked = false;
    private final CircOutInterpolator circOutInterpolator = new CircOutInterpolator();
    private SvgUtils svgUtils;
    private List<SvgUtils.SvgPath> paths;
    private final Paint iconPaint = new Paint();
    private boolean showIcon = true;

    public MiniButton(Context context) {
        super(context);
        init();
    }

    public MiniButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public MiniButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MiniButton);
        if (text == null)
            text = typedArray.getString(R.styleable.MiniButton_mb_SetText);
        textSize = typedArray.getDimensionPixelSize(R.styleable.MiniButton_mb_TextSize, Utils.spToPix(14));
        textColor = typedArray.getColor(R.styleable.MiniButton_mb_TextColor, Color.WHITE);

        String typefacePath = typedArray.getString(R.styleable.MiniButton_mb_Typeface);
        if (typefacePath != null)
            typeface = Typeface.createFromAsset(getContext().getAssets(), typefacePath);

        rippleColor = typedArray.getColor(R.styleable.MiniButton_mb_RippleColor, rippleColor);

        if (backgroundColor == Color.TRANSPARENT)
            backgroundColor = typedArray.getColor(R.styleable.MiniButton_mb_BackgroundColor, Color.TRANSPARENT);

        shadowColor = typedArray.getColor(R.styleable.MiniButton_mb_ShadowColor, shadowColor);
        cornerRadius = typedArray.getDimensionPixelSize(R.styleable.MiniButton_mb_CornerRadius, 0);
        iconReference = typedArray.getResourceId(R.styleable.MiniButton_mb_Icon, 0);
        shadowSize = typedArray.getDimensionPixelSize(R.styleable.MiniButton_mb_ShadowSize, 0);
        strokeColor = typedArray.getColor(R.styleable.MiniButton_mb_StrokeColor, strokeColor);
        strokeSize = typedArray.getDimensionPixelSize(R.styleable.MiniButton_mb_StrokeWidth, strokeSize);
        iconSize = typedArray.getDimensionPixelSize(R.styleable.MiniButton_mb_IconSize, iconSize);
        iconColor = typedArray.getColor(R.styleable.MiniButton_mb_IconColor, Color.WHITE);
        shadowDy = typedArray.getDimensionPixelSize(R.styleable.MiniButton_mb_ShadowDy, Utils.dipToPix(4));
        allowFastClick = typedArray.getBoolean(R.styleable.MiniButton_mb_AllowFastClick, true);

        if (typedArray.getBoolean(R.styleable.MiniButton_mb_Open, true))
            animatedValue = 100;
        else
            animatedValue = 0;

        typedArray.recycle();
    }

    private void init() {
        setWillNotDraw(false);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        clipPaint.setAntiAlias(true);

        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(backgroundColor);
        if (shadowSize > 0)
            shadowPaint.setShadowLayer(shadowSize, 0, shadowDy, shadowColor);
        else
            shadowDy = 0;

        strokePaint.setAntiAlias(true);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeSize);

        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);

        iconPaint.setAntiAlias(true);
        iconPaint.setStyle(Paint.Style.FILL);
        iconPaint.setColor(iconColor);

        initIcon();
    }

    private void initIcon() {
        if (iconReference != 0) {
            svgUtils = new SvgUtils(iconPaint);
            svgUtils.load(getContext(), iconReference);
            paths = svgUtils.getPathsForViewport(iconSize, iconSize);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        float with = getWidth();
        float height = getHeight();

        if (shadowSize != 0)
            baseRectF.set(shadowSize + strokeSize, (shadowSize - shadowDy) + strokeSize, with - (shadowSize + strokeSize), height - (shadowSize + shadowDy + strokeSize));
        else
            baseRectF.set(strokeSize, strokeSize, with - strokeSize, height - (strokeSize));

        float left = 0;
        float right = with;
        float step = with / 100f;
        switch (animateFrom) {
            case LEFT:
                right = step * animatedValue;
                break;
            case RIGHT:
                left = with - (step * animatedValue);
                break;
            case CENTER:
                left = (with / 2) - (step * (animatedValue / 2.0f));
                right = (with / 2) + (step * (animatedValue / 2.0f));
                break;
        }

        animationRectF.set(left, 0, right, height);

        path.reset();
        path.addRoundRect(baseRectF, cornerRadius >= 0 ? cornerRadius : baseRectF.bottom / 2, cornerRadius >= 0 ? cornerRadius : baseRectF.bottom / 2, Path.Direction.CW);
        canvas.drawPath(path, shadowPaint);

        textRect.set((int) baseRectF.left, (int) baseRectF.top, (int) baseRectF.right, (int) baseRectF.bottom);

        if (text != null)
            Utils.drawCenterText(text, textRect, baseRectF.left, baseRectF.top, canvas, textPaint);

        if (iconReference != 0 && showIcon) {
            canvas.save();
            canvas.translate(baseRectF.left + (baseRectF.width() / 2f - iconSize / 2f), baseRectF.top + (baseRectF.height() / 2f - iconSize / 2f));
            for (SvgUtils.SvgPath sPath : paths) {
                canvas.drawPath(sPath.getPath(), iconPaint);
            }
            canvas.restore();
        }

        if (strokeSize > 0)
            canvas.drawPath(path, strokePaint);

        if (showDot)
            canvas.drawCircle(right - Utils.dipToPix(6), Utils.dipToPix(6), Utils.dipToPix(2.5f), textPaint);

        int saveCount = 0;
        if (!isInEditMode())
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        super.draw(canvas);

        if (!isInEditMode()) {

            baseClipPath.reset();
            clipPaint.setXfermode(porterDuffXfermode);
            baseClipPath.addRoundRect(baseRectF, cornerRadius >= 0 ? cornerRadius : height / 2, cornerRadius >= 0 ? cornerRadius : height / 2, Path.Direction.CW);
            baseClipPath.setFillType(Path.FillType.INVERSE_WINDING);

            canvas.drawPath(baseClipPath, clipPaint);

            canvas.restoreToCount(saveCount);

            // clip animation
            animationClipPath.reset();
            animationClipPath.addRoundRect(animationRectF, cornerRadius >= 0 ? cornerRadius : height / 2, cornerRadius >= 0 ? cornerRadius : height / 2, Path.Direction.CW);
            animationClipPath.setFillType(Path.FillType.INVERSE_WINDING);

            canvas.drawPath(animationClipPath, clipPaint);

            clipPaint.setXfermode(null);
        }
    }

    @Override
    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    @Override
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        textPaint.setTypeface(typeface);
        invalidate();
    }

    @Override
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        invalidate();
    }

    @Override
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }

    @Override
    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
    }

    @Override
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        strokePaint.setColor(strokeColor);
        invalidate();
    }

    private final Handler longClickHandler = new Handler();
    private final Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            firstTouch = false;
            longClicked = true;
            performLongClick();
            doRipple(currentTouchX, currentTouchY);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return super.dispatchTouchEvent(event);

        currentTouchX = event.getX();
        currentTouchY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN & !firstTouch) {
            if (alphaRecView == null) {
                alphaRecView = new AlphaRecView(getContext());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) baseRectF.width(), (int) baseRectF.height());
                layoutParams.topMargin = (int) baseRectF.top;
                layoutParams.leftMargin = (int) baseRectF.left;
                alphaRecView.setLayoutParams(layoutParams);

                alphaRecView.setColor(rippleColor);
                alphaRecView.setOnFinishAnimatingListener(new AlphaRecView.OnFinishAnimatingListener() {

                    @Override
                    public void onFinish() {
                        removeView(alphaRecView);
                        alphaRecView = null;
                    }
                });
                addView(alphaRecView);
            }

            alphaRecView.startAlphaAnimation();
            firstX = (int) event.getX();
            firstY = (int) event.getY();
            firstTouch = true;

            if (isLongCLickListenerSet) {
                longClickHandler.removeCallbacks(longClickRunnable);
                longClickHandler.postDelayed(longClickRunnable, 500);
            }

            super.dispatchTouchEvent(event);
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP && firstTouch) {
            doRipple(currentTouchX, currentTouchY);

            firstTouch = false;
            longClicked = false;
            longClickHandler.removeCallbacks(longClickRunnable);
        }

        if ((event.getAction() == MotionEvent.ACTION_MOVE
                | event.getAction() == MotionEvent.ACTION_SCROLL
                | event.getAction() == MotionEvent.ACTION_CANCEL)
                & firstTouch & ((Math.abs(event.getX() - firstX) > 10) | (Math.abs(event.getY() - firstY) > 10))) {
            doRipple(event.getX(), event.getY());
            firstTouch = false;
            longClickHandler.removeCallbacks(longClickRunnable);
        }

        return super.dispatchTouchEvent(event);
    }

    private void doRipple(float x, float y) {
        if (alphaRecView != null) {
            alphaRecView.endAlphaAnimation();
        }
        final Ripple ripple = new Ripple(getContext());
        ripple.setCircleEndRadius(Math.max(getWidth(), getHeight()));
        ripple.setRippleColor(rippleColor);
        ripple.setPivotX(x);
        ripple.setPivotY(y);
        ripple.setX(x - ripple.getCircleEndRadius());
        ripple.setY(y - ripple.getCircleEndRadius());
        ripple.setOnFinishRippleingListner(new Ripple.OnFinishRipplingListener() {

            @Override
            public void onFinish() {
                removeView(ripple);
            }
        });
        addView(ripple, 0);
        ripple.doRipple();
    }

    @Override
    public void setOnLongClickListener(View.OnLongClickListener l) {
        isLongCLickListenerSet = true;
        super.setOnLongClickListener(l);
    }

    @Override
    public void changeColor(int fromColor, int toColor) {
        changeColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        changeColorAnimator.setDuration(250);
        changeColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                shadowPaint.setColor((int) changeColorAnimator.getAnimatedValue());
                invalidate();
            }
        });
        changeColorAnimator.start();
        backgroundColor = toColor;
    }

    @Override
    public void changeColor(int color) {
        if (changeColorAnimator != null && changeColorAnimator.isRunning())
            changeColorAnimator.cancel();
        changeColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), backgroundColor, color);
        changeColorAnimator.setDuration(250);
        changeColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                shadowPaint.setColor((int) changeColorAnimator.getAnimatedValue());
                invalidate();
            }
        });
        changeColorAnimator.start();
        backgroundColor = color;
    }

    @Override
    public void setColor(int color) {
        if (changeColorAnimator != null)
            changeColorAnimator.cancel();
        this.backgroundColor = color;
        shadowPaint.setColor(color);
        invalidate();
    }

    @Override
    public int getColor() {
        return backgroundColor;
    }

    @Override
    public void setIcon(int icon) {
        this.iconReference = icon;
        initIcon();
    }

    @Override
    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        initIcon();
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        shadowPaint.setShadowLayer(shadowSize, 0, shadowDy, shadowColor);
        invalidate();
    }

    @Override
    public int getShadowColor() {
        return shadowColor;
    }

    @Override
    public void setOpen(boolean open) {
        if (open)
            animatedValue = 100;
        else
            animatedValue = 0;
        invalidate();
    }

    @Override
    public boolean isOpen() {
        return animatedValue == 100;
    }

    @Override
    public void setProgress(float progress) {
        loadingView.setProgress(progress);
    }

    @Override
    public float getProgress() {
        return loadingView.getProgress();
    }

    @Override
    public void setRotationAnimation(boolean rotationAnimation) {
        loadingView.setRotationAnimation(rotationAnimation);
    }

    @Override
    public void setLoading(boolean loading) {
        if (isLoading && loading) return;
        super.setEnabled(!loading); // i don't want to alpha get changed
        isLoading = loading;
        if (loading) {
            if (loadingView == null) {
                loadingView = new LoadingView(getContext());
                loadingView.setBackgroundStrokeColor(Utils.adjustColorAlpha(textColor, 0.15f));
                loadingView.setProgressColor(textColor);
                loadingView.setBackgroundStrokeWidth(Utils.dipToPix(4));
                loadingView.setProgressStrokeWidth(Utils.dipToPix(4));
                LayoutParams layoutParams = new LayoutParams(Utils.dipToPix(30), Utils.dipToPix(30));
                layoutParams.topMargin = (int) (baseRectF.top + ((baseRectF.height() / 2.0f) - (layoutParams.height / 2.0f)));
                layoutParams.leftMargin = (int) (baseRectF.left + ((baseRectF.width() / 2.0f) - (layoutParams.width / 2.0f)));
                loadingView.setLayoutParams(layoutParams);
                addView(loadingView);
            }
            if (text != null)
                textHolder = text;
            setText(null);
            if (iconReference != 0)
                showIcon = false;
            loadingView.setVisibility(VISIBLE);
            loadingView.setLoading(true);
        } else {
            if (loadingView == null) return;
            loadingView.setVisibility(GONE);
            if (iconReference != 0)
                showIcon = true;
            if (textHolder != null)
                setText(textHolder);
            textHolder = null;
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setUserRotationAnimationWithProgress(boolean userRotationAnimationWithProgress) {
        loadingView.setUserRotationAnimationWithProgress(userRotationAnimationWithProgress);
    }

    @Override
    public void setAnimatedProgress(boolean animateProgress) {
        loadingView.setAnimatedProgress(animateProgress);
    }

    @Override
    public void setProgressColor(int progressColor) {
        loadingView.setProgressColor(progressColor);
    }

    @Override
    public int getProgressColor() {
        return loadingView.getProgressColor();
    }

    @Override
    public void setBackgroundStrokeColor(int backgroundStrokeColor) {
        loadingView.setBackgroundStrokeColor(backgroundStrokeColor);
    }

    @Override
    public int getBackgroundStrokeColor() {
        return loadingView.getBackgroundStrokeColor();
    }

    @Override
    public void setBackgroundStrokeWidth(float backgroundStrokeWidth) {
        loadingView.setBackgroundStrokeWidth(backgroundStrokeWidth);
    }

    @Override
    public float getBackgroundStrokeWidth() {
        return loadingView.getBackgroundStrokeWidth();
    }

    @Override
    public void setProgressStrokeWidth(float progressStrokeWidth) {
        loadingView.setProgressStrokeWidth(progressStrokeWidth);
    }

    @Override
    public float getProgressStrokeWidth() {
        return loadingView.getProgressStrokeWidth();
    }

    @Override
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    @Override
    public void setAnimatedValue(int animatedValue) {
        this.animatedValue = animatedValue;
        invalidate();
    }

    @Override
    public void open(long delay) {
        open(openFrom, delay);
    }

    @Override
    public void open(From from, long delay) {
        if (openAnimator != null && openAnimator.isRunning())
            return;
        if (closeAnimator != null && closeAnimator.isRunning())
            closeAnimator.cancel();
        animateFrom = from;
        if (openAnimator == null) {
            openAnimator = ValueAnimator.ofInt(animatedValue, 100);
            openAnimator.setDuration(250);
            openAnimator.setInterpolator(circOutInterpolator);
            openAnimator.setStartDelay(delay);
            openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatedValue = (int) animation.getAnimatedValue();
                    float alpha = ((1f / 100f) * (float) animatedValue);
                    setAlpha(alpha);
                    invalidate();
                }
            });
        }
        openAnimator.start();
    }

    @Override
    public void close(long delay) {
        close(closeFrom, delay);
    }

    @Override
    public void close(From from, long delay) {
        if (closeAnimator != null && closeAnimator.isRunning())
            return;
        if (openAnimator != null && openAnimator.isRunning())
            openAnimator.cancel();
        animateFrom = from;
        if (closeAnimator == null) {
            closeAnimator = ValueAnimator.ofInt(animatedValue, 0);
            closeAnimator.setDuration(250);
            closeAnimator.setStartDelay(delay);
            closeAnimator.setInterpolator(circOutInterpolator);
            closeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatedValue = (int) animation.getAnimatedValue();
                    float alpha = ((1f / 100f) * (float) animatedValue);
                    setAlpha(alpha);
                    invalidate();
                }
            });
        }
        closeAnimator.start();
    }

    @Override
    public boolean performClick() {
        if (canPerformClick())
            return super.performClick();
        else
            return false;
    }

    private boolean canPerformClick() {
        if (longClicked) return false;
        if (allowFastClick) return true;
        else if (System.currentTimeMillis() >= lastTimeClicked + 1000) {
            lastTimeClicked = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public int getShadowSize() {
        return shadowSize;
    }

    @Override
    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
        shadowPaint.setShadowLayer(shadowSize, 0, shadowDy, shadowColor);
        invalidate();
    }

    @Override
    public void setShadowDy(int shadowDy) {
        this.shadowDy = shadowDy;
        shadowPaint.setShadowLayer(shadowSize, 0, shadowDy, shadowColor);
        invalidate();
    }

    @Override
    public int getShadowDy() {
        return shadowDy;
    }

    @Override
    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
        iconPaint.setColor(iconColor);
    }

    @Override
    public int getIconColor() {
        return iconColor;
    }

    @Override
    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
        strokePaint.setStrokeWidth(strokeSize);
        invalidate();
    }

    @Override
    public void setOpenFrom(From openFrom) {
        this.openFrom = openFrom;
    }

    @Override
    public void setCloseFrom(From closeFrom) {
        this.closeFrom = closeFrom;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? 1f : 0.4f);
    }

    @Override
    public void setAllowFastClick(boolean allowFastClick) {
        this.allowFastClick = allowFastClick;
    }

    @Override
    public void showDot(boolean showDot) {
        this.showDot = showDot;
        invalidate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superstate", super.onSaveInstanceState());
        bundle.putInt("backgroundColor", backgroundColor);
        bundle.putInt("shadowColor", shadowColor);
        bundle.putInt("iconReference", iconReference);
        bundle.putInt("strokeWidth", strokeSize);
        bundle.putInt("strokeColor", strokeColor);
        bundle.putInt("iconColor", iconColor);
        bundle.putInt("iconSize", iconSize);
        bundle.putInt("rippleColor", rippleColor);
        bundle.putInt("shadowSize", shadowSize);
        bundle.putInt("shadowY", shadowDy);
        bundle.putInt("textSize", textSize);
        bundle.putInt("textColor", textColor);
        bundle.putInt("cornerRadius", cornerRadius);
        bundle.putInt("animatedValue", animatedValue);
        bundle.putString("text", text);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("superState");
            backgroundColor = bundle.getInt("backgroundColor");
            shadowColor = bundle.getInt("shadowColor");
            iconReference = bundle.getInt("iconReference");
            strokeSize = bundle.getInt("strokeWidth");
            strokeColor = bundle.getInt("strokeColor");
            iconColor = bundle.getInt("iconColor");
            iconSize = bundle.getInt("iconSize");
            rippleColor = bundle.getInt("rippleColor");
            shadowSize = bundle.getInt("shadowSize");
            shadowDy = bundle.getInt("shadowY");
            textSize = bundle.getInt("textSize");
            textColor = bundle.getInt("textColor");
            cornerRadius = bundle.getInt("cornerRadius");
            animatedValue = bundle.getInt("animatedValue");
            text = bundle.getString("text");
        }
        super.onRestoreInstanceState(state);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (paths != null) {
            paths.clear();
            paths = null;
            svgUtils = null;
        }
    }
}
