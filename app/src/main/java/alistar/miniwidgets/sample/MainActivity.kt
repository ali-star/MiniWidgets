package alistar.miniwidgets.sample

import alistar.miniwidgets.sample.databinding.ActivityMainBinding
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

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

            testButton.setOnClickListener {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}