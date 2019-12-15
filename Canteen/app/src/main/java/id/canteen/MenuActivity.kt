package id.canteen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.canteen.data.Menus
import id.canteen.data.Users
import id.canteen.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.masukkan.*

class MenuActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Menus>
    lateinit var listView: ListView
    var mn_menu : String = ""
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masukkan)
        firebaseDatabase.getReference("Users").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (i in p0.children) {
                        val user = i.getValue(Users::class.java)
                        if (user!!.current_user == mAuth.currentUser!!.uid) {
                            mn_menu = user.warung
                            text.setText("Menu Warung "+mn_menu)
                            ref = FirebaseDatabase.getInstance().getReference("Menus")
                            list = mutableListOf()
                            listView = findViewById(R.id.list_view)


                            ref.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()){
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
                val i = Intent(this@MenuActivity, setting::class.java)
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
