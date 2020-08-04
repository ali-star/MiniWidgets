package alistar.miniwidgets.sample

import alistar.miniwidgets.sample.databinding.SvgImageViewDemoFragmentBinding
import alistar.miniwidgets.utils.CircOutInterpolator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlin.math.pow
import kotlin.math.sqrt

class SVGImageViewDemoFragment : Fragment() {

    var circleX: Int = 0
    var circleY: Int = 0

    private var binding: SvgImageViewDemoFragmentBinding? = null

    companion object {
        fun newInstance(circleX: Int, circleY: Int): SVGImageViewDemoFragment {
            val fragment = SVGImageViewDemoFragment()
            fragment.circleX = circleX
            fragment.circleY = circleY
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.svg_image_view_demo_fragment, container, false)
        binding = SvgImageViewDemoFragmentBinding.bind(view).apply {
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
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}