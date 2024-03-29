package com.daymax86.linguasyne.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daymax86.linguasyne.R
import com.daymax86.linguasyne.ui.theme.LinguaSyneTheme
import com.daymax86.linguasyne.viewmodels.ReviseTermViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RevisionSummaryScreen(
    viewModel: ReviseTermViewModel,
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
            text = (stringResource(R.string.total_correct) + " $totalCorrect"),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.body1,
        )
        Text(
            text = (stringResource(R.string.total_incorrect) + " $totalIncorrect"),
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

