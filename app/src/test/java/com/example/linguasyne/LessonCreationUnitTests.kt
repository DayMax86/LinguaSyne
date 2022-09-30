package com.example.linguasyne

import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.LessonManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

@RunWith(JUnit4::class)
class CreateLessonTest {
    @Test
    fun createLesson_isCorrect() {
        //assertThat("Current vocab should have size 5", VocabRepository.currentVocab.size,5)
        assertEquals(5, LessonManager.currentLesson.lessonList.size)
    }
}

class TestAddLessonContentToFirebase {
    @Test
    suspend fun ContentAdded_isCorrect() {

        val userFirestoreVocab: List<Vocab> = FirebaseFirestore
            .getInstance()
            .collection("users")
            .document("${FirebaseManager.currentUser?.email}")
            .collection("terms")
            .get()
            .await()
            .toObjects(Vocab::class.java)
        //If this is a lesson, unlock the terms for the user and add them to their personal firebase list
        for (term: Vocab in LessonManager.currentLesson.lessonList) {
            //For each vocab item in the lesson list
            if (userFirestoreVocab.binarySearch(
                    element = term,
                    comparator = compareBy {
                        it.id
                    }
                ) < 0 //The binary search returns a negative number if the element is not found
            ) {
                //For each vocab item in the user's firebase list

            }

        }

    }
}
