package id.canteen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.canteen.session.Session
import kotlinx.android.synthetic.main.activity_daf_warung.*

class DafWarung : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daf_warung)
        val ref = Session["Nama"]
        bro.text = ref

    }
}
