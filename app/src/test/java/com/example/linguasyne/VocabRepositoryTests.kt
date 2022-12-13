package com.example.linguasyne


import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.VocabRepository
import com.example.linguasyne.viewmodels.VocabDisplayViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class VocabRepositoryTests {

    private val scope = TestScope(UnconfinedTestDispatcher())

    private lateinit var viewModel: VocabDisplayViewModel

    @BeforeEach
    fun beforeEach() {
        VocabRepository.currentVocab = listOf(Vocab())
        //viewModel = VocabDisplayViewModel()
    }

    @Test
    @DisplayName("Should return a list of 21 objects")
    fun filterByUnlockLevel_isCorrect() {
        scope
            .launch {
                FirebaseManager.loadVocabFromFirebase()
            }
            .invokeOnCompletion {
                VocabRepository.allVocab
                    .filter { it.unlockLevel <= 0 }
                    .sortedBy { it.id }

                assertEquals(21, VocabRepository.currentVocab.size)
            }

    }

}
