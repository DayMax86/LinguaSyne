package com.example.linguasyne

import android.R.raw
import android.content.res.Resources
import android.util.Log
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.firestore.FirebaseFirestore
import java.io.InputStream

object CSVManager {

    fun importVocabCSV() {
        val inputsstr: InputStream = Resources.getSystem().openRawResource(R.raw.test)
        csvReader().open(inputsstr) {
            readAllAsSequence().forEach { row ->
                Log.d("HomeActivity", "$row")

                val term = Vocab(
                    row[0],                          //id
                    row[1],                          //name
                    row[2].toInt(),                  //unlock level
                    row[3].split("/"),    //translations
                    row[4].split("/"),    //mnemonics
                    row[5].split("/"),    //gender
                    row[6].split("/")     //types
                )
                FirebaseManager.addVocabToFirebase(term)
            }

        }

    }


}