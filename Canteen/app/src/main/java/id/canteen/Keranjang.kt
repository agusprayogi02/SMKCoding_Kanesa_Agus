package id.canteen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.database.FirebaseDatabase
import id.canteen.data.Menus

class Keranjang : AppCompatActivity() {

    lateinit var list: MutableList<Menus>
    lateinit var idList: ListView
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)

    }
}
