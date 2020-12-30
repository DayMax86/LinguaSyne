package com.example.linguasyne

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField

object FirebaseManager {

    fun loadVocabFromFirebase() {
        val ref = FirebaseFirestore.getInstance()
        ref.collection("vocab")
            .get()
            .addOnSuccessListener { documents ->
                val results = mutableListOf<Vocab>()
                Log.d("FirebaseManager", "OnSuccessListener triggered!")
                for (document in documents) {
                    Log.d("FirebaseManager", "${document.id} => ${document.data}")
                    val vocab_translations = document.get("translations")
                    val vocab_mnemonics = document.get("mnemonics")
                    val vocab_genders = document.get("genders")
                    val vocab_types = document.get("types")
                    val vocab = Vocab(
                        document.getField<String>("id").toString(),
                        document.getField<String>("name").toString(),
                        document.getField<Int>("unlock_level")!!.toInt(),
                        vocab_translations as List<String>,
                        vocab_mnemonics as List<String>,
                        vocab_genders as List<String>,
                        vocab_types as List<String>
                    )
                    results.add(vocab)
                }
                Repository.currentVocab = results
                Repository.allVocab = results
            }
            .addOnFailureListener { exception ->
                Log.d("VocabSearchActivity", "OnFailureListener triggered!")
                Log.w("VocabSearchActivity", "Error getting documents: ", exception)
            }
    }
}