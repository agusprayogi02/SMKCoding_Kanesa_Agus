package id.canteen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.canteen.data.Users
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.google.firebase.auth.FirebaseAuth
import id.canteen.ui.login.LoginActivity


class SignUp : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
//    private val nama = inname.text.toString()
//    private val user = inuser.text.toString()
//    private val pass = inpass.text.toString()
//    private val level = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSpinner()
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("USERS")
        btnsignup.setOnClickListener {
            sigup()
        }
    }

    private fun sigup() {
        val error = "Harus di ISi"
        val nama = inname.text.toString()
        val user = inuser.text.toString()
        val pass = inpass.text.toString()
        if (inname.text!!.isEmpty()) {
            inname.error = error
        } else if (inuser.text!!.isEmpty()) {
            inuser.error = error
        } else if (inpass.text!!.isEmpty()) {
            inpass.error = error
        } else if (konpass.text.toString() != inpass.text.toString()) {
            konpass.error = "Konfigurasi Password Salah"
        } else if (konpass.text!!.isEmpty()) {
            konpass.error = error
        } else {
            mAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = mAuth.getCurrentUser()
                        Toast.makeText(this, "$user", Toast.LENGTH_LONG).show()
//                        savedata()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }
            val level = "user"
            val users = Users(nama, user, pass, level)
            val userId = ref.push().key.toString()
            ref.child(userId).setValue(users).addOnCompleteListener {
                Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
//                val i = Intent(this, LoginActivity::class.java)
//                startActivity(i)
            }
        }
    }

    private fun savedata() {

        val level = "user"
        val nama = inname.text.toString()
        val user = inuser.text.toString()
        val pass = inpass.text.toString()
        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("User/$uid")
        db.setValue(Users(nama, user, pass, level))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
//                    val i = Intent(this, LoginActivity::class.java)
//                    startActivity(i)
                } else {
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setSpinner() {
        val adapter =
            ArrayAdapter.createFromResource(this, R.array.jk, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jk.adapter = adapter
    }
}
