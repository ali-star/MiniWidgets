package alistar.miniwidgets.button;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

public class AlphaRecView extends View {

    private float alpha;
    private OnFinishAnimatingListener onFinishAnimatingListener;
    private int color = Color.parseColor("#10000000");

    public AlphaRecView(Context context) {
        super(context);
        init();
    }

    public AlphaRecView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(color);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void startAlphaAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", alpha, 1);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                alpha = arg0.getAnimatedFraction();
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    public void endAlphaAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", alpha, 0);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                alpha = arg0.getAnimatedFraction();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onFinishAnimatingListener != null)
                    onFinishAnimatingListener.onFinish();
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    public void setOnFinishAnimatingListener(OnFinishAnimatingListener onFinishAnimatingListener) {
        this.onFinishAnimatingListener = onFinishAnimatingListener;
    }

    public interface OnFinishAnimatingListener {
        public void onFinish();
    }

    public void setColor(int color) {
        this.color = color;
        setBackgroundColor(color);
    }

}
