package id.canteen.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import id.canteen.MainActivity
import id.canteen.R
import id.canteen.SignUp
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
                        return@addOnCompleteListener
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        progressDialog.hide()

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
