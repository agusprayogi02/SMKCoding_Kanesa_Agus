package id.canteen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import id.canteen.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private var handler = Handler()
    private var progressStatus = 0
    private var isStarted = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler(Handler.Callback {
            if (isStarted) {
                progressStatus++
            }
            progressBarHorizontal.progress = progressStatus
            textViewHorizontalProgress.text = "${progressStatus}/${progressBarHorizontal.max}"
            handler.sendEmptyMessageDelayed(0, 30)
            if (progressStatus == 100) {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }

            true
        })

        handler.sendEmptyMessage(0)
        isStarted = true
    }
}
