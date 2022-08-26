package com.example.linguasyne.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguasyne.managers.FirebaseManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var userEmailInput: String by mutableStateOf("")
    var userPasswordInput: String by mutableStateOf("")

    var outlineColour by mutableStateOf(Color(0xFF0016E0))

    var goToCreateAccount: Boolean by mutableStateOf(false)
    var goToHome: Boolean by mutableStateOf(false)

    fun init() {
        if (FirebaseManager.logInUser()) {
            goToHome = true
        }
    }

    fun handleEmailChange(text: String) {
        userEmailInput = text
    }

    fun handlePasswordChange(text: String) {
        userPasswordInput = text
    }

    fun handleButtonPress() {
        // TODO() implement coroutine
        /*viewModelScope.launch {
            // Some async FB call
            // But treat it as synchronous
        }*/
        FirebaseManager.logInUser(userEmailInput, userPasswordInput, onSuccess = {
            outlineColour = Color(0xFF00FF00)
            goToHome = true
        }, onFailure = {
            outlineColour = Color(0xFFFF0000)
        })
        //Successfully logged in user so can go to home activity!
        //TODO() Make this colour a reference rather than a hardcoded hex value

    }

    fun handleTextPress(int: Int) {
        goToCreateAccount = true
    }

}

