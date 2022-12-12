package com.example.linguasyne.managers

import android.util.Log
import com.example.linguasyne.classes.Vocab
import kotlinx.coroutines.coroutineScope

object VocabRepository {
    var allVocab: List<Vocab> = emptyList()

    var currentVocab: List<Vocab> = emptyList()

    fun filterByName(search_termName: String) {
        currentVocab = allVocab
            .filter { it.name.contains(search_termName.toLowerCase()) }
            .sortedBy { it.name }
    }

    fun filterById(searchTermId: String) {
        currentVocab = allVocab
            .filter { it.id.contains(searchTermId.toLowerCase()) }
            .sortedBy { it.id }
    }

    fun filterByUnlockLevel(searchTermLevel: Int) {
        currentVocab = allVocab
            .filter { it.unlockLevel <= searchTermLevel }
            .sortedBy { it.id }
    }

    suspend fun filterByUserNotYetUnlocked() {
        var tempList: List<Vocab> = allVocab
        coroutineScope {
            FirebaseManager.getUserVocabUnlocks().apply {
                //TODO() This is an inefficient algorithm - it may bottleneck with large lists
                allVocab.forEach { av ->
                    this.forEach { uU ->
                        if (av.id == uU.id) {
                            tempList = tempList.minus(av)
                        }
                    }
                }
            }
            currentVocab = tempList
                .sortedBy { it.id }
        }

    }
}
