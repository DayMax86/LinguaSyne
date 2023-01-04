package com.example.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguasyne.managers.FirebaseManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import com.example.linguasyne.classes.User
import com.example.linguasyne.enums.AnimationLengths
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.ui.theme.LsErrorRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val navController: NavHostController
) : ViewModel() {

    var userEmailInput: String by mutableStateOf("")
    var userPasswordInput: String by mutableStateOf("")

    var outlineColour by mutableStateOf(Color(0xFF9466ff))

    var showLoadingAnim by mutableStateOf(false)

    var animateSuccess: Boolean by mutableStateOf(false)
    var animateDuration: Long by mutableStateOf(AnimationLengths.ANIMATION_DURATION_DEFAULT)
    var blurAmount: Int by mutableStateOf(0)

    fun handleLogin() {
        viewModelScope.launch {
            showLoadingAnim = true
            try {
                Firebase.auth
                    .signInWithEmailAndPassword(userEmailInput, userPasswordInput)
                    .await()
                    .apply {
                        val user = User(userEmailInput)
                        FirebaseManager.currentUser = user
                        //Feedback to user that login was successful
                        showLoadingAnim = false
                        animateSuccess = true
                        blurAmount = 5
                        delay(2500)
                        //loadUserImage()
                        goToHome()
                    }
                //If the user changes the email input between sign in and now it will likely crash! //TODO()

            } catch (e: Exception) {
                Log.e("LoginViewModel", "$e")
                //Feedback to user that login failed
                    outlineColour = LsErrorRed
                showLoadingAnim = false
            }
        }
        showLoadingAnim = false
    }

    fun handleEmailChange(text: String) {
        userEmailInput = text
    }

    fun handlePasswordChange(text: String) {
        userPasswordInput = text
    }

    fun handleTextPress(int: Int) {
        navController.navigate(ComposableDestinations.CREATE_ACCOUNT)
    }

    private fun goToHome() {
        navController.navigate(ComposableDestinations.HOME)
    }
}

