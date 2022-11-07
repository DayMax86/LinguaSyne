package com.example.linguasyne.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.ReviseTermViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RevisionSummaryScreen(
    viewModel: ReviseTermViewModel,
) {

    LinguaSyneTheme(
        false,
    ) {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize(),
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
            text = (stringResource(R.string.total_correct) + " ${totalCorrect}"),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.body1,
        )
        Text(
            text = (stringResource(R.string.total_incorrect) + " ${totalIncorrect}"),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.body1,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .width(150.dp),
            onClick = { onSummaryButtonPress() },
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSurface,
            ),
        )
        {
            Text(stringResource(R.string.continue_string))
        }
    }
}

