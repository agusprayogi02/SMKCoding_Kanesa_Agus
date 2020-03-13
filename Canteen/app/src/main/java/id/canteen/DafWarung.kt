package id.canteen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.canteen.data.Menus
import id.canteen.session.SessionNmWarung
import kotlinx.android.synthetic.main.activity_daf_warung.*
import id.canteen.data.AdapterMenuW
import id.canteen.data.Warung
import id.canteen.session.userNow

class DafWarung : AppCompatActivity() {

    private val ref = FirebaseDatabase.getInstance()
    lateinit var listMenuW: MutableList<Menus>
    private lateinit var dataSess: String
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daf_warung)
        dataSess = SessionNmWarung["Nama"]!!
        listMenuW = mutableListOf()
        listView = findViewById(R.id.recMenu)
        ref.getReference("Warung")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (h in p0.children) {
                            val war = h.getValue(Warung::class.java)
                            if (dataSess == war!!.idUser) {
                                Glide.with(this@DafWarung).load(war.link)
                                    .apply(RequestOptions())
                                    .into(BackWarung)
                            }
                        }
                    }
                }

            })
        ref.getReference("Menus")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        listMenuW.clear()
                        for (h in p0.children) {
                            val menu = h.getValue(Menus::class.java)
                            if (dataSess == menu!!.idUser) {
                                listMenuW.add(menu)
//                                Toast.makeText(this@DafWarung,menu.nama,Toast.LENGTH_SHORT).show()
                            }
                        }
                        val adapter = AdapterMenuW(this@DafWarung, R.layout.isi_menu, listMenuW)
                        listView.adapter = adapter
                    }
                }
            })
    }

}
