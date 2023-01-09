package com.daymax86.linguasyne.ui.elements

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daymax86.linguasyne.BuildConfig
import com.daymax86.linguasyne.R
import com.daymax86.linguasyne.HiddenData
import com.daymax86.linguasyne.activities.StartActivity
import com.daymax86.linguasyne.managers.CSVManager
import com.daymax86.linguasyne.managers.FirebaseManager
import com.daymax86.linguasyne.viewmodels.ReviseTermViewModel

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
    mailIntent: Intent,
    linkedIntent: Intent,
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

        Column(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .clickable { context.startActivity(linkedIntent) },
                horizontalArrangement = Arrangement.Start,
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(id = R.string.linkedin),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                    Icon(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.linkedin_logo),
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
                    .clickable { context.startActivity(mailIntent) },
                horizontalArrangement = Arrangement.Start,
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(id = R.string.feedback),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                    )
                    Icon(
                        Icons.Default.Email,
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

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(intrinsicSize = IntrinsicSize.Max),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = stringResource(id = R.string.app_version_text, com.daymax86.linguasyne.BuildConfig.VERSION_CODE),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
                maxLines = 1,
            )
        }

    }
}

@Composable
fun HomeDrawerContent(
    signOut: () -> Unit,
    aboutLinguaSyne: () -> Unit,
    context: Context,
    toggleTutorial: () -> Unit,
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

    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    toggleTutorial()
                },
            horizontalArrangement = Arrangement.Start,
        ) {

            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.tutorial),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                )
                Icon(
                    Icons.Default.Refresh,
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

    if (FirebaseManager.currentUser?.id == com.daymax86.linguasyne.HiddenData.ADMIN_USER_ID) {
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
    if (FirebaseManager.currentUser?.id == com.daymax86.linguasyne.HiddenData.ADMIN_USER_ID) {
        CSVManager.importVocabCSV(context)
    }
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
