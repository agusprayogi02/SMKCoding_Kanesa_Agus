package id.canteen

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.canteen.data.Users
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.google.firebase.auth.FirebaseAuth
import id.canteen.ui.login.LoginActivity


class SignUp : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSpinner()
        setlevel()
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Users")
        btnsignup.setOnClickListener {
            sigup()
        }
    }

    private fun setlevel() {
        val adapter = ArrayAdapter.createFromResource(this,R.array.level, R.layout.support_simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        hallevel.adapter = adapter
    }

    private fun sigup() {
        val error = "Harus di Isi !!"
        val user = inuser.text.toString()
        val pass = inpass.text.toString()
        val warung: String
        if (inname.text!!.isEmpty()) {
            inname.error = error
        } else if (inuser.text!!.isEmpty()) {
            inuser.error = error
        }else if(inpass.length() <= 6){
            inpass.error = "Password Minimal 6 Karakter!!"
        } else if (inpass.text!!.isEmpty()) {
            inpass.error = error
        } else if (hallevel.selectedItem.toString().equals("Pilih Jenis User",true)) {
            Toast.makeText(this,"Jenis User "+error,Toast.LENGTH_LONG).show()
        } else if (jk.selectedItem.toString() == "Pilih Jenis Kelamin") {
            Toast.makeText(this,"Jenis Kelamin "+error,Toast.LENGTH_LONG).show()
        }else {
            mAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val usercur = mAuth.currentUser!!.uid
                        Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG).show()
                        savedata(usercur)
                    } else Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG).show()
                    // If sign in fails, display a message to the user.
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }
//            savedata()
//            Toast.makeText(this, "gagal", Toast.LENGTH_LONG).show()
//            startActivity(Intent(this,SignUp::class.java))
        }
    }

    private fun savedata(cur : String) {
        val level: String
        if(hallevel.selectedItem.toString().equals("Penjual",true)){
            level = "Member"
        }else{
            level = "User"
        }
        val uang = 0
        val war = "Nama warung"
        val nama = inname.text.toString()
        val user = inuser.text.toString()
        val pass = inpass.text.toString()
        val jenkel = jk.selectedItem.toString()
        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("Users/$uid")
        db.setValue(Users(cur ,nama, user, pass, jenkel ,level , uang, war))
            .addOnCompleteListener {
                val progressDialog = ProgressDialog(
                    this,
                    R.style.Theme_MaterialComponents_Light_Dialog
                )
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Authenticating...")
                progressDialog.show()
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    progressDialog.show()
                } else {
                    progressDialog.show()
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
