package com.FreeLab.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_profiles.*

class MyProfiles : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profiles)

        ambilData()

    }

    private fun ambilData() {
        val bundle = intent.extras

        val nama = bundle!!.getString("nama")
        val gender = bundle.getString("gender")
        val email = bundle.getString("email")
        val telp = bundle.getString("telp")
        val alamat = bundle.getString("alamat")

        txtnm.text = nama
        txtkl.text = gender
        txteml.text = email
        txttlp.text = telp
        txtAddress.text = alamat

    }
}
