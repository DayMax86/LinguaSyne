package com.example.linguasyne.ui.elements

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.linguasyne.R
import com.example.linguasyne.gitexclude.HiddenData
import com.example.linguasyne.managers.CSVManager
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.viewmodels.ReviseTermViewModel

@Composable
fun MainDrawerContent(
    screenContent: @Composable () -> Unit,
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
                        text = stringResource(id = R.string.linguasyne),
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )

                    Image(
                        painterResource(R.drawable.linguasyne_logo),
                        modifier = Modifier
                            .size(width = 50.dp, height = 50.dp)
                            .padding(start = 10.dp, top = 6.dp)
                            .border(2.dp, MaterialTheme.colors.secondary, RoundedCornerShape(10))
                            .clip(shape = RectangleShape),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
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
                horizontalArrangement = Arrangement.Start,
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                    Icon(
                        Icons.Default.Settings,
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

        screenContent()

        //TODO() Add app version text here

    }
}

@Composable
fun HomeDrawerContent(
    signOut: () -> Unit,
    context: Context,
) {

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
            modifier = Modifier
                .clickable {
                    signOut()
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

    if (FirebaseManager.currentUser?.id == HiddenData.ADMIN_USER_ID) {
        Column(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max),
            horizontalAlignment = Alignment.Start,
        ) {

            Row(
                modifier = Modifier
                    .clickable {
                        uploadLanguageData(context)
                    },
                horizontalArrangement = Arrangement.Start,
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = "Upload CSV data",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

private fun uploadLanguageData(context: Context) {
    /*if (FirebaseManager.currentUser?.id == HiddenData.ADMIN_USER_ID) {
        CSVManager.importVocabCSV(context)
    }*/
    //DEVELOPER USE ONLY.
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviseDrawerContent(
    viewModel: ReviseTermViewModel,
) {
    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    viewModel.onBackPressed()
                },
            horizontalArrangement = Arrangement.Start,
        ) {

            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.end_session),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                )
                Icon(
                    Icons.Default.Done,
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
