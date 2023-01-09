package com.daymax86.linguasyne.viewmodels


import android.app.Activity
import android.content.Context
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
import com.daymax86.linguasyne.activities.StartActivity
import com.daymax86.linguasyne.classes.User
import com.daymax86.linguasyne.enums.ComposableDestinations
import com.daymax86.linguasyne.managers.*
import com.daymax86.linguasyne.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeViewModel(
    private val navController: NavHostController,
    finishActivity: () -> Unit,
) : ViewModel() {

    val closeApp: () -> Unit = finishActivity

    var user: User by mutableStateOf(FirebaseManager.currentUser!!)
    var userImage: Uri? by mutableStateOf(FirebaseManager.currentUser!!.imageUri)

    var reviewsDue: Int by mutableStateOf(0)
    var reviewsClickable: Boolean by mutableStateOf(false)
    var lessonsDue: Int by mutableStateOf(0)
    var lessonsClickable: Boolean by mutableStateOf(false)

    val activeIndicatorColour: Color = LsLightPrimaryVariant
    val inactiveIndicatorColour: Color = LsLightOnPrimary

    var displayTutorial by mutableStateOf(false)
    var blurAmount by mutableStateOf(0)

    init {
        viewModelScope.launch {
            FirebaseManager.loadVocabFromFirebase()
            getReviewsAndLessonsDue()
            user.levelUpCheck(FirebaseManager.getUserVocabUnlocks())
            displayTutorial = (FirebaseManager.getUserVocabUnlocks().isEmpty())
            if (displayTutorial) {blurAmount = 5}
        }
        loadUserImage()
    }

    fun toggleTutorial() {
        displayTutorial = !displayTutorial
        blurAmount = if (displayTutorial) {
            5
        } else {
            0
        }
    }

    private suspend fun getReviewsAndLessonsDue() {
        reviewsDue = FirebaseManager.checkReviewsDue()
        lessonsDue = FirebaseManager.checkLessonsDue()
        reviewsClickable = reviewsDue > 0
        lessonsClickable = lessonsDue > 0
    }

    fun onBackPressed() {
        closeApp()
    }

    private fun loadUserImage() {
        viewModelScope.launch {
            try {
                val firebaseUser = FirebaseManager.currentUser
                val firestoreRef =
                    Firebase.firestore.collection("users").document(firebaseUser!!.email)
                firestoreRef
                    .get()
                    .await()
                    .let { doc ->
                        //Successfully obtained user image uri from firebase
                        FirebaseManager.currentUser!!.imageUri =
                            Uri.parse(doc.get("imageUri") as String?)
                        userImage = FirebaseManager.currentUser!!.imageUri
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
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val firestoreRef =
                    Firebase.firestore.collection("users").document(firebaseUser?.email!!)
                val storageRef =
                    FirebaseStorage.getInstance()
                        .getReference("/users/${FirebaseManager.currentUser!!.id}/image/$filename")
                if (localUri != null) {
                    storageRef.putFile(localUri)
                        .await()
                        .apply {
                            FirebaseStorage.getInstance().reference
                                .child("users/${FirebaseManager.currentUser!!.id}/image/$filename").downloadUrl
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
        //TODO(Not yet implemented)
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




