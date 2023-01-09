package com.example.linguasyne.managers

import android.net.Uri
import android.util.Log
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

object FirebaseManager {

    var currentUser: User? = User("")

    fun loadVocabFromFirebase() {
        val ref = FirebaseFirestore.getInstance()
        ref.collection("vocab")
            .get()
            .addOnSuccessListener { documents ->
                val results = mutableListOf<Vocab>()
                for (document in documents) {
                    val vocabTranslations = document.get("translations")
                    val vocabMnemonics = document.get("mnemonics")
                    val vocabGender = document.get("gender")
                    val vocabTypes = document.get("types")
                    val vocab = Vocab(
                        document.getField<String>("id").toString(),
                        document.getField<String>("name").toString(),
                        document.getField<Int>("unlockLevel") ?: 0,
                        vocabTranslations as List<String>,
                        vocabMnemonics as List<String>,
                        vocabGender as String,
                        vocabTypes as List<String>
                    )
                    results.add(vocab)
                }
                VocabRepository.currentVocab = results
                VocabRepository.allVocab = results.sortedBy {
                    it.name
                }
            }
            .addOnFailureListener { exception ->
                Log.e("VocabSearchActivity", "Error getting documents: ", exception)
            }
    }

    fun addVocabToFirebase(term: Vocab) { //For uploading vocab data from CSV
        FirebaseFirestore.getInstance()
            .collection("vocab")
            .add(term)
            .addOnSuccessListener {
                Log.d("StartActivity", "Vocab item #" + term.id + " added to firebase")
            }
    }

    suspend fun getUserVocabUnlocks(): List<Vocab> {
        var fetchedTerms: List<Vocab>
        coroutineScope {
            fetchedTerms = FirebaseFirestore.getInstance()
                .collection("users")
                .document("${currentUser?.email}")
                .collection("terms")
                .get()
                .await()
                .toObjects(Vocab::class.java)
        }
        return fetchedTerms
    }

    fun getDefaultUserImageUri(): Uri { //Provide a default profile picture, stored on firestore
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/linguasyne.appspot.com/o/default%2Fdefault_profile_image.png?alt=media&token=92568d94-2835-4648-8128-672e998fe3de")
    }

    suspend fun getUserDarkModePreference(): Boolean { //User's choice stored in firebase
        var darkMode: Boolean
        try {
            coroutineScope {
                darkMode = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document("${currentUser?.email}")
                    .get().await().get("darkModeEnabled") as Boolean
            }.apply {
                return darkMode
            }
        } catch (e: Exception) {
            Log.e("FirebaseManager", "Couldn't fetch darkmode preference: $e")
            return false //Return default in case user setting can't be accessed on Firebase
        }
    }

    suspend fun updateUserDarkModeChoice() { //Change the user's darkmode preference on firebase
        coroutineScope {
            Firebase.firestore.collection("users").document(currentUser!!.email)
                .update("darkModeEnabled", currentUser!!.darkModeEnabled)
        }.addOnSuccessListener {
            Log.d(
                "FirebaseManager",
                "User dark mode preference set to ${currentUser!!.darkModeEnabled}"
            )
        }
    }

    fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        currentUser = null
        Log.d("StartActivity", "User signed out")
    }

    suspend fun checkReviewsDue(): Int { //Find out how many terms the user has ready to review
        var termsDue = 0
        coroutineScope {
            getUserVocabUnlocks().onEach { vocab ->
                if (vocab.reviewDue()) {
                    termsDue++
                }
            }
        }
        return termsDue
    }

    suspend fun checkLessonsDue(): Int { //Find out how many lessons the user has available
        var termsDue: Int
        coroutineScope {
            var userUnlocks: List<Vocab> = emptyList()
            getUserVocabUnlocks().apply {
                userUnlocks = this
                VocabRepository.filterByUnlockLevel(currentUser!!.level).apply {
                    termsDue =
                        VocabRepository.currentVocab.size - userUnlocks.size
                    Log.d("FirebaseManager", "returning termsDue LESSONS (value = $termsDue)")
                }
            }
        }
        return termsDue
    }

    suspend fun increaseUserLevel() { // on firebase
        coroutineScope {
            Firebase.firestore.collection("users").document(currentUser!!.email)
                .update("level", currentUser!!.level)
        }.addOnSuccessListener {
            Log.d("FirebaseManager", "User level increased on Firebase successfully")
        }
    }

}
