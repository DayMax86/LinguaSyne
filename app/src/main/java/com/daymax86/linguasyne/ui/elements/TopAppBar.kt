package com.daymax86.linguasyne.ui.elements

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daymax86.linguasyne.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DefaultTopAppBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    titleResourceId: Int,
    onHelpClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = titleResourceId),
                color = MaterialTheme.colors.onSurface
            )
        },
        backgroundColor = MaterialTheme.colors.secondary,
        navigationIcon = {
            Icon(
                Icons.Default.Menu,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable(
                        onClick = {
                            scope.launch {
                                if (scaffoldState.drawerState.isClosed) {
                                    scaffoldState.drawerState.open()
                                } else {
                                    scaffoldState.drawerState.close()

                                }
                            }
                            Log.d(
                                "StartActivity",
                                "Is closed = ${scaffoldState.drawerState.isClosed}"
                            )
                        }
                    ),
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
            )
        },
        actions = {
            /*TopAppBarActionButton(
                imageVector = Icons.Default.Info,
                description = "Help",
            ) { onHelpClick() }*/
        }
    )

}

@Composable
fun TopAppBarActionButton(
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(imageVector = imageVector, contentDescription = description)
    }
}

