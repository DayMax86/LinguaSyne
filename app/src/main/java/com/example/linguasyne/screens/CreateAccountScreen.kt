package com.example.linguasyne.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.activities.StartActivity
import com.example.linguasyne.ui.animations.AnimateSuccess
import com.example.linguasyne.ui.elements.SelectImage
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.White
import com.example.linguasyne.viewmodels.CreateAccountViewModel

@Composable
fun CreateAccountScreen(navController: NavHostController){

    val viewModel = CreateAccountViewModel(navController)

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
        onClickProfileImage = viewModel::uploadUserImage,
        blurAmount = viewModel.blurAmount,
    )

    AnimateSuccess(
        animate = viewModel.animateSuccess,
        animationSpec = tween(viewModel.animateDuration.toInt()),
        initialScale = 0f,
        transformOrigin = TransformOrigin.Center,
    )

    TogglePasswordStrengthIndicator(
        showProgressBar = viewModel.showProgressBar,
        passwordStrength = viewModel.passwordStrength,
        progressBarValue = viewModel.progressBarValue,
    )

}


@Composable
fun TogglePasswordStrengthIndicator(
    showProgressBar: Boolean,
    passwordStrength: Int,
    progressBarValue: Float,
) {
    if (showProgressBar) {
        DisplayPasswordStrength(
            passwordStrength = passwordStrength,
            progressBarValue = progressBarValue,
        )
    }
}

@Composable
fun DisplayPasswordStrength(
    passwordStrength: Int,
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
                    text = stringResource(id = passwordStrength),
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
    handlePasswordChange: (String) -> Unit,
    emailOutlineColour: Color,
    passwordOutlineColour: Color,
    buttonOnClick: () -> Unit,
    textOnClick: () -> Unit,
    userImage: Uri?,
    onClickProfileImage: (Uri) -> Unit,
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
                        onClickProfileImage
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
                    onValueChange = { handlePasswordChange(it) },
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
