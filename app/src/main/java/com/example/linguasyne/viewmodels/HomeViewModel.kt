package com.example.linguasyne.viewmodels


import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.linguasyne.classes.User
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.managers.*
import com.example.linguasyne.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeViewModel(
    private val navController: NavHostController,
) : ViewModel() {

    var user: User by mutableStateOf(FirebaseManager.currentUser?: User(""))
    var userImage: Uri? by mutableStateOf(FirebaseManager.currentUser?.imageUri ?: FirebaseManager.getDefaultUserImageUri())

    val activeIndicatorColour: Color = LsDarkPurple
    val inactiveIndicatorColour: Color = LsGrey

    init {
        FirebaseManager.loadVocabFromFirebase()
        loadUserImage()
    }

    fun onBackPressed() {
        //Back button disabled on home screen
    }

    private fun loadUserImage() {
        viewModelScope.launch {
            try {
                val firestoreRef =
                    Firebase.firestore.collection("users").document(user.email)

                firestoreRef
                    .get()
                    .await()
                    .let {doc ->
                        //Successfully obtained user image uri from firebase
                        this@HomeViewModel.user.imageUri =
                            Uri.parse(doc.get("imageUri") as String?)
                        userImage = this@HomeViewModel.user.imageUri
                    }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Image exception: $e")
                userImage = FirebaseManager.getDefaultUserImageUri()
            }
        }
    }

    fun uploadUserImage(localUri: Uri?) {
        viewModelScope.launch {
            try {
                val filename = "profileImage"
                val firestoreRef =
                    Firebase.firestore.collection("users").document(user.email)
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
                Log.e("HomeViewModel", "$e")
            }
        }
    }

    fun handleHelpClick() {
        //Do something
    }

    fun handleVocabLessonClick() {
        viewModelScope
            .launch {
                LessonManager.createLesson { navController.navigate(ComposableDestinations.TERM_DISPLAY) }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleRevisionClick() {
        navController.navigate(ComposableDestinations.REVISE)
    }

    fun handleTermBaseClick() {
        navController.navigate(ComposableDestinations.TERM_SEARCH)
    }


}




