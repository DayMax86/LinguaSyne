package com.example.linguasyne.ui.elements

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SharedTopAppBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    androidx.compose.material.TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
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
                contentDescription = ""
            )
        }
    )

}