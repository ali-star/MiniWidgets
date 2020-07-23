package alistar.miniwidgets.button;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

import alistar.miniwidgets.utils.CircOutInterpolator;
import alistar.miniwidgets.utils.Utils;

public class Ripple extends View {

    private Paint paint = new Paint();
    private float circleRadius = 0;
    public static final float CIRCLE_END_RADIUS = Utils.dipToPix(150);
    private OnFinishRipplingListener onFinishRipplingListener;
    private int color = Color.parseColor("#10000000");
    private CircOutInterpolator circOutInterpolator = new CircOutInterpolator();

    public Ripple(Context context) {
        super(context);
        init();
    }

    public Ripple(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(color);
        setLayoutParams(new LayoutParams((int) (CIRCLE_END_RADIUS * 2), (int) (CIRCLE_END_RADIUS * 2)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleRadius, paint);
    }

    public void doRipple() {
        ObjectAnimator rippleAnimator = ObjectAnimator.ofFloat(this, radiusProperty, circleRadius, CIRCLE_END_RADIUS);
        rippleAnimator.setDuration(400);
        rippleAnimator.setInterpolator(circOutInterpolator);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        alphaAnimator.setDuration(400);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onFinishRipplingListener != null)
                    onFinishRipplingListener.onFinish();
            }
        });
        animatorSet.playTogether(rippleAnimator, alphaAnimator);
        animatorSet.start();
    }

    public float getRadius() {
        return circleRadius;
    }

    public void setRadius(float radius) {
        circleRadius = radius;
        invalidate();
    }

    private Property<Ripple, Float> radiusProperty = new Property<Ripple, Float>(Float.class, "radius") {

        @Override
        public Float get(Ripple ripple) {
            return ripple.getRadius();
        }

        @Override
        public void set(Ripple ripple, Float value) {
            ripple.setRadius(value);
        }

    };

    public void setOnFinishRippleingListner(OnFinishRipplingListener onFinishRipplingListener) {
        this.onFinishRipplingListener = onFinishRipplingListener;
    }

    public interface OnFinishRipplingListener {
        void onFinish();
    }

    public void setRippleColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

}
