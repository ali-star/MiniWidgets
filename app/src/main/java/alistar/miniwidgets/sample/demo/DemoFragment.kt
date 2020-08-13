package alistar.miniwidgets.sample.demo

import alistar.miniwidgets.utils.CircOutInterpolator
import alistar.miniwidgets.utils.Utils
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.fragment.app.Fragment

open class DemoFragment : Fragment() {

    var circleX: Int = 0
    var circleY: Int = 0

    fun startOpenAnimation(circle: View, contentsLayout: View) {
        circle.post {
            circle.x = circleX.toFloat() - (circle.width / 2)
            circle.y = circleY.toFloat() - (circle.height / 2)
            val alphaAnimator = ObjectAnimator.ofFloat(circle, "alpha", 0f, 1f).setDuration(200)
            val scaleXAnimator = ObjectAnimator.ofFloat(circle, "scaleX", 0f, 30f).setDuration(700)
            val scaleYAnimator = ObjectAnimator.ofFloat(circle, "scaleY", 0f, 30f).setDuration(700)
            val animatorSet = AnimatorSet()
            animatorSet.interpolator = CircOutInterpolator()
            animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator)
            animatorSet.start()
        }

        contentsLayout.isClickable = true
        contentsLayout.post {
            val alphaAnimator = ObjectAnimator.ofFloat(contentsLayout, "alpha", 0f, 1f)

            val currentY = contentsLayout.y
            val xAnimator = ObjectAnimator.ofFloat(contentsLayout, "y", currentY + Utils.dipToPix(24), currentY)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(alphaAnimator, xAnimator)
            animatorSet.duration = 500
            animatorSet.startDelay = 300
            animatorSet.interpolator = CircOutInterpolator()
            animatorSet.start()
        }
    }

}