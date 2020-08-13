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
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kotlin.math.abs

class SVGImageViewDemoFragment : DemoFragment() {

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
                startOpenAnimation(circle, contentsLayout)

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

            shadowRadiusSeekBar.apply {
                max = 24
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        SVGImageView.shadowRadius = Utils.dipToPix(progress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }


            val shadowDxMin = -24
            val shadowDxMax = 24
            shadowDxSeekBar.apply {
                max = abs(shadowDxMin) + abs(shadowDxMax)
                progress = max / 2
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        val value = shadowDxMin + progress
                        SVGImageView.shadowDx = Utils.dipToPix(value)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }

            val shadowDyMin = -24
            val shadowDyMax = 24
            shadowDySeekBar.apply {
                max = abs(shadowDyMin) + abs(shadowDyMax)
                progress = max / 2
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        val value = shadowDyMin + progress
                        SVGImageView.shadowDy = Utils.dipToPix(value)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}