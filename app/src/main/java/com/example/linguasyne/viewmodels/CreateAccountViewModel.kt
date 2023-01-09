package com.example.linguasyne.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.linguasyne.R
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsErrorRed
import com.example.linguasyne.ui.theme.LsLightPrimary
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.linguasyne.classes.User
import com.example.linguasyne.enums.AnimationLengths
import com.example.linguasyne.enums.ComposableDestinations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class CreateAccountViewModel(
    private val navController: NavHostController
) : ViewModel() {

    var userEmailInput: String by mutableStateOf("")
    var userPasswordInput: String by mutableStateOf("")

    var emailOutlineColour by mutableStateOf(Color(0x3F0F0F0F))
    var passwordOutlineColour by mutableStateOf(Color(0x3F0F0F0F))

    var passwordStrength: Int by mutableStateOf(0)
    var progressBarValue: Float by mutableStateOf(0f)

    var showProgressBar: Boolean by mutableStateOf(false)
    var goToHome: Boolean by mutableStateOf(false)

    var showLoadingAnim by mutableStateOf(false)
    var animateSuccess: Boolean by mutableStateOf(false)
    var animateDuration: Long by mutableStateOf(AnimationLengths.ANIMATION_DURATION_LONG)
    var blurAmount: Int by mutableStateOf(0)

    var userImage: Uri? by mutableStateOf(null)

    fun handleEmailChange(text: String) {
        emailOutlineColour = LsLightPrimary
        userEmailInput = text
    }

    fun handlePasswordChange(text: String) {
        passwordOutlineColour = LsLightPrimary
        userPasswordInput = text

        if (userPasswordInput == "") { //User hasn't entered a password so hide the strength indicator
            showProgressBar = false
        } else { // User has entered a password so the progressbar etc. should be displayed

            showProgressBar = true

            when (checkPasswordStrength(userPasswordInput)) {
                PasswordStrengths.WHITESPACE -> {
                    passwordStrength =
                        (R.string.password_strength_spaces)
                    progressBarValue = 0f
                    passwordOutlineColour = LsErrorRed
                }
                PasswordStrengths.VERY_WEAK -> {
                    passwordStrength =
                        (R.string.password_strength_very_weak)
                    progressBarValue = 0.2f
                    passwordOutlineColour = Color(0x2200FF00)
                }
                PasswordStrengths.WEAK -> {
                    passwordStrength =
                        (R.string.password_strength_weak)
                    progressBarValue = 0.4f
                    passwordOutlineColour = Color(0x4400FF00)
                }
                PasswordStrengths.AVERAGE -> {
                    passwordStrength =
                        (R.string.password_strength_average)
                    progressBarValue = 0.6f
                    passwordOutlineColour = Color(0x6600FF00)
                }
                PasswordStrengths.STRONG -> {
                    passwordStrength =
                        (R.string.password_strength_strong)
                    progressBarValue = 0.8f
                    passwordOutlineColour = Color(0x8800FF00)
                }
                PasswordStrengths.VERY_STRONG -> {
                    passwordOutlineColour = LsCorrectGreen
                    passwordStrength =
                        (R.string.password_strength_very_strong)
                    progressBarValue = 1f
                }
                PasswordStrengths.ERROR -> {
                    Log.e("CreateAccountActivity", "Error in displaying password strength")
                }
                PasswordStrengths.SHORT -> {
                    passwordStrength =
                        (R.string.password_strength_short)
                    progressBarValue = 0f
                    passwordOutlineColour = LsErrorRed
                }
            }
        }
    }

    fun handleButtonPress() {
        showProgressBar = false
        createAccount(userEmailInput, userPasswordInput)
    }


    private fun createAccount(email: String, password: String) {
        showLoadingAnim = true
        viewModelScope.launch {
            try {
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                /*------------ ADD TO FIREBASE 'AUTHENTICATION' -------------*/
                auth.createUserWithEmailAndPassword(
                    email.lowercase().filter {
                        !it.isWhitespace()
                    },
                    password
                )
                    .await()
                // Sign in success, update UI with the signed-in user's information
                val user = User(email)
                FirebaseManager.currentUser = user
                /*------------ ADD TO FIREBASE 'FIRESTORE' -------------*/
                addUserToFirestore(user)
            } catch (e: Exception) {
                //TODO() If sign in fails, feed back the reason to the user rather than just a colour
                emailOutlineColour = LsErrorRed
                passwordOutlineColour = LsErrorRed
                Log.e("CreateAccountViewModel", "$e")
            }
        }.invokeOnCompletion { showLoadingAnim = false }
    }

    private fun addUserToFirestore(user: User) {
        viewModelScope.launch {
            try {
                val firestoreInstance = FirebaseFirestore.getInstance()
                firestoreInstance
                    .collection("users").document(user.id)
                    .set(user)
                    .await()
                    .apply {
                        //this.update(FieldPath.of(this.path),"users/${user.email}")
                        uploadUserImage(userImage)
                        handleLogin()
                    }
                Log.d("CreateAccountViewModel", "User successfully added to Firebase")
            } catch (e: Exception) {
                Log.e("CreateAccountViewModel", "$e")
            }
        }
    }

    fun uploadUserImage(localUri: Uri?) {
        viewModelScope.launch {
            try {
                val user = FirebaseManager.currentUser ?: User("")
                val filename = "profileImage"
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val firestoreRef =
                    Firebase.firestore.collection("users").document(firebaseUser?.email ?: "")
                val storageRef =
                    FirebaseStorage.getInstance()
                        .getReference("/users/${user.id}/image/$filename")
                if (localUri != null) {
                    storageRef.putFile(localUri)
                        .await()
                        .apply {
                            FirebaseStorage.getInstance().reference
                                .child("users/${user.id}/image/$filename").downloadUrl
                                .await()
                                .apply {
                                    firestoreRef
                                        .update("imageUri", this)
                                        .await()
                                    userImage = this
                                }
                        }
                }

            } catch (e: Exception) {
                Log.e("CreateAccountViewModel", "$e")
            }
        }
    }

    private fun handleLogin() {
        viewModelScope.launch {
            goToHome = try {
                Firebase.auth
                    .signInWithEmailAndPassword(
                        userEmailInput.lowercase().filter { !it.isWhitespace() }, userPasswordInput
                    )
                    .await()
                //Feedback to user that login was successful
                animateSuccess = true
                blurAmount = 5
                delay(2500)
                true
            } catch (e: Exception) {
                Log.e("Login", "$e")
                false
            }
            if (goToHome) {
                navController.navigate(ComposableDestinations.HOME)
            }
        }
    }

    fun handleTextPress() {
        navController.navigate(ComposableDestinations.LOGIN)
    }

    private fun checkPasswordStrength(password: String): PasswordStrengths { //Custom password strength algorithm
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

        return when (score) {
            in 0..4 -> PasswordStrengths.VERY_WEAK
            in 5..9 -> PasswordStrengths.WEAK
            in 10..19 -> PasswordStrengths.AVERAGE
            in 20..24 -> PasswordStrengths.STRONG
            in 25..100 -> PasswordStrengths.VERY_STRONG
            else -> PasswordStrengths.ERROR
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