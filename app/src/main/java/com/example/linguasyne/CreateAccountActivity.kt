package com.example.linguasyne

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account_activity)

        findViewById<EditText>(R.id.password_register_textbox).addTextChangedListener {
            //Check the strength of the user's proposed password without ever showing it in plaintext, then indicate the strength in the UI.

            val pBar: ProgressBar = findViewById<ProgressBar>(R.id.password_register_progressbar)
            when (
                checkPasswordStrength(findViewById<EditText>(R.id.password_register_textbox).text.toString())) {
                PasswordStrengths.WHITESPACE -> {
                    pBar.progress = 0
                    findViewById<TextView>(R.id.password_strength_textview).text =
                        "password cannot contain spaces!"
                }
                PasswordStrengths.SHORT -> {
                    pBar.progress = 0
                    findViewById<TextView>(R.id.password_strength_textview).text =
                        "too short!"
                }
                PasswordStrengths.VERY_WEAK -> {
                    pBar.progress = 20
                    findViewById<TextView>(R.id.password_strength_textview).text =
                        "very weak"
                }
                PasswordStrengths.WEAK -> {
                    pBar.progress = 40
                    findViewById<TextView>(R.id.password_strength_textview).text =
                        "weak"
                }
                PasswordStrengths.AVERAGE -> {
                    pBar.progress = 60
                    findViewById<TextView>(R.id.password_strength_textview).text =
                        "average"
                }
                PasswordStrengths.STRONG -> {
                    pBar.progress = 80
                    findViewById<TextView>(R.id.password_strength_textview).text =
                        "strong"
                }
                PasswordStrengths.VERY_STRONG -> {
                    pBar.progress = 100
                    findViewById<TextView>(R.id.password_strength_textview).text =
                        "very strong!"
                }
                //JUST FOR TESTING
                PasswordStrengths.ERROR -> Toast.makeText(
                    this,
                    "Error in displaying password strength",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
        findViewById<EditText>(R.id.password_register_textbox).setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                ShowHidePasswordStrength(false)
            } else {
                ShowHidePasswordStrength(true)
            }
        }

        findViewById<ProgressBar>(R.id.password_register_progressbar).setOnClickListener {
            ShowHidePasswordStrength(false)
        }


        findViewById<Button>(R.id.create_account_button).setOnClickListener {
            val email = findViewById<TextView>(R.id.email_register_textbox).text.toString()
            val password = findViewById<EditText>(R.id.password_register_textbox).text.toString()
            //Check password is long enough
            if (checkPasswordStrength(password) == PasswordStrengths.SHORT) {
                Toast.makeText(
                    this,
                    "Password must be more than 8 characters in length!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            //Check there is no whitespace in the password
            if (checkPasswordStrength(password) == PasswordStrengths.WHITESPACE) {
                Toast.makeText(
                    this,
                    "Password cannot contain spaces!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("CreateAccountActivity", "createUserWithEmail:SUCCESS, $email")
                        val user = User(email)
                        FirebaseManager.current_user = user
                        Toast.makeText(
                            baseContext,
                            "Account successfully created",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(
                            "CreateAccountActivity",
                            "createUserWithEmail:FAILURE",
                            task.exception
                        )
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_LONG)
                            .show()
                        //updateUI(null)
                    }

                }
            val user = User(email)
            addUserToFirebase(user)

        }

        findViewById<TextView>(R.id.return_to_login_text).setOnClickListener {
            finish()
        }

    }

    private fun ShowHidePasswordStrength(show: Boolean) {
        when (show) {
            false -> {
                findViewById<ProgressBar>(R.id.password_register_progressbar).visibility =
                    View.INVISIBLE
                findViewById<TextView>(R.id.password_strength_textview).visibility = View.INVISIBLE

                findViewById<EditText>(R.id.email_register_textbox).visibility = View.VISIBLE
            }
            true -> {
                findViewById<ProgressBar>(R.id.password_register_progressbar).visibility =
                    View.VISIBLE
                findViewById<TextView>(R.id.password_strength_textview).visibility = View.VISIBLE

                findViewById<EditText>(R.id.email_register_textbox).visibility = View.INVISIBLE
            }
        }
    }

    private fun checkPasswordStrength(password: String): PasswordStrengths {
        var score: Int = 0
        //Is the pw longer than 8 characters?
        if (password.length > 8) {
            //Allow password
        } else {
            //Do not allow password and alert user that it is too short.
            return PasswordStrengths.SHORT
        }

        //Does the pw include lowercase AND uppercase letters?
        //Does the pw include numbers?
        //Does the pw include other characters?
        //Does the pw have at least 5 unique characters?
        var containsLowercase = false
        var containsUppercase = false
        var containsDigit = false
        var containsSymbol = false

        for (char: Char in password) {
            if (char.isLowerCase()) {
                containsLowercase = true
            }
            if (char.isUpperCase()) {
                containsUppercase = true
            }
            if (char.isDigit()) {
                containsDigit = true
            }
            if (!char.isLetterOrDigit()) {
                containsSymbol = true
            }
        }

        if (containsLowercase && containsUppercase) {
            score += 5
        }
        if (containsSymbol || containsDigit) {
            if (containsSymbol && containsDigit) {
                score += 10
            }
            score += 10
        }
        if (containsLowercase && containsUppercase && containsDigit && containsSymbol) {
            score += 20
        }

        if (password.contains(" ")) {
            return PasswordStrengths.WHITESPACE
        }

        when (score) {
            in 0..4 -> return PasswordStrengths.VERY_WEAK
            in 5..9 -> return PasswordStrengths.WEAK
            in 10..19 -> return PasswordStrengths.AVERAGE
            in 20..24 -> return PasswordStrengths.STRONG
            in 25..100 -> return PasswordStrengths.VERY_STRONG
            else -> return PasswordStrengths.ERROR
        }

    }

    private enum class PasswordStrengths {
        VERY_WEAK,
        WEAK,
        AVERAGE,
        STRONG,
        VERY_STRONG,
        ERROR,
        SHORT,
        WHITESPACE
    }

    private fun addUserToFirebase(user: User) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("CreateAccountActivity", "User added to Firestore")
            }

    }

}