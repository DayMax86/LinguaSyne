package com.example.linguasyne

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

object FirebaseManager {

    lateinit var current_user: User

    fun loadVocabFromFirebase() {
        Log.d("VocabSearchActivity", "Before attaching listener")
        val ref = FirebaseFirestore.getInstance()
        ref.collection("vocab")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("VocabSearchActivity", "Inside OnSuccessListener")
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
                VocabRepository.currentVocab = results
                VocabRepository.allVocab = results.sortedBy {
                    it.name
                }
            }
            .addOnFailureListener { exception ->
                Log.d("VocabSearchActivity", "Inside OnFailureListener")
                Log.d("VocabSearchActivity", "Error getting documents: ", exception)
            }
        Log.d("VocabSearchActivity", "After attaching listener")
    }

    fun addVocabToFirebase(term: Term) {
        FirebaseFirestore.getInstance()
            .collection("vocab")
            .add(term)
            .addOnSuccessListener {
                Log.d("HomeActivity", "Vocab item #" + "$term.id" + " added to firebase")
            }
    }

    fun uploadUserImageToFirebase(iv: ImageView) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("image.jpg")
        val imagesRef = storageRef.child("images/image.jpg")

        // Get the data from an ImageView as bytes
        iv.isDrawingCacheEnabled = true
        iv.buildDrawingCache()
        val bitmap = (iv.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

}