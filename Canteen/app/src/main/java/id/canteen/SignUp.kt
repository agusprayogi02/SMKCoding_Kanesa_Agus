package com.kanesa.canteenkanesa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.kanesa.canteenkanesa.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSpinner()
        cencel.setOnClickListener{
            Cencel()
        }
    }

    private fun Cencel() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter.createFromResource(this,R.array.jk, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jk.adapter = adapter
    }
}
