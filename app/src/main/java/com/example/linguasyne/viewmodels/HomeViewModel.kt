package com.example.linguasyne.viewmodels


import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.linguasyne.classes.NewsItem
import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel() {

    var user: User by mutableStateOf(FirebaseManager.current_user)
    var userImage: Uri? by mutableStateOf(FirebaseManager.current_user.user_image_uri)

    var launchTermBase: Boolean by mutableStateOf(false)
    var launchVocabLesson: Boolean by mutableStateOf(false)
    var launchRevisionSession: Boolean by mutableStateOf(false)
    var launchLogin: Boolean by mutableStateOf(false)

    var newsItems: List<NewsItem.Data> by mutableStateOf(emptyList())

    fun init(uriFetch: () -> Unit) {
        FirebaseManager.loadVocabFromFirebase()
        FirebaseManager.getUserImageFromFirestore { uriFetch() }
    }

    fun imageFetched(uri: Uri?) {
        userImage = uri
        Log.d("HomeViewModel", "Image fetched from firestore: $userImage")
    }

    fun createLesson() {
        LessonManager.createLesson(LessonTypes.VOCAB)
        launchVocabLesson = false //Make sure to set this back to false in case the user starts a new lesson without restarting the home activity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createSession() {
        RevisionSessionManager.createSession()
        launchRevisionSession = false //Set back to false so the user can launch another session without home activity restart
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


    fun APIcall(onSuccess: ()-> Unit) {

        val apiCall = APIManager.create()
        apiCall.getNewsItems().enqueue(object : Callback<NewsResponse> {

            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                newsItems = response.body()!!.data
                Log.d("HomeViewModel","${newsItems.size}")

                if (response.isSuccessful){
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("HomeViewModel", "ONFAILURE RESPONSE FROM API CALL: ${t.message}")
            }

        }
        )

    }

}