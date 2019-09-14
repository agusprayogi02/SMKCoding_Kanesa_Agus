package id.canteen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.canteen.data.Users
import id.canteen.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSpinner()

        ref = FirebaseDatabase.getInstance().getReference("USERS")
        btnsignup.setOnClickListener{
            sigup()
        }
    }

    private fun sigup() {
        val error = "Harus di ISi"
        val nama = inname.text.toString()
        val user = inuser.text.toString()
        val pass = inpass.text.toString()
        val level = "user"
        if (inname.text!!.isEmpty()){
            inname.error = error
        }else if(inpass.text!!.isEmpty()){
            inpass.error = error
        }else if(konpass.text.toString() != inpass.text.toString()){
            konpass.error = "Konfigurasi Password Salah"
        }else if(konpass.text!!.isEmpty()){
            konpass.error = error
        }else {
            val users = Users(nama,user,pass,level)
            val userId = ref.push().key.toString()
            ref.child(userId).setValue(users).addOnCompleteListener {
                Toast.makeText(this, "Successs",Toast.LENGTH_SHORT).show()
                val i = Intent(this,LoginActivity::class.java)
                startActivity(i)
            }
        }
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter.createFromResource(this,R.array.jk, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jk.adapter = adapter
    }
}
