package id.canteen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.canteen.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private var handler = Handler()
    private var progressStatus = 0
    private var isStarted = false
    private var auth = FirebaseAuth.getInstance()

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
            if (progressStatus == 99) {
                if (auth.currentUser != null) {
                    val level = auth.currentUser!!.displayName.toString()
//                    Toast.makeText(this, level, Toast.LENGTH_LONG).show()
                    when {
                        level.equals("User", true) -> {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        level.equals("Member", true) -> {
                            startActivity(Intent(this, MenuActivity::class.java))
                        }
                        else -> {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    }
                    finish()
                }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                }
            }

            true
        })

        handler.sendEmptyMessage(0)
        isStarted = true
    }
}
