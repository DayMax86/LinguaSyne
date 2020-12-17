package com.example.linguasyne

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        findViewById<Button>(R.id.login_button).setOnClickListener{
            val email = findViewById<TextView>(R.id.email_login_textbox).text.toString()
            val password = findViewById<EditText>(R.id.password_login_textbox).text.toString()
        }

        findViewById<TextView>(R.id.create_account_text).setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            Log.d("LoginActivity", "Attempting to launch CreateAccountActivity")
            startActivity(intent)
        }
    }
}