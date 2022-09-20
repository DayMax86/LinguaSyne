package com.example.linguasyne.managers

import android.util.Log
import com.example.linguasyne.classes.Vocab
import kotlinx.coroutines.coroutineScope

object VocabRepository {
    var listChange: (() -> Unit)? = null

    var allVocab: List<Vocab> = emptyList()

    var currentVocab: List<Vocab> = emptyList()
        set(value) {
            field = value
            listChange?.invoke()
        }

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
            .filter { it.unlockLevel == searchTermLevel }
            .sortedBy { it.id }
    }

    suspend fun filterByUserNotYetUnlocked() {
        val userUnlocks = FirebaseManager.getUserVocabUnlocks()
        var tempList: List<Vocab> = emptyList()
        allVocab
            .forEach { vAll ->
                val e = userUnlocks.firstOrNull {
                    it.id == vAll.id
                }
                if (e == null) {
                    tempList = tempList.plusElement(vAll)
                }
            }
        currentVocab = tempList
            .sortedBy { it.id }
    }

    suspend fun filterByUserUnlocked() {
        val userUnlocks = FirebaseManager.getUserVocabUnlocks()
        val tempList: List<Vocab> = emptyList()
        allVocab
            .forEach { Vall ->
                tempList.plus(userUnlocks
                    .firstOrNull { Vuser ->
                        Vuser.id == Vall.id
                    })
                Log.d(
                    "VocabRepository",
                    "UserList ID: ${tempList.firstOrNull { it.id == Vall.id }?.id}, AllVocab ID: ${Vall.id}"
                )
            }
        currentVocab = tempList
            .sortedBy { it.id }
    }

}