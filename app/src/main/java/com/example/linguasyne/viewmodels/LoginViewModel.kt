package com.example.linguasyne.viewmodels

import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.linguasyne.FirebaseManager
import com.example.linguasyne.R
import com.example.linguasyne.User
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel {

    var userEmailInput: String by mutableStateOf("")
    var userPasswordInput: String by mutableStateOf("")

    var outlineColour by mutableStateOf(Color(0x3F0F0F0F))

    var goToCreateAccount: Boolean by mutableStateOf(false)

    fun handleEmailChange(text: String) {
        userEmailInput = text
    }

    fun handlePasswordChange(text: String) {
        userPasswordInput = text
    }

    fun handleButtonPress() {
        if (FirebaseManager.logInUser(userEmailInput, userPasswordInput)) {
            //Successfully logged in user so can go to home activity!
        } else {
            //User login to Firebase unsuccessful
        }
    }

    fun handleTextPress(int: Int) {
        goToCreateAccount = true
    }

}

