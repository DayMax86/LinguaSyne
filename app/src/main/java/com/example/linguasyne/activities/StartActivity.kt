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
import androidx.compose.runtime.rememberCoroutineScope
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
            val viewModel = StartViewModel(navController)

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
                            //TODO() How to customise the contents without having to pass the parameters?
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
                                startDestination = ComposableDestinations.LOADING,
                            ) {
                                composable(ComposableDestinations.HOME) {
                                    HomeScreen(navController)
                                }
                                composable(ComposableDestinations.TERM_SEARCH) {
                                    SearchScreen(navController)
                                }
                                composable(ComposableDestinations.CREATE_ACCOUNT) {
                                    CreateAccountScreen(navController)
                                }
                                composable(ComposableDestinations.LOGIN) {
                                    LoginScreen(navController)
                                }
                                composable(ComposableDestinations.TERM_DISPLAY) {
                                    VocabDisplayScreen(navController)
                                }
                                composable(ComposableDestinations.REVISE) {
                                    ReviseTermScreen(navController)
                                }
                                composable(ComposableDestinations.SUMMARY) {
                                    RevisionSummaryScreen(navController)
                                }
                                composable(ComposableDestinations.LOADING) {
                                    Animate(viewModel.animateLoading)
                                    viewModel.init()

                                }
                            }


                        }

                    },

                    )
            }


        }
    }

}
