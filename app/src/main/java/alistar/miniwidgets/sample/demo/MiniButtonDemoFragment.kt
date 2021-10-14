package alistar.miniwidgets.sample.demo

import alistar.miniwidgets.sample.R
import alistar.miniwidgets.sample.databinding.MiniButtonDemoFragmentBinding
import alistar.miniwidgets.utils.Utils
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

class MiniButtonDemoFragment : DemoFragment() {

    private var binding: MiniButtonDemoFragmentBinding? = null

    companion object {
        fun newInstance(circleX: Int, circleY: Int): MiniButtonDemoFragment {
            val fragment = MiniButtonDemoFragment()
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
        val view = inflater.inflate(R.layout.mini_button_demo_fragment, container, false)
        binding = MiniButtonDemoFragmentBinding.bind(view).apply {
            startOpenAnimation(circle, contentsLayout)

            greenColorButton.setOnClickListener {
                miniButton.changeColor(Color.parseColor("#4AD4C9"))
            }

            orangeColorButton.setOnClickListener {
                miniButton.changeColor(Color.parseColor("#FF8772"))
            }

            purpleColorButton.setOnClickListener {
                miniButton.changeColor(Color.parseColor("#B451FC"))
            }

            blueColorButton.setOnClickListener {
                miniButton.changeColor(Color.parseColor("#6CEAFB"))
            }

            darkColorButton.setOnClickListener {
                miniButton.changeColor(Color.parseColor("#455077"))
            }

            shGreenColorButton.setOnClickListener {
                miniButton.shadowColor = Color.parseColor("#664AD4C9")
            }

            shOrangeColorButton.setOnClickListener {
                miniButton.shadowColor = Color.parseColor("#66FF8772")
            }

            shPurpleColorButton.setOnClickListener {
                miniButton.shadowColor = Color.parseColor("#66B451FC")
            }

            shBlueColorButton.setOnClickListener {
                miniButton.shadowColor = Color.parseColor("#666CEAFB")
            }

            shDarkColorButton.setOnClickListener {
                miniButton.shadowColor = Color.parseColor("#66455077")
            }

            shadowRadiusSeekBar.apply {
                max = 24
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        miniButton.shadowSize = Utils.dipToPix(progress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }

            shadowDyRadiusSeekBar.apply {
                max = 16
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        miniButton.shadowDy = Utils.dipToPix(progress)
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