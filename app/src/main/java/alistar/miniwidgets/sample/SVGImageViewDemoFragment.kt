package alistar.miniwidgets.sample

import alistar.miniwidgets.sample.databinding.SvgImageViewDemoFragmentBinding
import android.animation.ValueAnimator
import android.os.Bundle
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
                circle.x = circleX.toFloat()
                circle.y = circleY.toFloat()

                val valueAnimator = ValueAnimator.ofInt(0, 100)
                valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Int
                    val radius = 1000
                    circle.layoutParams.width = 1000
                    circle.layoutParams.height = 1000
                    // circle.alpha = (animatedValue / 100).toFloat()
                    circle.requestLayout()
                }

                valueAnimator.duration = 4000
                valueAnimator.start()
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