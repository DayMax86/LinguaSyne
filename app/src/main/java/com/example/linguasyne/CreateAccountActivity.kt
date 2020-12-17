package com.example.linguasyne

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account_activity)

        findViewById<Button>(R.id.create_account_button).setOnClickListener {
            val email = findViewById<TextView>(R.id.email_register_textbox).text.toString()
            val password = findViewById<EditText>(R.id.password_register_textbox).text.toString()

            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("CreateAccountActivity", "createUserWithEmail:SUCCESS, $email")
                        val user = auth.currentUser
                        Toast.makeText(baseContext, "Account successfully created", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("CreateAccountActivity", "createUserWithEmail:FAILURE", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_LONG).show()
                        //updateUI(null)
                    }

                }
            val user = User(email)
            addUserToFirebase(user)

        }

        findViewById<TextView>(R.id.return_to_login_text).setOnClickListener{
            finish()
        }

    }

    private fun addUserToFirebase(user: User){
            FirebaseFirestore.getInstance()
                .collection("users")
                .add(user)
                .addOnSuccessListener {
                    Log.d("CreateAccountActivity", "User added to Firestore")
                }

    }

}