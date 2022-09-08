package com.example.linguasyne.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.activities.StartActivity
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.White
import com.example.linguasyne.viewmodels.CreateAccountViewModel

@Composable
fun CreateAccountScreen(){

    val viewModel = CreateAccountViewModel()

    ReturnToLogin(toReturn = viewModel.returnToLogin)
    GoToHome(goToHome = viewModel.goToHome)



    DisplayCreateAccount(
        viewModel.userEmailInput,
        viewModel.userPasswordInput,
        handleEmailChange = viewModel::handleEmailChange,
        handlePasswordChange = viewModel::handlePasswordChange,
        emailOutlineColour = viewModel.emailOutlineColour,
        passwordOutlineColour = viewModel.passwordOutlineColour,
        buttonOnClick = viewModel::handleButtonPress,
        textOnClick = viewModel::handleTextPress,
        userImage = viewModel.userImage,
        blurAmount = viewModel.blurAmount,
    )

    AnimateSuccessfulLogin(
        animate = viewModel.animateSuccess,
        animationSpec = tween(viewModel.animateDuration.toInt()),
        initialScale = 0f,
        transformOrigin = TransformOrigin.Center,
    )

    TogglePasswordStrengthIndicator(showProgressBar = viewModel.showProgressBar)

}



@Composable
fun ReturnToLogin(toReturn: Boolean) {
    if (toReturn) {
        this.finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun GoToHome(goToHome: Boolean) {
    if (goToHome) {
        this.finish()
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimateSuccessfulLogin(
    animate: Boolean,
    animationSpec: FiniteAnimationSpec<Float>,
    initialScale: Float,
    transformOrigin: TransformOrigin,
) {

    Column(
        modifier = Modifier
            .fillMaxHeight(0.75f),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        )
        {

            AnimatedVisibility(
                modifier = Modifier
                    .padding(2.dp),
                visible = animate,

                enter = scaleIn(
                    animationSpec = animationSpec,
                    initialScale = initialScale,
                    transformOrigin = transformOrigin,
                ) + expandVertically(
                    expandFrom = Alignment.CenterVertically
                ) + expandHorizontally(
                    expandFrom = Alignment.CenterHorizontally
                ),

                exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                Row(
                    Modifier
                        .size(200.dp)
                        .border(4.dp, MaterialTheme.colors.secondary, shape = CircleShape)
                        .background(
                            color = LsCorrectGreen, shape = CircleShape
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        Text(
                            text = String(Character.toChars(0x2713)),
                            //color = LsCorrectGreen,
                            style =
                            TextStyle(
                                color = White,
                                fontSize = 150.sp,
                            ),

                            )
                    }
                }
            }
        }

    }

}

@Composable
fun TogglePasswordStrengthIndicator(
    showProgressBar: Boolean,
) {
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
            .padding(all = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(350.dp))

        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.password_strength),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
            )
            Row(
                modifier = Modifier
                    .padding(start = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    text = passwordStrength,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            backgroundColor = MaterialTheme.colors.onBackground,
            progress = progressBarValue,
        )


    }
}

@Composable
fun DisplayCreateAccount(
    userEmailInput: String,
    userPasswordInput: String,
    handleEmailChange: (String) -> Unit,
    handlePasswordChange: (String, Context) -> Unit,
    emailOutlineColour: Color,
    passwordOutlineColour: Color,
    buttonOnClick: () -> Unit,
    textOnClick: () -> Unit,
    userImage: Uri?,
    blurAmount: Int
) {

    Column(
        modifier = Modifier
            .blur(blurAmount.dp, blurAmount.dp, BlurredEdgeTreatment.Rectangle)
            .fillMaxHeight()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
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
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userEmailInput,
                    onValueChange = { handleEmailChange(it) },
                    label = { Text(stringResource(R.string.email_address)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = emailOutlineColour
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userPasswordInput,
                    onValueChange = { handlePasswordChange(it,this@CreateAccountActivity.baseContext) },
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = passwordOutlineColour
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

            }
        }

        Spacer(modifier = Modifier.padding(bottom = 200.dp))

        Row(
            modifier = Modifier
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
                Text(stringResource(R.string.create_account))
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            horizontalArrangement = Arrangement.Center
        )
        {
            Row(
                modifier = Modifier
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.return_to_login)),
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
        Toast.makeText(this, "${resources.getText(R.string.camera_error_toast)}", Toast.LENGTH_LONG).show()
    }

}

@Deprecated("Deprecated in Java")
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    Log.d("ImageSelector", "Image has been selected")
    viewModel.userImage = data?.data
}