package com.example.linguasyne.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.R
import com.example.linguasyne.classes.User
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.reflect.KFunction0

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = LoginViewModel()
        viewModel.init()

        setContent {
            GoToCreateAccount(goToCreateAccount = viewModel.goToCreateAccount)
            GoToHome(goToHome = viewModel.goToHome)
            LinguaSyneTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                )
                {
                    DisplayLogin(
                        viewModel.userEmailInput,
                        viewModel.userPasswordInput,
                        handleEmailChange = viewModel::handleEmailChange,
                        handlePasswordChange = viewModel::handlePasswordChange,
                        outlineColour = viewModel.outlineColour,
                        buttonOnClick = viewModel::handleButtonPress,
                        textOnClick = viewModel::handleTextPress,
                    )
                }
            }
        }
    }

    @Composable
    fun GoToCreateAccount(goToCreateAccount: Boolean) {
        if (goToCreateAccount) {
            // Launch the create account screen
            this.finish()
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun GoToHome(goToHome: Boolean) {
        if (goToHome) {
            // Launch the home screen
            this.finish()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun DisplayLogin(
        userEmailInput: String,
        userPasswordInput: String,
        handleEmailChange: (String) -> Unit,
        handlePasswordChange: (String) -> Unit,
        outlineColour: Color,
        buttonOnClick: () -> Unit,
        textOnClick: (Int) -> Unit,
    ) {

        Column(
            modifier = Modifier
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(110.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = userEmailInput,
                onValueChange = { handleEmailChange(it) },
                label = { Text("Email address") },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = outlineColour
                ),
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = userPasswordInput,
                onValueChange = { handlePasswordChange(it) },
                label = { Text("Password") },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = outlineColour
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )


        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(250.dp))

            Button(
                onClick = { buttonOnClick() },
                shape = RoundedCornerShape(100),
                modifier = Modifier.size(200.dp, 45.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            )
            {
                Text("Log in")
            }

            Spacer(modifier = Modifier.height(20.dp))

            ClickableText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = AnnotatedString("No account? Click here to create one"),
                onClick = textOnClick
            )

        }


    }
/*
---------------------------- OLD -----------------------

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
---------------------------------------------------------------------  */


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