package id.canteen

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import id.canteen.data.Users
import id.canteen.ui.login.LoginActivity
import id.canteen.ui.histori.HistoriFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    var mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkoutuser()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        if (!FirebaseAuth.getInstance().uid.isNullOrEmpty()){
            val headerview = nav_view.getHeaderView(0)
            val UserPeng : TextView = headerview.findViewById(R.id.UserPeng)
            val username : TextView = headerview.findViewById(R.id.penguna)
            val image: ImageView = headerview.findViewById(R.id.ImgPeng)
            UserPeng.text = mAuth.currentUser!!.email
            setSupportActionBar(toolbar)

            ref.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        for (h in p0.children){
                            val user = h.getValue(Users::class.java)
                            if(user!!.current_user == mAuth.currentUser!!.uid){
                                username.text = user.nama
//                                Picasso.get().load(user.image).into(image)
                                Glide.with(this@MainActivity).load(user.image)
                                    .apply(RequestOptions())
                                    .into(image)
                                if (user.level.equals("member",true)){
                                    val i = Intent(this@MainActivity, MenuActivity::class.java)
                                    startActivity(i)
                                }
                            }
                        }
                    }
                }
            })
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_user,
                R.id.nav_histori, R.id.nav_share, R.id.nav_info
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun checkoutuser() {
        if(FirebaseAuth.getInstance().uid.isNullOrEmpty()){
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                HistoriFragment()
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
                    Toast.makeText(applicationContext,"Whatsapp Tidak Terintall!!",Toast.LENGTH_LONG).show()
                }
                true
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val i = Intent(this, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
