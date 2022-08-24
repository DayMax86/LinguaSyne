package com.example.linguasyne.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.widget.addTextChangedListener
import coil.compose.AsyncImage
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.R
import com.example.linguasyne.classes.User
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.CreateAccountViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.reflect.KFunction0

class CreateAccountActivity : AppCompatActivity() {

    val viewModel = CreateAccountViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TogglePasswordStrengthIndicator(showProgressBar = viewModel.showProgressBar)
            ReturnToLogin(toReturn = viewModel.returnToLogin)
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
                        userImage = viewModel.userImage,
                    )
                }
            }
        }
    }

    @Composable
    fun ReturnToLogin(toReturn: Boolean) {
        if (toReturn) {
            Toast.makeText(this, "Welcome :)", Toast.LENGTH_LONG).show()
            this.finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun TogglePasswordStrengthIndicator(
        showProgressBar: Boolean,
    ) {
        val viewModel = CreateAccountViewModel()
        if (showProgressBar) {
            DisplayPasswordStrength(
                passwordStrength = viewModel.passwordStrength,
                progressBarValue = viewModel.progressBarValue,
            )
        }
    }

    @Composable
    fun DisplayPasswordStrength(
        passwordStrength: String,
        progressBarValue: Float,
    ) {
        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Yellow)
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(400.dp))

            Row(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Red)
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Password strength:",
                )

                Spacer(modifier = Modifier.width(40.dp))

                Text(
                    text = passwordStrength,
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.onBackground,
                progress = progressBarValue,
            )


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
        textOnClick: () -> Unit,
        userImage: Uri?,
    ) {

        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Yellow)
                .fillMaxHeight()
        ) {

            Row(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Red)
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .clickable {
                            selectImage() //Activity launch. ViewModel image uri set.
                        }
                        .border(
                            color = MaterialTheme.colors.primary,
                            width = 2.dp,
                            shape = CircleShape
                        )
                        .size(width = 150.dp, height = 150.dp)
                        .clip(shape = CircleShape),
                    model = userImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }


            Column(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Yellow)
            ) {
                Row(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Red)
                        .padding(all = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

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
                }

                Row(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Red)
                        .padding(all = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
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
            }

            Row(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Red)
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = { buttonOnClick() },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier.size(200.dp, 45.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                )
                {
                    Text("Create account")
                }

            }

            Row(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Red)
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            )
            {
                Row(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Red)
                        .padding(all = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ClickableText(
                        text = AnnotatedString("Return to login"),
                        onClick = { textOnClick() },
                    )
                }
            }
        }

    }


    private fun selectImage() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        val PICK_IMAGE = 1
        try {
            startActivityForResult(intent, PICK_IMAGE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            Toast.makeText(this, "Error launching camera", Toast.LENGTH_LONG).show()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("ImageSelector", "Image has been selected")

        val uri = data?.data
        // User needs to be logged in first - wait for successful login
        viewModel.userImage = uri
    }

}