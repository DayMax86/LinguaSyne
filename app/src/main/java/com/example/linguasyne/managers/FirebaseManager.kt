package com.example.linguasyne.managers

import android.net.Uri
import android.util.Log
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

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
                        document.getField<Int>("unlock_level")!!.toInt(),
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

    fun addVocabToFirebase(term: Term) {
        FirebaseFirestore.getInstance()
            .collection("vocab")
            .add(term)
            .addOnSuccessListener {
                Log.d("HomeActivity", "Vocab item #" + term.id + " added to firebase")
            }
    }


    fun getDefaultUserImageUri(): Uri {
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/linguasyne.appspot.com/o/default%2FAngrySteveEmote.png?alt=media&token=b3ba69f5-0ba8-41f4-9449-b58327b7f4d0")
    }

    fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        currentUser = null
        Log.d("HomeActivity", "User signed out")
        Log.d("HomeActivity", "Current user id: ${FirebaseAuth.getInstance().currentUser?.uid}")
    }

}