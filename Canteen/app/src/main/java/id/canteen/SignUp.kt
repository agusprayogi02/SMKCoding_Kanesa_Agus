@file:Suppress("DEPRECATION")

package id.canteen

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.canteen.data.Users
import id.canteen.data.Warung
import id.canteen.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_sign_up.*


@Suppress("DEPRECATION")
class SignUp : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var ref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        progressDialog = ProgressDialog(
            this,
            R.style.Theme_MaterialComponents_Light_Dialog
        )
        val anim = AnimationUtils.loadAnimation(this, R.anim.shake)
        setSpinner()
        setlevel()
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Users")

        hallevel.onItemSelectedListener = this
        btnsignup.setOnClickListener {
            btnsignup.startAnimation(anim)
            sigup()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        if (hallevel.selectedItem.toString().equals("Pilih Jenis User", true)) {
            adpwar.isVisible = false
        } else if (hallevel.selectedItem.toString().equals("Penjual", true)) {
            adpwar.isVisible = true
        } else if (hallevel.selectedItem.toString().equals("Pembeli", true)) {
            adpwar.isVisible = false
        }

        Inwarung.isVisible = adpwar.isVisible
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun setlevel() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.level,
            R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        hallevel.adapter = adapter
    }

    private fun sigup() {
        val error = "Harus di Isi !!"
        val user = inuser.text.toString()
        val pass = inpass.text.toString()
        when {
            inname.text!!.isEmpty() -> {
                inname.error = error
            }
            inuser.text!!.isEmpty() -> {
                inuser.error = error
            }
            inpass.length() < 6 -> {
                inpass.error = "Password Minimal 6 Karakter!!"
            }
            inpass.text!!.isEmpty() -> {
                inpass.error = error
            }
            hallevel.selectedItem.toString().equals("Pilih Jenis User", true) -> {
                Toast.makeText(this, "Jenis User " + error, Toast.LENGTH_LONG).show()
            }
            jk.selectedItem.toString() == "Pilih Jenis Kelamin" -> {
                Toast.makeText(this, "Jenis Kelamin " + error, Toast.LENGTH_LONG).show()
            }
            else -> {
                if (hallevel.selectedItem.toString().equals("Penjual", true)) {
                    if (Inwarung.text.toString().isEmpty()) {
                        Inwarung.error = error
                    }
                }
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Pembuatan...")
                progressDialog.show()
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
    }

    private fun savedata(cur: String) {
        val level: String
        if (hallevel.selectedItem.toString().equals("Penjual", true)) {
            level = "Member"
        } else {
            level = "User"
        }
        val uang = 0
        val nama = inname.text.toString()
        val user = inuser.text.toString()
        val pass = inpass.text.toString()
        val jenkel = jk.selectedItem.toString()
        var image: String
        image = if (level.equals("Member", true)) {
            if (jenkel.equals("Laki-Laki", true)) {
                "https://firebasestorage.googleapis.com/v0/b/canteen-school.appspot.com/o/images%2Ffoto%2Fmember-lk.png?alt=media&token=ab40e254-89dd-450f-acb7-7c84a01f85ac"
            } else {
                "https://firebasestorage.googleapis.com/v0/b/canteen-school.appspot.com/o/images%2Ffoto%2Fmember-p.png?alt=media&token=4781af40-9ea1-4811-8ede-a1fff0e6509e"
            }
        } else {
            if (jenkel.equals("Laki-Laki", true)) {
                "https://firebasestorage.googleapis.com/v0/b/canteen-school.appspot.com/o/images%2Ffoto%2Fuser-lk.png?alt=media&token=75a3511b-548b-4b14-91f9-6285b84add7f"
            } else {
                "https://firebasestorage.googleapis.com/v0/b/canteen-school.appspot.com/o/images%2Ffoto%2Fuser-p.png?alt=media&token=52a98242-2f99-4d9f-952f-83777dda128a"
            }
        }
        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("Users/$uid")
        if (level.equals("Member", true)) {
            val nmwar = Inwarung.text.toString()
            val linkback =
                "https://firebasestorage.googleapis.com/v0/b/canteen-school.appspot.com/o/images%2Ffoto%2Fwar.jpg?alt=media&token=730c38a1-497c-4c17-969d-d85956549671"
            val back = "war.jpg"
            val war = FirebaseDatabase.getInstance().getReference("Warung/$uid")
            war.setValue(Warung(cur, nmwar, nama, back, linkback))
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

        }
        db.setValue(Users(cur, image, nama, user, pass, jenkel, level, uang))
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    val userProf = UserProfileChangeRequest.Builder().setDisplayName(level).build()
                    val curent = mAuth.currentUser
                    curent?.updateProfile(userProf)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Success!!", Toast.LENGTH_SHORT).show()
                                val i = Intent(this, LoginActivity::class.java)
                                progressDialog.dismiss()
                                startActivity(i)
                                finish()
                            }
                        }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                    finish()
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
