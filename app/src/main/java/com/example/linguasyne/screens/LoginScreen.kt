package com.example.linguasyne.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.White
import com.example.linguasyne.viewmodels.LoginViewModel

@Composable
fun LoginScreen(navController: NavHostController) {

    val viewModel = LoginViewModel(navController)
    viewModel.init()

    DisplayLogin(
        viewModel.userEmailInput,
        viewModel.userPasswordInput,
        handleEmailChange = viewModel::handleEmailChange,
        handlePasswordChange = viewModel::handlePasswordChange,
        outlineColour = viewModel.outlineColour,
        buttonOnClick = viewModel::handleLogin,
        textOnClick = viewModel::handleTextPress,
        blurAmount = viewModel.blurAmount,
    )

    AnimateSuccess(
        animate = viewModel.animateSuccess,
        animationSpec = tween(viewModel.animateDuration.toInt()),
        initialScale = 0f,
        transformOrigin = TransformOrigin.Center,
    )
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
    blurAmount: Int,
) {

    Column(
        modifier = Modifier
            .blur(blurAmount.dp, blurAmount.dp, BlurredEdgeTreatment.Rectangle)
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
                        text = stringResource(id = R.string.email_address),
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
                        text = stringResource(id = R.string.password),
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
                    text = stringResource(id = R.string.log_in),
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
                        text = AnnotatedString(stringResource(id = R.string.create_account_prompt)),
                        onClick = textOnClick,
                        style = MaterialTheme.typography.body1,
                    )
                }

            }

        }
    }

}


