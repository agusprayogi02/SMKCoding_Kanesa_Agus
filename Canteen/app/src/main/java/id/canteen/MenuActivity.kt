package id.canteen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.canteen.data.AdapterMenu
import id.canteen.data.Menus
import id.canteen.data.Users
import id.canteen.data.Warung
import id.canteen.session.userNow
import id.canteen.ui.login.LoginActivity
import kotlinx.android.synthetic.main.masukkan.*

class MenuActivity : AppCompatActivity() {

    lateinit var list : MutableList<Menus>
    lateinit var listView: ListView
    var mn_menu : String = ""
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masukkan)

//        text.setOnClickListener {
//            startActivity(Intent(this@MenuActivity,Setting::class.java))
//        }

        firebaseDatabase.getReference("Users").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (i in p0.children) {

                        val user = i.getValue(Users::class.java)
                        if (user!!.current_user == userNow.currrenuser()) {
                            list = mutableListOf()
                            listView = findViewById(R.id.list_view)
                            firebaseDatabase.getReference("Warung").addValueEventListener(
                                object : ValueEventListener{
                                    override fun onCancelled(p0: DatabaseError) {
                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.exists()){
                                            list.clear()
                                            for (h in p0.children){
                                                val warung = h.getValue(Warung::class.java)
                                                if (warung!!.idUser == userNow.currrenuser()) {
                                                    mn_menu = warung.Nama
                                                    text.text = "Menu Warung $mn_menu"
                                                }
                                            }
                                        }
                                    }

                                }
                            )

                            firebaseDatabase.getReference("Menus").addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()){
                                        list.clear()
                                        for (h in p0.children){
                                            val menu = h.getValue(Menus::class.java)
                                            if (userNow.currrenuser() == menu!!.idUser) {
                                                list.add(menu)
                                            }
                                        }
                                        val adapter = AdapterMenu(
                                            this@MenuActivity,
                                            R.layout.activity_menu,
                                            list
                                        )
                                        listView.adapter = adapter
                                    }
                                }
                            })

                        }
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_menu, menu)
        inflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_tambah -> {
                val intent = Intent(this,Tambah::class.java)
                intent.putExtra("warung",mn_menu)
                startActivity(intent)
                true
            }
            R.id.action_share -> {
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    val uri = "ayo install Canteen School Now"
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Share Here")
                    intent.putExtra(Intent.EXTRA_TEXT,uri)
                    startActivity(Intent.createChooser(intent,"Sharing Option"))
                }catch (exp: Exception){
                    Toast.makeText(applicationContext,"Whatsapp Tidak Terintall!!", Toast.LENGTH_LONG).show()
                }
                true
            }
            R.id.action_settings ->{
                val i = Intent(this@MenuActivity, Setting::class.java)
                startActivity(i)
                true
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val i = Intent(this, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                true
            }
            else -> {
                false
            }
        }
    }

}
