package com.example.linguasyne.viewmodels


import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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
) : BaseViewModel() {

    var user: User by mutableStateOf(FirebaseManager.currentUser!!)
    var userImage: Uri? by mutableStateOf(FirebaseManager.currentUser!!.imageUri)

    val activeIndicatorColour: Color = LsDarkPurple
    val inactiveIndicatorColour: Color = LsGrey

    init {
        FirebaseManager.loadVocabFromFirebase()
        loadUserImage()
    }

/*    override fun drawerContent() {

    }*/

    fun onBackPressed() {
        //Back button disabled on home screen
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
                    .let {doc ->
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

    fun signOut() {
        FirebaseManager.signOutUser()
        navController.navigate(ComposableDestinations.LOGIN)
    }

}




