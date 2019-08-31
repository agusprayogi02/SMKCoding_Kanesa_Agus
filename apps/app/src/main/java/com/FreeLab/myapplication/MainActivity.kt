package com.FreeLab.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.FreeLab.myapplication.MyProfiles
import com.FreeLab.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var namaInput : String = ""
    var emailInput : String = ""
    var telpInput : String = ""
    var alamatInput : String = ""
    var genderInput : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave.setOnClickListener { validasiInput() }

        setDataSpinnerGener()

    }

    private fun setDataSpinnerGener() {
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.kl, android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerGender.adapter = adapter
    }

    private fun validasiInput() {
        namaInput = edtName.text.toString()
        emailInput = edtemail.text.toString()
        telpInput = edtTelp.text.toString()
        alamatInput = edtAddress.text.toString()
        genderInput = spinnerGender.selectedItem.toString()

        when{

            namaInput.isEmpty() -> edtName.error = "Nama tidak boleh kosong"
            genderInput.equals("Pilih kelamin") -> tampilToast("Kelamin harus dipilih")
            emailInput.isEmpty() -> edtemail.error = "Email tidak boleh kosong"
            telpInput.isEmpty() -> edtTelp.error = "Telp tidak boleh kosong"
            alamatInput.isEmpty() -> edtAddress.error = "Alamat tidak boleh kosong"

            else -> {

                tampilToast("Navigasi ke halaman profil diri")
                navigasiKeProfilDiri()

            }
        }

    }

    private fun navigasiKeProfilDiri() {

        val intent = Intent(this, MyProfiles::class.java)

        val bundle = Bundle()
        bundle.putString("nama", namaInput)
        bundle.putString("gender", genderInput)
        bundle.putString("email", emailInput)
        bundle.putString("telp", telpInput)
        bundle.putString("alamat", alamatInput)

        intent.putExtras(bundle)

        startActivity(intent)

    }

    private fun tampilToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}