package com.example.linguasyne.ui.elements

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R
import com.example.linguasyne.HiddenData
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.viewmodels.ReviseTermViewModel

@Composable
fun ShowSettings(
    visible: Boolean,
    darkMode: Boolean,
    toggleDarkMode: (Boolean) -> Unit,
) {
    if (visible) {
        Column(
            modifier = Modifier
                //.border(2.dp, Color.Red)
                .fillMaxHeight()
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
                    .fillMaxHeight()
                    //.width(intrinsicSize = IntrinsicSize.Max),
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.dark_mode),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )

                    Switch(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp),
                        checked = darkMode,
                        onCheckedChange = toggleDarkMode,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colors.primary,
                            checkedTrackColor = MaterialTheme.colors.secondary,
                            uncheckedThumbColor = MaterialTheme.colors.primaryVariant,
                            uncheckedTrackColor = MaterialTheme.colors.onBackground,
                        )
                    )

                }

                Divider(
                    modifier = Modifier
                        .height(1.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }
    }
}

@Composable
fun MainDrawerContent(
    screenContent: @Composable () -> Unit,
    settings: @Composable () -> Unit,
    toggleSettingsDisplay: () -> Unit,
    shareIntent: Intent,
    context: Context,
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
                            .border(
                                2.dp,
                                MaterialTheme.colors.secondary,
                                RoundedCornerShape(10)
                            )
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
                modifier = Modifier
                    .clickable {
                        toggleSettingsDisplay()
                    },
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

        settings()

        Column(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .clickable { context.startActivity(shareIntent) },
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
    aboutLinguaSyne: () -> Unit,
    context: Context,
) {

    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .clickable { aboutLinguaSyne() },
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

    /*Column(
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
    }*/

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
