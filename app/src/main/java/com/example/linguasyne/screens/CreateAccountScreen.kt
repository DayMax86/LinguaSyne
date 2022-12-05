package com.example.linguasyne.screens

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.ui.animations.AnimateLoading
import com.example.linguasyne.ui.animations.AnimateSuccess
import com.example.linguasyne.viewmodels.CreateAccountViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateAccountScreen(navController: NavHostController) {

    val viewModel = remember { CreateAccountViewModel(navController) }

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

    AnimateLoading(
        animate = viewModel.showLoadingAnim,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
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
            .fillMaxHeight(0.35f)
            .background(color = MaterialTheme.colors.background)
            //.border(2.dp, MaterialTheme.colors.primary)
            .padding(all = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

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

@OptIn(ExperimentalComposeUiApi::class)
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
    blurAmount: Int,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

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
                        .clickable {
                            onClickProfileImage //TODO()
                        }
                        .border(
                            color = MaterialTheme.colors.secondary,
                            width = 4.dp,
                            shape = CircleShape
                        )
                        .size(width = 200.dp, height = 200.dp)
                        .clip(shape = CircleShape),
                    model = userImage ?: R.drawable.default_profile_image,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )

            }

            OutlinedTextField(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                value = userEmailInput,
                onValueChange = { handleEmailChange(it) },
                label = { Text(text = stringResource(id = R.string.email_address)) },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = emailOutlineColour,
                    unfocusedBorderColor = MaterialTheme.colors.primaryVariant,
                    textColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.secondary,
                    placeholderColor = MaterialTheme.colors.secondary,
                    focusedLabelColor = MaterialTheme.colors.secondary,
                ),
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                value = userPasswordInput,
                onValueChange = { handlePasswordChange(it) },
                label = { Text(text = stringResource(id = R.string.password)) },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = passwordOutlineColour,
                    unfocusedBorderColor = MaterialTheme.colors.primaryVariant,
                    textColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.secondary,
                    placeholderColor = MaterialTheme.colors.secondary,
                    focusedLabelColor = MaterialTheme.colors.secondary,
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions {
                    buttonOnClick()
                    //keyboardController?.hide()
                }
            )


            Button(
                onClick = { buttonOnClick() },
                shape = RoundedCornerShape(100),
                modifier = Modifier
                    .size(200.dp, 55.dp)
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSurface,
                )
            )
            {
                Text(
                    text = stringResource(id = R.string.create_account),
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
                        text = AnnotatedString(
                            text = stringResource(id = R.string.return_to_login),
                        ),
                        onClick = { textOnClick() },
                        style = MaterialTheme.typography.caption,

                        )
                }

            }

        }
    }

}
