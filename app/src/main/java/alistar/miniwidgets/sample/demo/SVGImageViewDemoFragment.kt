package alistar.miniwidgets.sample.demo

import alistar.miniwidgets.sample.R
import alistar.miniwidgets.sample.databinding.SvgImageViewDemoFragmentBinding
import alistar.miniwidgets.utils.CircOutInterpolator
import alistar.miniwidgets.utils.Utils
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SVGImageViewDemoFragment : Fragment() {

    var circleX: Int = 0
    var circleY: Int = 0

    private var binding: SvgImageViewDemoFragmentBinding? = null

    companion object {
        fun newInstance(circleX: Int, circleY: Int): SVGImageViewDemoFragment {
            val fragment =
                SVGImageViewDemoFragment()
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

            greenGradientButton.setOnClickListener {
                SVGImageView.gradientStartColor = Color.parseColor("#4AD4C9")
                SVGImageView.gradientEndColor = Color.parseColor("#239ECA")
            }

            orangeGradientButton.setOnClickListener {
                SVGImageView.gradientStartColor = Color.parseColor("#FF8772")
                SVGImageView.gradientEndColor = Color.parseColor("#FF6F9E")
            }

            purpleGradientButton.setOnClickListener {
                SVGImageView.gradientStartColor = Color.parseColor("#B451FC")
                SVGImageView.gradientEndColor = Color.parseColor("#7266FC")
            }

            blueGradientButton.setOnClickListener {
                SVGImageView.gradientStartColor = Color.parseColor("#6CEAFB")
                SVGImageView.gradientEndColor = Color.parseColor("#0A52F3")
            }

            darkGradientButton.setOnClickListener {
                SVGImageView.gradientStartColor = Color.parseColor("#455077")
                SVGImageView.gradientEndColor = Color.parseColor("#212941")
            }

            greenColorButton.setOnClickListener {
                SVGImageView.color = Color.parseColor("#4AD4C9")
            }

            orangeColorButton.setOnClickListener {
                SVGImageView.color = Color.parseColor("#FF8772")
            }

            purpleColorButton.setOnClickListener {
                SVGImageView.color = Color.parseColor("#B451FC")
            }

            blueColorButton.setOnClickListener {
                SVGImageView.color = Color.parseColor("#6CEAFB")
            }

            darkColorButton.setOnClickListener {
                SVGImageView.color = Color.parseColor("#455077")
            }

            contentsLayout.alpha = 0f
            contentsLayout.post {
                val alphaAnimator = ObjectAnimator.ofFloat(contentsLayout, "alpha", 0f, 1f).apply {
                    duration = 500
                    startDelay = 300
                }

                val currentY = contentsLayout.y
                val xAnimator = ObjectAnimator.ofFloat(contentsLayout, "y", currentY + Utils.dipToPix(24), currentY).apply {
                    duration = 500
                    startDelay = 300
                }

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(alphaAnimator, xAnimator)
                animatorSet.interpolator = CircOutInterpolator()
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