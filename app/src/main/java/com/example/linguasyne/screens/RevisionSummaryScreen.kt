package com.example.linguasyne.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.ReviseTermViewModel

@Composable
fun RevisionSummaryScreen(navController: NavHostController) {

    val viewModel = ReviseTermViewModel(navController)


    LinguaSyneTheme(
        false,
    ) {
        Surface(
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {
            Summary(

                viewModel.summaryTotalCorrect,
                viewModel.summaryTotalIncorrect,
                viewModel::onSummaryButtonPress,

            )
        }
    }

}


@Composable
fun Summary(
    totalCorrect: Int,
    totalIncorrect: Int,
    onSummaryButtonPress: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.summary),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h1
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = (stringResource(R.string.total_correct) + "${totalCorrect}"),
            color = MaterialTheme.colors.primary
        )
        Text(
            text = (stringResource(R.string.total_incorrect) + "${totalIncorrect}"),
            color = MaterialTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onSummaryButtonPress() },
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
        )
        {
            Text(stringResource(R.string.continue_string))
        }
    }
}