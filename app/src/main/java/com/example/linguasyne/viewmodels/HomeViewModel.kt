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
import com.example.linguasyne.classes.NewsItem
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.managers.*
import com.example.linguasyne.ui.theme.LsTeal200
import com.example.linguasyne.ui.theme.LsVocabTextBlue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(
    private val navController: NavHostController,
) : ViewModel() {

    var user: User by mutableStateOf(FirebaseManager.currentUser!!)
    var userImage: Uri? by mutableStateOf(FirebaseManager.currentUser!!.imageUri)

    //var newsItems: List<NewsItem.Data> by mutableStateOf(emptyList())
    val selectedNewsColour: Color = LsVocabTextBlue
    val unselectedNewsColour: Color = LsTeal200

    fun init() {
        FirebaseManager.loadVocabFromFirebase()
        loadUserImage()
        //apiCall()
    }

    fun onBackPressed(){
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
                    .apply {
                        //Successfully obtained user image uri from firebase
                        FirebaseManager.currentUser!!.imageUri =
                            Uri.parse(this.get("imageUri") as String?)
                    }
                userImage = FirebaseManager.currentUser!!.imageUri
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


    private fun createLesson() {
        viewModelScope.launch {
            LessonManager.createLesson()
            /*launchVocabLesson =
                false //Make sure to set this back to false in case the user starts a new lesson without restarting the home activity*/
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createSession() {
        RevisionSessionManager.createSession()
        /*launchRevisionSession =
            false //Set back to false so the user can launch another session without home activity restart*/
    }

    fun handleHelpClick() {
        //Do something
    }

    fun handleVocabLessonClick() {
        createLesson()
        navController.navigate(ComposableDestinations.TERM_DISPLAY)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleRevisionClick() {
        createSession()
        navController.navigate(ComposableDestinations.REVISE)
    }

    fun handleTermBaseClick() {
        navController.navigate(ComposableDestinations.TERM_SEARCH)
    }

    fun signOut() {
        FirebaseManager.signOutUser()
        navController.navigate(ComposableDestinations.LOGIN)
    }


    private fun apiCall() {
    /*    val apiCall = APIManager.create()
        apiCall.getNewsItems().enqueue(object : Callback<NewsResponse> {

            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                newsItems = response.body()!!.data
                Log.d("HomeViewModel", "${newsItems.size}")

                if (response.isSuccessful) {
                    Log.d("HomeViewModel", "Successful response from API get")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("HomeViewModel", "ONFAILURE RESPONSE FROM API CALL: ${t.message}")
            }

        }
        )*/

    }



}
