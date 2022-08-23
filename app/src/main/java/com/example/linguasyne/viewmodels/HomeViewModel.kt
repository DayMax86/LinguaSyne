package com.example.linguasyne.viewmodels


import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.linguasyne.R
import com.example.linguasyne.activities.HomeActivity
import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.managers.LessonTypes
import com.example.linguasyne.managers.RevisionSessionManager


class HomeViewModel : ViewModel() {

    var user: User by mutableStateOf(FirebaseManager.current_user)
    var userImage: Uri? by mutableStateOf(FirebaseManager.current_user.user_image_uri)

    var launchTermBase: Boolean by mutableStateOf(false)
    var launchVocabLesson: Boolean by mutableStateOf(false)
    var launchRevisionSession: Boolean by mutableStateOf(false)
    var launchLogin: Boolean by mutableStateOf(false)

    fun init() {
        FirebaseManager.loadVocabFromFirebase()
        if (FirebaseManager.current_user.user_image_uri == null) {
            userImage = FirebaseManager.getDefaultUserImageUri()
        } else {
            userImage = FirebaseManager.current_user.user_image_uri
        }
    }

    fun createLesson() {
        LessonManager.createLesson(LessonTypes.VOCAB)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createSession() {
        RevisionSessionManager.createSession()
    }

    fun firebaseImageUpload(uri: Uri) {
        userImage = uri
    }

    fun handleHelpClick() {
        //Do something
        //TEMP!!---------
        signOut()
        /*--------------*/
    }

    fun handleVocabLessonClick() {
        launchVocabLesson = true
    }

    fun handleRevisionClick() {
        launchRevisionSession = true
    }

    fun handleTermBaseClick() {
        launchTermBase = true
    }

    fun handleProfileImageClick() {
        //Do something
    }

    fun signOut() {
        FirebaseManager.signOutUser()
        launchLogin = true
    }


}