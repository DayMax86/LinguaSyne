package com.example.linguasyne.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.linguasyne.FirebaseManager
import com.example.linguasyne.R
import com.example.linguasyne.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        findViewById<Button>(R.id.login_button).setOnClickListener{
            logInUser()
            val intent = Intent(this, HomeActivity::class.java)
            Log.d("LoginActivity", "Attempting to launch HomeActivity")
            startActivity(intent)
        }

        findViewById<TextView>(R.id.create_account_text).setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            Log.d("LoginActivity", "Attempting to launch CreateAccountActivity from OnClickListener (create_account_text)")
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            //If user is already logged in, launch go to home screen.
            FirebaseManager.current_user = User(auth.currentUser!!.email.toString())
            val intent = Intent(this, HomeActivity::class.java)
            Log.d("LoginActivity", "Attempting to launch HomeActivity from onStart()")
            startActivity(intent)
            finish()
        }
    }

    private fun logInUser() {
        FirebaseAuth.getInstance()

        val email = findViewById<TextView>(R.id.email_login_textbox).text.toString()
        val password = findViewById<EditText>(R.id.password_login_textbox).text.toString()

        if (email != "" && password != "") {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    Log.d("LoginActivity", "User logged in")
                    Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                    FirebaseManager.current_user = User(email)
                }
                .addOnFailureListener {
                    Log.d("LoginActivity", "User log in failed")
                    Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
                }

        }
        else {
            Log.d("LoginActivity", "Either email or password is null")
            Toast.makeText(this,"Please enter both an email address and password", Toast.LENGTH_LONG).show()
        }

    }


}