package id.canteen.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import id.canteen.MainActivity
import id.canteen.R
import id.canteen.SignUp
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        Iklan
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
//        akhir Iklan

        add.setOnClickListener {
            Masuk()
        }
        login.setOnClickListener {
            val email = username.text.toString()
            val password = password.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Insert Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val progressDialog = ProgressDialog(
                this,
                R.style.Theme_MaterialComponents_Light_Dialog
            )
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Authenticating...")
            progressDialog.show()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (!it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        progressDialog.hide()
                        return@addOnCompleteListener

                    } else
                        Toast.makeText(this, "Succesfully Login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    progressDialog.hide()
                    finish()
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed Login: ${it.message}")
                    Toast.makeText(this, "Email/Password incorrect", Toast.LENGTH_SHORT).show()

                }
        }
        }

    private fun Masuk() {
        val i = Intent(this, SignUp::class.java)
        startActivity(i)
    }
}
