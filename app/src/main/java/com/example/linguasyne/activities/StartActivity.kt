package com.example.linguasyne.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.screens.*
import com.example.linguasyne.ui.elements.MainDrawerContent
import com.example.linguasyne.ui.elements.SharedTopAppBar
import com.example.linguasyne.viewmodels.StartViewModel

class StartActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val viewModel = remember { StartViewModel(navController) }
            var drawerContent: @Composable () -> Unit by remember { mutableStateOf({}) }

            val scope = rememberCoroutineScope()
            val scaffoldState =
                rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

            //TODO() Change to match system theme once colours have been set before release
            LinguaSyneTheme(darkTheme = false) {

                //Global, shared features (e.g. TopAppBar, Drawer)
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        MainDrawerContent(
                            screenContent = drawerContent,
                        )
                        /*MainDrawerContent(
                            viewModel::handleTermBaseClick,
                            viewModel::signOut,
                        )*/
                    },

                    topBar = {
                        SharedTopAppBar(
                            scope = scope,
                            scaffoldState = scaffoldState,
                        )
                    },

                    content = { padding ->
                        Surface(
                            modifier = Modifier
                                .padding(padding)
                                .background(MaterialTheme.colors.background)
                                .fillMaxHeight()
                        ) {

                            NavHost(
                                navController = navController,
                                startDestination = if (viewModel.loginCheck())
                                    ComposableDestinations.HOME else ComposableDestinations.LOGIN,
                            ) {
                                composable(ComposableDestinations.HOME) {
                                    drawerContent = { HomeDrawerContent(viewModel::signOut) }
                                    HomeScreen(navController)
                                }
                                composable(ComposableDestinations.TERM_SEARCH) {
                                    SearchScreen(navController)
                                }
                                composable(ComposableDestinations.CREATE_ACCOUNT) {
                                    CreateAccountScreen(navController)
                                }
                                composable(ComposableDestinations.LOGIN) {
                                    drawerContent = { LoginDrawerContent() }
                                    LoginScreen(navController)
                                }
                                composable(ComposableDestinations.TERM_DISPLAY) {
                                    VocabDisplayScreen(navController)
                                }
                                composable(ComposableDestinations.REVISE) {
                                    drawerContent = { ReviseDrawerContent() }
                                    ReviseTermScreen(navController)
                                }
                                composable(ComposableDestinations.SUMMARY) {
                                    RevisionSummaryScreen(navController)
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}
