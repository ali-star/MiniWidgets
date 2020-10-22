package alistar.miniwidgets.sample.demo

import alistar.miniwidgets.sample.R
import alistar.miniwidgets.sample.databinding.MiniButtonDemoFragmentBinding
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
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}