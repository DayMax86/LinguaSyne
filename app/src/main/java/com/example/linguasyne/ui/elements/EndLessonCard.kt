package com.example.linguasyne.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R
@Composable
fun EndLessonCard(
    modifier: Modifier,
    buttonOnClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
    ) {
        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.lesson_end),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.complete_lesson),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
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
                        text = stringResource(R.string.continue_string),
                    )
                }

            }

        }
    }

}

