package com.example.linguasyne.viewmodels

import android.net.Uri
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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.linguasyne.classes.User
import com.example.linguasyne.enums.AnimationLengths
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsErrorRed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val navController: NavHostController
) : ViewModel() {

    var userEmailInput: String by mutableStateOf("")
    var userPasswordInput: String by mutableStateOf("")

    var outlineColour by mutableStateOf(Color(0xFF0016E0))

    var animateSuccess: Boolean by mutableStateOf(false)
    var animateDuration: Long by mutableStateOf(AnimationLengths.ANIMATION_DURATION_LONG)
    var blurAmount: Int by mutableStateOf(0)

    fun handleLogin() {
        viewModelScope.launch {
            try {
                Firebase.auth
                    .signInWithEmailAndPassword(userEmailInput, userPasswordInput)
                    .await()
                    .apply {
                        val user = User(userEmailInput)
                        FirebaseManager.currentUser = user
                        //Feedback to user that login was successful
                        animateSuccess = true
                        blurAmount = 5
                        delay(2500)
                        //loadUserImage()
                        goToHome()
                    }
                //If the user changes the email input between sign in and now it will likely crash!

            } catch (e: Exception) {
                Log.e("LoginViewModel", "$e")
                //Feedback to user that login failed
                outlineColour = LsErrorRed
            }
        }
    }

    /*private fun loadUserImage() {
        viewModelScope.launch {
            try {
                val firebaseUser = FirebaseManager.currentUser
                Log.e("LoginViewModel", firebaseUser!!.email)
                val firestoreRef =
                    Firebase.firestore.collection("users").document(firebaseUser.email)
                firestoreRef
                    .get()
                    .await()
                    .apply {
                        //Successfully obtained user image uri from firebase
                        FirebaseManager.currentUser!!.imageUri =
                            Uri.parse(this.get("user_image_uri") as String?)
                    }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Image exception: $e")
            }
        }
    }*/


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

