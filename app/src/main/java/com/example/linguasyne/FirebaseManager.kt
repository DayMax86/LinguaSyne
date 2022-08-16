package com.example.linguasyne

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

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

    fun uploadUserImageToFirebase(uri: Uri?) {
        val filename = "profileImage"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val storageRef =
            FirebaseStorage.getInstance()
                .getReference("/users/${firebaseUser?.uid}/image/$filename")
        if (uri != null) {
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    Log.d("HomeActivity", "User image successfully uploaded to Firebase")
                    Log.d("HomeActivity", "User image uri = $uri")
                    Log.d(
                        "HomeActivity",
                        "User image path = /users/${firebaseUser?.uid}/image/$filename"
                    )
                }
            val imageRef = storageRef.child("/users/${firebaseUser?.uid}/image/$filename")
            val ONE_MEGABYTE: Long = 1024 * 1024
            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                current_user.user_image = bitmap
                Log.d("HomeActivity", "Current user's image has been set")

            }.addOnFailureListener {
                //TODO() Currently the onFailureListener is always triggering
                Log.d("HomeActivity", "Failed to get byte data from image")
            }
        } else {
            Log.d("HomeActivity", "Imagine upload to Firebase UNSUCCESSFUL - uri == null")
        }

    }

    /*fun getUserImageFromFirebase(): Bitmap? {
        val filename = "profileImage"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val storageRef =
            FirebaseStorage.getInstance()
                .getReference("/users/${firebaseUser?.uid}/image/$filename")
        if (firebaseUser != null) {
            storageRef.downloadUrl.addOnSuccessListener {
                Log.d("HomeActivity", "User image URL: $it")
            }
        }
        return null
    }*/

    fun createNewAccount(email: String, password: String): Boolean {

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var success = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Sign in success, update UI with the signed-in user's information
                Log.d("CreateAccountActivity", "createUserWithEmail:SUCCESS, $email")
                val user = User(email)
                current_user = user
                addUserToFirebase(user)
                // Let the source of the function call know if the account has been made successfully or not
                // so it can update the ui accordingly
                success = true
            }
            .addOnFailureListener {
                // If sign in fails, display a message to the user.
                Log.e(
                    "CreateAccountActivity",
                    "createUserWithEmail:FAILURE",
                )
                success = false
            }
        return success
    }

    fun addUserToFirebase(user: User) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("CreateAccountActivity", "User added to Firestore")
            }
            .addOnFailureListener {
                Log.e("CreateAccountActivity", it.toString())
            }
    }

}