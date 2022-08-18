package com.example.linguasyne.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.linguasyne.managers.FirebaseManager

class LoginViewModel {

    var userEmailInput: String by mutableStateOf("")
    var userPasswordInput: String by mutableStateOf("")

    var outlineColour by mutableStateOf(Color(0x3F0F0F0F))

    var goToCreateAccount: Boolean by mutableStateOf(false)
    var goToHome: Boolean by mutableStateOf(false)

    fun handleEmailChange(text: String) {
        userEmailInput = text
    }

    fun handlePasswordChange(text: String) {
        userPasswordInput = text
    }

    fun handleButtonPress() {
        if (FirebaseManager.logInUser(userEmailInput, userPasswordInput)) {
            //Successfully logged in user so can go to home activity!
            //TODO() Make this colour a reference rather than a hardcoded hex value
            outlineColour = Color(0xFF00FF00)
            goToHome = true
        } else {
            //User login to Firebase unsuccessful
            //TODO() Make this colour a reference rather than a hardcoded hex value
            outlineColour = Color(0xFFFF0000)
        }
    }

    fun handleTextPress(int: Int) {
        goToCreateAccount = true
    }

}

