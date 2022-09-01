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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
                .fillMaxHeight(),
        ) {

            Column(
                modifier = Modifier
                    .padding(all = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .border(
                                color = MaterialTheme.colors.secondary,
                                width = 4.dp,
                                shape = CircleShape
                            )
                            .size(width = 225.dp, height = 225.dp)
                            .clip(shape = CircleShape),
                        model = R.drawable.linguasyne_logo,
                        contentDescription = null,
                        contentScale = ContentScale.None,
                    )
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userEmailInput,
                    onValueChange = { handleEmailChange(it) },
                    label = {
                        Text(
                            text = "${resources.getText(R.string.email_address)}",
                            color = MaterialTheme.colors.secondary,
                        )
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = outlineColour,
                        unfocusedBorderColor = Color.Gray,
                        textColor = MaterialTheme.colors.primary,
                    ),
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userPasswordInput,
                    onValueChange = { handlePasswordChange(it) },
                    label = {
                        Text(
                            text = "${resources.getText(R.string.password)}",
                            color = MaterialTheme.colors.secondary,
                        )
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = outlineColour,
                        unfocusedBorderColor = Color.Gray,
                        textColor = MaterialTheme.colors.primary,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )


                Button(
                    onClick = { buttonOnClick() },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier
                        .height(60.dp)
                        .width(150.dp)
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.onSurface,
                    )
                )
                {
                    Text(
                        text = "${resources.getText(R.string.log_in)}",
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(

                    ) {
                        ClickableText(
                            text = AnnotatedString("${resources.getText(R.string.create_account_prompt)}"),
                            onClick = textOnClick,
                        )
                    }

                }

            }
        }

    }


}