package alistar.miniwidgets.sample

import alistar.miniwidgets.sample.databinding.ActivityMainBinding
import alistar.miniwidgets.utils.Utils
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding!!.apply {

            githubButton.setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://github.com/ali-star/MiniWidgets")
                    }
                )
            }

            infoTextView.post {
                infoTextView.layoutParams.width = rootLayout.width - Utils.dipToPix(72)
                infoTextView.requestLayout()
            }

            svgImageViewDemoButton.setOnClickListener {
                val point = it.getPointOnScreen()
                supportFragmentManager.beginTransaction().add(
                    R.id.fragmentContainer,
                    SVGImageViewDemoFragment.newInstance(point.x, point.y)
                ).commit()
            }

            val handler = Handler()
            val runnable = object : Runnable {
                override fun run() {
                    if (binding == null)
                        return
                    viewFlipper.startFlipping()
                    handler.postDelayed(this, 3000)
                }
            }
            handler.post(runnable)
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(currentFragment)
                .commitAllowingStateLoss()
            return
        } else
            super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}