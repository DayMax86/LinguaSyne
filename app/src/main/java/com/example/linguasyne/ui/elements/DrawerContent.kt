package com.example.linguasyne.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R

@Composable
fun MainDrawerContent(

) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    )
    {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
            ) {

                Row(
                    modifier = Modifier
                        .width(intrinsicSize = IntrinsicSize.Min)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                    Icon(
                        Icons.Default.Settings,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(40.dp, 40.dp),
                        contentDescription = null,
                        tint = MaterialTheme.colors.secondary
                    )
                }

            }

            Divider(
                modifier = Modifier
                    .height(3.dp),
                color = MaterialTheme.colors.onBackground,
            )
        }

        Column(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        //launchTermBase()
                    },
                horizontalArrangement = Arrangement.Start,
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(id = R.string.term_base),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                    Icon(
                        Icons.Default.Search,
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
                        text = stringResource(id = R.string.help),
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

        Column(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                /*modifier = Modifier.clickable { //TODO() FOR DEVELOPER USE ONLY!! Will be removed in release version.
        CSVManager.importVocabCSV(this@StartActivity.applicationContext)
    },*/
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
                        text = stringResource(id = R.string.share),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                    Icon(
                        Icons.Default.Share,
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

        Column(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max),
            horizontalAlignment = Alignment.Start,
        ) {

            Row(
                modifier = Modifier
                    .clickable {
                        //signOut()
                    },
                horizontalArrangement = Arrangement.Start,
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_out),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                    Icon(
                        Icons.Default.AccountCircle,
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
}
