package id.canteen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import com.google.firebase.database.*
import id.canteen.data.Menus
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.masukkan.*

class MenuActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Menus>
    lateinit var listView: ListView
    lateinit var mn_menu : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masukkan)

        val iin = intent
        val b = iin.extras!!.get("Nama_menu")
        mn_menu = b.toString()
        text.setText("Menu Warung "+mn_menu)
        ref = FirebaseDatabase.getInstance().getReference("$mn_menu/Menus")
        list = mutableListOf()
        listView = findViewById(R.id.list_view)


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){
                    list.clear()
                    for (h in p0.children){
                        val menu = h.getValue(Menus::class.java)
                        list.add(menu!!)
                    }
                    val adapter = id.canteen.data.AdapterMenu(this@MenuActivity,R.layout.activity_menu,list)
                    listView.adapter = adapter
                }
            }
        })
    }
    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_tambah -> {
                val intent = Intent(this,Tambah::class.java)
                intent.putExtra("warung",mn_menu)
                startActivity(intent)
                true
            }else -> {
                false
            }
        }
    }

}
