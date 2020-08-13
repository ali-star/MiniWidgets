package alistar.miniwidgets.sample.demo

import alistar.miniwidgets.sample.R
import alistar.miniwidgets.sample.databinding.LoadingViewDemoFragmentBinding
import alistar.miniwidgets.utils.Utils
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

class LoadingViewDemoFragment : DemoFragment() {

    private var binding: LoadingViewDemoFragmentBinding? = null

    companion object {
        fun newInstance(circleX: Int, circleY: Int): LoadingViewDemoFragment {
            val fragment = LoadingViewDemoFragment()
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
        val view = inflater.inflate(R.layout.loading_view_demo_fragment, container, false)
        binding = LoadingViewDemoFragmentBinding.bind(view).apply {
            startOpenAnimation(circle, contentsLayout)

            setProgressColorOnClick(prGreenColorButton, Color.parseColor("#4AD4C9"))
            setProgressColorOnClick(prOrangeColorButton, Color.parseColor("#FF8772"))
            setProgressColorOnClick(prPurpleColorButton, Color.parseColor("#B451FC"))
            setProgressColorOnClick(prBlueColorButton, Color.parseColor("#3D72DE"))
            setProgressColorOnClick(prDarkColorButton, Color.parseColor("#455077"))

            setBackgroundColorOnClick(bgGreenColorButton, Color.parseColor("#4D4AD4C9"))
            setBackgroundColorOnClick(bgOrangeColorButton, Color.parseColor("#4DFF8772"))
            setBackgroundColorOnClick(bgPurpleColorButton, Color.parseColor("#4DB451FC"))
            setBackgroundColorOnClick(bgBlueColorButton, Color.parseColor("#1AB9C0CB"))
            setBackgroundColorOnClick(bgDarkColorButton, Color.parseColor("#4D455077"))

            progressWidthSeekBar.apply {
                max = 24
                progress = Utils.pixToDip(loadingView.progressStrokeWidth).toInt()
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        val value = 1 + progress
                        loadingView.progressStrokeWidth = Utils.dipToPix(value.toFloat())
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }

            backgroundWidthSeekBar.apply {
                max = 24
                progress = Utils.pixToDip(loadingView.backgroundStrokeWidth).toInt()
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        loadingView.backgroundStrokeWidth = Utils.dipToPix(progress.toFloat())
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }

            progressSeekBar.apply {
                max = 100
                progress = 25
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        loadingView.progress = progress.toFloat()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }

            changeModeButton.setOnClickListener {
                loadingView.setLoading(!loadingView.isRotating)
                changeModeButton.text = if (loadingView.isRotating) "LOADING" else "PROGRESS"
            }
        }
        return view
    }

    private fun setProgressColorOnClick(view: View, color: Int) {
        view.setOnClickListener {
            binding?.loadingView?.progressColor = color
        }
    }

    private fun setBackgroundColorOnClick(view: View, color: Int) {
        view.setOnClickListener {
            binding?.loadingView?.backgroundStrokeColor = color
        }
    }

}