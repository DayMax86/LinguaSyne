package com.example.linguasyne.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R
import com.example.linguasyne.ui.theme.LinguaSyneTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EndRevisionSessionScreen(
    onConfirmDialogueButton: () -> Unit,
    onDismissDialogueButton: () -> Unit,
) {

    LinguaSyneTheme(
        false,
    ) {
        DisplayEndSession(
            onConfirmDialogueButton = onConfirmDialogueButton,
            onDismissDialogueButton = onDismissDialogueButton,
        )
    }
}


@Composable
fun DisplayEndSession(
    onConfirmDialogueButton: () -> Unit,
    onDismissDialogueButton: () -> Unit,
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AlertDialog(
            modifier = Modifier
                .border(
                    color = MaterialTheme.colors.primary,
                    width = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                ),
            shape = RoundedCornerShape(size = 16.dp),
            backgroundColor = MaterialTheme.colors.onBackground,

            onDismissRequest = { /*TODO*/ },
            title = {
                Text(
                    modifier = Modifier
                        .padding(bottom = 32.dp),
                    text = stringResource(id = R.string.end_session_warning_prompt),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                )
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = { onConfirmDialogueButton() },
                        shape = RoundedCornerShape(100),
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.error,
                            contentColor = MaterialTheme.colors.onSurface,
                        ),
                        content = {
                            Text(text = stringResource(id = R.string.end_session))
                        }
                    )
                }
            },
            dismissButton = {
                Column(
                    modifier = Modifier
                        //.padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = { onDismissDialogueButton() },
                        shape = RoundedCornerShape(100),
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSurface,
                        ),
                        content = {
                            Text(text = stringResource(id = R.string.resume))
                        }
                    )
                }
            },
        )


    }

}

