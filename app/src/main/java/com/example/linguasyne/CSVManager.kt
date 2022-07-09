package com.example.linguasyne

import android.R.raw
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.getSystem
import android.util.Log
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.firestore.FirebaseFirestore
import java.io.InputStream

object CSVManager {

    fun importVocabCSV(context: Context) {
        val inputstr: InputStream = context.resources.openRawResource(R.raw.vocab_data)
        csvReader().open(inputstr) {
            readAllAsSequence().forEach { row ->
                Log.d("CSVManager", "$row")

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