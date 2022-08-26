package com.example.linguasyne.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsErrorRed
import com.example.linguasyne.ui.theme.LsTextBlue

class CreateAccountViewModel : ViewModel() {

    var userEmailInput: String by mutableStateOf("")
    var userPasswordInput: String by mutableStateOf("")

    var emailOutlineColour by mutableStateOf(Color(0x3F0F0F0F))
    var passwordOutlineColour by mutableStateOf(Color(0x3F0F0F0F))

    var passwordStrength: String by mutableStateOf("")
    var progressBarValue: Float by mutableStateOf(0f)

    var showProgressBar: Boolean by mutableStateOf(false)
    var returnToLogin: Boolean by mutableStateOf(false)

    var userImage: Uri? by mutableStateOf(FirebaseManager.getDefaultUserImageUri())

    fun handleEmailChange(text: String) {
        emailOutlineColour = LsTextBlue
        userEmailInput = text
    }

    fun handlePasswordChange(text: String) {
        passwordOutlineColour = LsTextBlue
        userPasswordInput = text

        if (userPasswordInput == "") { //User hasn't entered a password so hide the strength indicator
            showProgressBar = false
        } else { // User has entered a password so the progressbar etc. should be displayed

            showProgressBar = true

            when (checkPasswordStrength(userPasswordInput)) {
                PasswordStrengths.WHITESPACE -> {
                    passwordStrength = "Password cannot contain spaces!"
                    progressBarValue = 0f
                    passwordOutlineColour = LsErrorRed
                }
                PasswordStrengths.VERY_WEAK -> {
                    passwordStrength = "Very weak"
                    progressBarValue = 0.2f
                    passwordOutlineColour = Color(0x2200FF00)
                }
                PasswordStrengths.WEAK -> {
                    passwordStrength = "Weak"
                    progressBarValue = 0.4f
                    passwordOutlineColour = Color(0x4400FF00)
                }
                PasswordStrengths.AVERAGE -> {
                    passwordStrength = "Average"
                    progressBarValue = 0.6f
                    passwordOutlineColour = Color(0x6600FF00)
                }
                PasswordStrengths.STRONG -> {
                    passwordStrength = "Strong"
                    progressBarValue = 0.8f
                    passwordOutlineColour = Color(0x8800FF00)
                }
                PasswordStrengths.VERY_STRONG -> {
                    passwordOutlineColour = LsCorrectGreen
                    passwordStrength = "Very strong!"
                    progressBarValue = 1f
                }
                PasswordStrengths.ERROR -> {
                    Log.e("CreateAccountActivity", "Error in displaying password strength")
                }
                PasswordStrengths.SHORT -> {
                    passwordStrength = "Too short"
                    progressBarValue = 0f
                    passwordOutlineColour = LsErrorRed
                }
            }
        }
    }

    fun handleButtonPress() {
        FirebaseManager.createNewAccount(
            userEmailInput,
            userPasswordInput,
            { accountCreateFailure() })
        { accountCreated() }

    }

    private fun accountCreated() {
        FirebaseManager.uploadUserImageToFirebaseStorage(userImage) { firebaseImageUpload(userImage) }
    }

    private fun accountCreateFailure() {
        emailOutlineColour = LsErrorRed
    }

    fun handleTextPress() {
        returnToLogin = true
    }

    fun firebaseImageUpload(uri: Uri?) {
        userImage = uri
        Log.d("CreateAccountViewModel", "Firebase image upload complete")
    }

    private fun checkPasswordStrength(password: String): PasswordStrengths {
        var score = 0
        //Is the pw longer than 8 characters?
        if (password.length > 8) {
            //Allow password
        } else {
            //Do not allow password and alert user that it is too short.
            return PasswordStrengths.SHORT
        }

        //Does the pw include lowercase AND uppercase letters?
        //Does the pw include numbers?
        //Does the pw include other characters?
        //Does the pw have at least 5 unique characters?
        var containsLowercase = false
        var containsUppercase = false
        var containsDigit = false
        var containsSymbol = false

        for (char: Char in password) {
            if (char.isLowerCase()) {
                containsLowercase = true
            }
            if (char.isUpperCase()) {
                containsUppercase = true
            }
            if (char.isDigit()) {
                containsDigit = true
            }
            if (!char.isLetterOrDigit()) {
                containsSymbol = true
            }
        }

        if (containsLowercase && containsUppercase) {
            score += 5
        }
        if (containsSymbol || containsDigit) {
            if (containsSymbol && containsDigit) {
                score += 10
            }
            score += 10
        }
        if (containsLowercase && containsUppercase && containsDigit && containsSymbol) {
            score += 20
        }

        if (password.contains(" ")) {
            return PasswordStrengths.WHITESPACE
        }

        when (score) {
            in 0..4 -> return PasswordStrengths.VERY_WEAK
            in 5..9 -> return PasswordStrengths.WEAK
            in 10..19 -> return PasswordStrengths.AVERAGE
            in 20..24 -> return PasswordStrengths.STRONG
            in 25..100 -> return PasswordStrengths.VERY_STRONG
            else -> return PasswordStrengths.ERROR
        }

    }

    private enum class PasswordStrengths {
        VERY_WEAK,
        WEAK,
        AVERAGE,
        STRONG,
        VERY_STRONG,
        ERROR,
        SHORT,
        WHITESPACE
    }

}