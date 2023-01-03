package com.example.linguasyne.activities

import android.content.Intent
import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linguasyne.R
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.screens.*
import com.example.linguasyne.ui.elements.HomeDrawerContent
import com.example.linguasyne.ui.elements.MainDrawerContent
import com.example.linguasyne.ui.elements.ReviseDrawerContent
import com.example.linguasyne.ui.elements.DefaultTopAppBar
import com.example.linguasyne.viewmodels.BaseViewModel
import com.example.linguasyne.viewmodels.ReviseTermViewModel
import com.example.linguasyne.viewmodels.StartViewModel
import com.example.linguasyne.viewmodels.VocabSearchViewModel

class StartActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uriHandler = LocalUriHandler.current
            val navController = rememberNavController()
            val viewModel = remember { StartViewModel(navController) }
            var baseViewModel: BaseViewModel? = null
            var drawerContent: @Composable () -> Unit by remember { mutableStateOf({}) }
            var topBarStringResource: Int by remember { mutableStateOf(R.string.app_name) }
            var onClickHelp: () -> Unit by remember { mutableStateOf({}) }
            val mediaPlayerCorrect = MediaPlayer.create(this.baseContext, R.raw.answer_correct_sound)
            val mediaPlayerWrong = MediaPlayer.create(this.baseContext, R.raw.answer_wrong_sound)

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)

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
                            shareIntent,
                        )
                    },

                    topBar = {
                        DefaultTopAppBar(
                            scope = scope,
                            scaffoldState = scaffoldState,
                            titleResourceId = topBarStringResource,
                            onHelpClick = {
                                //navController.navigate(ComposableDestinations.HELP)
                                //baseViewModel?.showHelp()
                            },
                        )
                    },

                    content = { padding ->
                        Surface(
                            modifier = Modifier
                                .padding(padding)
                                .background(MaterialTheme.colors.background)
                                .fillMaxHeight()
                        ) {
                            Column() {
                                if (baseViewModel?.helpText?.isNotEmpty() == true) {
                                    Text(
                                        baseViewModel?.helpText ?: ""
                                    )
                                }

                                NavHost(
                                    navController = navController,
                                    startDestination = if (viewModel.loginCheck())
                                        ComposableDestinations.HOME else ComposableDestinations.LOGIN,
                                ) {
                                    composable(ComposableDestinations.HOME) {
                                        drawerContent = { HomeDrawerContent(viewModel::signOut,
                                            { viewModel.aboutLinguaSyne(uriHandler) },
                                            this@StartActivity.applicationContext) }
                                        topBarStringResource = R.string.app_name
                                        HomeScreen(navController, { onClickHelp() })
                                    }
                                    composable(ComposableDestinations.TERM_SEARCH) {
                                        baseViewModel = VocabSearchViewModel(navController)
                                        topBarStringResource = R.string.term_base
                                        SearchScreen(baseViewModel as VocabSearchViewModel)
                                    }
                                    composable(ComposableDestinations.CREATE_ACCOUNT) {
                                        topBarStringResource = R.string.create_account
                                        CreateAccountScreen(navController)
                                    }
                                    composable(ComposableDestinations.LOGIN) {
                                        drawerContent = { LoginDrawerContent() }
                                        topBarStringResource = R.string.log_in
                                        LoginScreen(navController)
                                    }
                                    composable(ComposableDestinations.TERM_DISPLAY) {
                                        VocabDisplayScreen(navController)
                                        topBarStringResource = if (LessonManager.activeLesson) {
                                            R.string.lesson
                                        } else {
                                            R.string.term_base
                                        }
                                    }
                                    composable(ComposableDestinations.REVISE) {
                                        val vm = remember { ReviseTermViewModel(navController, mediaPlayerCorrect, mediaPlayerWrong) }
                                        drawerContent = { ReviseDrawerContent(vm) }
                                        topBarStringResource = R.string.revision
                                        ReviseTermScreen(
                                            viewModel = vm
                                        )
                                    }
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}



