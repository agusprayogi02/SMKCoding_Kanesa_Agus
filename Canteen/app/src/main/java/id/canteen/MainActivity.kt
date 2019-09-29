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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import id.canteen.ui.login.LoginActivity
import id.canteen.ui.histori.HistoriFragment


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkoutuser()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
                    val uri: String = "ayo install Canteen School Now"
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
