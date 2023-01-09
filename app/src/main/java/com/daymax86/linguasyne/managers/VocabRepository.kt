package com.daymax86.linguasyne.managers

import android.util.Log
import com.daymax86.linguasyne.classes.Vocab
import kotlinx.coroutines.coroutineScope

object VocabRepository {
    var allVocab: List<Vocab> = emptyList()

    var currentVocab: List<Vocab> = emptyList()

    fun filterByName(search_termName: String) { //Not currently used but may be in future versions
        currentVocab = allVocab
            .filter { it.name.contains(search_termName.lowercase()) }
            .sortedBy { it.name }
    }

    fun filterById(searchTermId: String) {
        currentVocab = allVocab
            .filter { it.id.contains(searchTermId.lowercase()) }
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
