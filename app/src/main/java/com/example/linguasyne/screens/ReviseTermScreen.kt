package com.example.linguasyne.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.util.CoilUtils
import com.example.linguasyne.R
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.managers.RevisionSessionManager
import com.example.linguasyne.ui.animations.AnimateSuccess
import com.example.linguasyne.viewmodels.ReviseTermViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviseTermScreen(
    viewModel: ReviseTermViewModel,
) {

    Surface(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxHeight()

    ) {
        DisplaySummary(
            display = viewModel.displaySummary,
            viewModel = viewModel
        )

        DisplayEndSessionWarning(
            display = viewModel.displayEndSessionWarning,
            onConfirmDialogueButton = viewModel::onEndPressed,
            onDismissDialogueButton = viewModel::onBackPressed,
        )

        Column(
            modifier = Modifier.blur(
                viewModel.blurAmount.dp,
                viewModel.blurAmount.dp,
                BlurredEdgeTreatment.Rectangle
            )
        ) {

            ViewTerm(
                display = viewModel.displayTerm,
                backBehaviour = viewModel::onBackPressed,
                termName = viewModel.currentTermTitle,
                userInput = viewModel.userInput,
                onClickSubmit = viewModel::handleSubmit,
                onClickMasc = viewModel::handleMascClick,
                onClickFem = viewModel::handleFemClick,
                handleChange = viewModel::handleInput,
                textFieldOutlineColour = viewModel.textFieldOutlineColour.colourDefinitions,
                mascOutlineColour = viewModel.mascOutlineColour.colourDefinitions,
                femOutlineColour = viewModel.femOutlineColour.colourDefinitions,
                selectGenderTextColour = viewModel.selectGenderTextColour.colourDefinitions,
                mascImage = viewModel.mascImage,
                femImage = viewModel.femImage,
                flagEmoji = viewModel.flagEmoji,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AnimateSuccess(
                animate = viewModel.animateCorrect,
                animationSpec = tween(viewModel.animateDuration.toInt()),
                initialScale = 0f,
                transformOrigin = TransformOrigin.Center,
            )
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayEndSessionWarning(
    display: Boolean,
    onConfirmDialogueButton: () -> Unit,
    onDismissDialogueButton: () -> Unit,
) {
    if (display) {
        EndRevisionSessionScreen(
            onConfirmDialogueButton = onConfirmDialogueButton,
            onDismissDialogueButton = onDismissDialogueButton,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplaySummary(display: Boolean, viewModel: ReviseTermViewModel) {
    if (display) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            RevisionSummaryScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun ViewTerm(
    display: Boolean,
    backBehaviour: () -> Unit,
    termName: String?,
    userInput: String,
    handleChange: (String) -> Unit,
    onClickSubmit: () -> Unit,
    onClickMasc: () -> Unit,
    onClickFem: () -> Unit,
    textFieldOutlineColour: Color,
    mascOutlineColour: Color,
    femOutlineColour: Color,
    selectGenderTextColour: Color,
    mascImage: Int,
    femImage: Int,
    flagEmoji: String,
) {
    if (display) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
        ) {

            BackHandler {
                backBehaviour()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = termName.toString(),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1
                )


            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userInput,
                    onValueChange = { handleChange(it) },
                    label = { Text(stringResource(id = R.string.enter_translation)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textFieldOutlineColour,
                        unfocusedBorderColor = textFieldOutlineColour,
                        textColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.secondary,
                        placeholderColor = MaterialTheme.colors.secondary,
                        focusedLabelColor = MaterialTheme.colors.secondary,
                    ),
                    trailingIcon = @Composable { Text(flagEmoji) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        onClickSubmit()
                    }
                )


            }

            Row(
                modifier = Modifier
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.select_gender),
                    color = selectGenderTextColour,
                    style = MaterialTheme.typography.body1,
                    textDecoration = if (mascImage == R.drawable.alphamars || femImage == R.drawable.alphavenus) { //Strikethrough if gender doesn't need to be selected (alpha images are always used in this case)
                        TextDecoration.LineThrough
                    } else {
                        null
                    }
                )
            }

            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

                Row {
                    Image(
                        painterResource(mascImage),
                        modifier = Modifier
                            .border(2.dp, mascOutlineColour, RoundedCornerShape(10))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onClickMasc()
                            }
                            .size(width = 75.dp, height = 100.dp)
                            .clip(shape = RectangleShape),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                }

                Row(
                    modifier = Modifier
                ) {
                    Image(
                        painterResource(femImage),
                        modifier = Modifier
                            .border(2.dp, femOutlineColour, RoundedCornerShape(10))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onClickFem()
                            }
                            .size(width = 75.dp, height = 100.dp)
                            .clip(shape = RectangleShape),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                }

            }


            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {

                Button(
                    onClick = { onClickSubmit() },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier.size(200.dp, 45.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.onSurface,
                    ),
                )
                {
                    Text(
                        text = stringResource(id = R.string.submit),
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }

        }
    }
}
