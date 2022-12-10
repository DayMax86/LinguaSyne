package com.example.linguasyne.managers

import android.net.Uri
import android.util.Log
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.ReviewTimes
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
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

    fun addVocabToFirebase(term: Vocab) {
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

    fun getDefaultUserImageUri(): Uri {
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/linguasyne.appspot.com/o/default%2Fdefault_profile_image.png?alt=media&token=92568d94-2835-4648-8128-672e998fe3de")
    }

    fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        currentUser = null
        Log.d("StartActivity", "User signed out")
        Log.d("StartActivity", "Current user id: ${FirebaseAuth.getInstance().currentUser?.uid}")
    }

    fun checkReviewsDue(): Int {
        var fetchedTerms: List<Vocab> = emptyList()
        var termsDue = 0
        FirebaseFirestore.getInstance()
            .collection("users")
            .document("${currentUser?.email}")
            .collection("terms")
            .get()
            .addOnSuccessListener {
                fetchedTerms = it.toObjects(Vocab::class.java)
                fetchedTerms.forEach { vocab ->
                    if (vocab.nextReviewTime.compareTo(Timestamp.now()) <= 0) {
                        vocab.reviewDue = true
                        termsDue++
                    }
                }
            }
        return termsDue
    }

    suspend fun checkLessonsDue(): Int {
        var termsDue = 0
        var fetchedTerms: List<Vocab> = emptyList()
        getUserVocabUnlocks().apply {
            val userUnlocks: List<Vocab> = this
            FirebaseFirestore.getInstance()
                .collection("vocab")
                .get()
                .addOnSuccessListener {
                    Log.d("FirebaseManager", "vocab fetched successfully")
                    fetchedTerms = it.toObjects(Vocab::class.java)
                    fetchedTerms.forEach { vocab ->
                        if (vocab.unlockLevel <= currentUser!!.level) {
                            Log.d("FirebaseManager", "vocab unlock level <= user level")
                            userUnlocks.forEach { unlockedTerm ->
                                Log.d("FirebaseManager", "vocab id=${vocab.id}, unlockedTerm id=${unlockedTerm.id}")
                                if (vocab.id == unlockedTerm.id) {
                                    termsDue++
                                }
                            }
                        }
                    }
                    Log.d("FirebaseManager", "terms due: ${termsDue}")
                }
                .addOnFailureListener {
                    Log.e("FirebaseManager", "$it")
                }
        }
        return termsDue
    }

}
