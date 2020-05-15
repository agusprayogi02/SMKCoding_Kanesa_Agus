@file:Suppress("DEPRECATION")

package id.canteen.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.perf.FirebasePerformance
import id.canteen.MainActivity
import id.canteen.MenuActivity
import id.canteen.R
import id.canteen.SignUp
import id.canteen.data.Users
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    lateinit var mAdView: AdView
    lateinit var mAuth: FirebaseAuth
    var ref: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textrun.isSelected = true
//        textrun.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_running))
        val anim = AnimationUtils.loadAnimation(this, R.anim.shake)
        login.animation = anim
        add.animation = anim

        val myTrace = FirebasePerformance.getInstance().newTrace("test_trace")
        myTrace.start()
        progressDialog = ProgressDialog(
            this,
            R.style.Theme_MaterialComponents_Light_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()
        cekuser()
        mAuth = FirebaseAuth.getInstance()
//        Iklan
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
//        akhir Iklan

        ref.getReference("Users")

        textrun.setOnClickListener {
            Masuk()
        }
        add.setOnClickListener {
            it.startAnimation(anim)
            Masuk()
        }
        login.setOnClickListener {
            it.startAnimation(anim)
            val email = username.text.toString()
            val password = password.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Insert Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressDialog.show()
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (!it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        progressDialog.dismiss()
                        return@addOnCompleteListener
                    }
                    Toast.makeText(this, "Succesfully Login", Toast.LENGTH_SHORT).show()
                    getdata()
                    progressDialog.dismiss()
                    finish()
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed Login: ${it.message}")
                    Toast.makeText(this, "Email/Password incorrect", Toast.LENGTH_SHORT).show()

                }
        }

        myTrace.stop()
    }

    private fun cekuser() {
        if (!FirebaseAuth.getInstance().uid.isNullOrEmpty()) {
            progressDialog = ProgressDialog(
                this,
                R.style.Theme_MaterialComponents_Light_Dialog
            )
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Authenticating...")
            progressDialog.show()
            getdata()
        }
        progressDialog.dismiss()
    }

    private fun getdata() {
        ref.getReference("Users").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (i in p0.children) {
                        val user = i.getValue(Users::class.java)
                        if (user!!.current_user == mAuth.currentUser!!.uid) {
                            val data = user.level
                            if (data.equals("member", true)) {
                                val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                                startActivity(intent)
                                progressDialog.dismiss()
                                finish()
                            } else {
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                progressDialog.dismiss()
                                finish()
                            }
                        }
                    }
                }
            })
    }

    private fun Masuk() {
        val i = Intent(this, SignUp::class.java)
        startActivity(i)
    }
}
