package com.example.linguasyne.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.ReviseTermViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EndRevisionSessionScreen(
    buttonOnClick: () -> Unit,
) {

    LinguaSyneTheme(
        false,
    ) {
        DisplayEndSession(
            buttonOnClick = buttonOnClick
        )
    }
}


@Composable
fun DisplayEndSession(
    buttonOnClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.end_session_warning_prompt),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
        )

        Button(
            onClick = { buttonOnClick() },
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(bottom = 16.dp, top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSurface,
            ),
            content = {
                Text(text = stringResource(id = R.string.end_session))
            }
        )

    }

}

