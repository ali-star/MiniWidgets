package alistar.miniwidgets.sample

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton.setOnClickListener {

        }

        /*aboutBase.setOnClickListener { view ->
            val widthAnimator = ValueAnimator.ofInt(view.width, rootLayout.width)
            widthAnimator.addUpdateListener {
                aboutBase.layoutParams.width = it.animatedValue as Int
                aboutBase.requestLayout()
            }

            val heightAnimator = ValueAnimator.ofInt(view.height, rootLayout.height)
            heightAnimator.addUpdateListener {
                aboutBase.layoutParams.height = it.animatedValue as Int
                aboutBase.requestLayout()
            }

            val marginAnimator = ValueAnimator.ofInt(0, (view.layoutParams as ConstraintLayout.LayoutParams).bottomMargin)
            heightAnimator.addUpdateListener {
                (aboutBase.layoutParams as ConstraintLayout.LayoutParams).bottomMargin = -(it.animatedValue as Int)
                aboutBase.requestLayout()
            }

            widthAnimator.start()
            heightAnimator.start()
            marginAnimator.start()
        }*/

        githubButton.setOnClickListener {
            val whiteColor = ContextCompat.getColor(applicationContext, R.color.white)
            val lightGreenColor = ContextCompat.getColor(applicationContext, R.color.lightGreen)
            val darkColor = ContextCompat.getColor(applicationContext, R.color.dark)
            val black40Color = ContextCompat.getColor(applicationContext, R.color.black_40)
            val lightGreen40Color = ContextCompat.getColor(applicationContext, R.color.lightGreen_40)

            githubButton.changeColor(
                if (githubButton.color == whiteColor)
                    lightGreenColor
                else
                    whiteColor
            )

            githubButton.shadowColor = if (githubButton.color == whiteColor)
                black40Color
            else
                lightGreen40Color

            githubButton.iconColor = if (githubButton.iconColor == whiteColor)
                darkColor
            else
                whiteColor
        }
    }
}