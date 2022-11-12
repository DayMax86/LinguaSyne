package com.example.linguasyne.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.ui.animations.AnimateSuccess
import com.example.linguasyne.viewmodels.LoginViewModel

@Composable
fun LoginScreen(navController: NavHostController) {

    val viewModel = remember { LoginViewModel(navController) }

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
fun LoginDrawerContent(

) {

    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
        ) {

            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.about),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                )
                Icon(
                    Icons.Default.Info,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 6.dp)
                        .size(20.dp, 20.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }

        }

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = MaterialTheme.colors.onBackground,
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
                        .size(width = 200.dp, height = 200.dp)
                        .clip(shape = CircleShape),
                    model = R.drawable.linguasyne_logo_expanded,
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
                    focusedBorderColor = outlineColour,
                    unfocusedBorderColor = outlineColour,
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
                    focusedBorderColor = outlineColour,
                    unfocusedBorderColor = outlineColour,
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
                }
            )


            Button(
                onClick = { buttonOnClick() },
                shape = RoundedCornerShape(100),
                modifier = Modifier
                    .size(140.dp, 55.dp)
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
                        text = AnnotatedString(
                            text = stringResource(id = R.string.create_account_prompt),
                        ),
                        onClick = textOnClick,
                        style = MaterialTheme.typography.caption,

                    )
                }

            }

        }
    }

}


