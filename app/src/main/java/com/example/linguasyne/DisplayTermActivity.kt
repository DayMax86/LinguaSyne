package com.example.linguasyne

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.TermTypes
import com.google.firebase.FirebaseCommonRegistrar

class DisplayTerm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.display_term_translations_textview).movementMethod =
            ScrollingMovementMethod()
        findViewById<TextView>(R.id.display_term_mnemonics_textview).movementMethod =
            ScrollingMovementMethod()

        //This was overriding the filtered list when a lesson is created.
        //FirebaseManager.loadVocabFromFirebase()

        //Need some way of the display data function knowing to take from current_lesson rather than all vocab when activity is started from lesson button

        displayVocabData()
        enableLeftRightArrows()

        findViewById<ImageView>(R.id.left_arrow_image).setOnClickListener {
            if (l_clickable) {
//                Log.d("DisplayTermActivity", "v_prev is null!")
                VocabRepository.filterVocabRepositoryById(v_prev?.id.toString())
                enableLeftRightArrows()
                displayVocabData()
            }
        }
        findViewById<ImageView>(R.id.right_arrow_image).setOnClickListener {
            if (r_clickable) {
//                Log.d("DisplayTermActivity", "v_next is null!")
                VocabRepository.filterVocabRepositoryById(v_next?.id.toString())
                enableLeftRightArrows()
                displayVocabData()
            }
        }

    }

    var v_prev: Term? = null
    var v_next: Term? = null
    var r_clickable = false
    var l_clickable = false


    private fun clearUI() {
        findViewById<TextView>(R.id.term_name_textbox).text =
            ""
        findViewById<TextView>(R.id.term_unlock_level_textbox).text =
            ""
        findViewById<TextView>(R.id.display_term_translations_textview).text =
            ""
        findViewById<TextView>(R.id.display_term_mnemonics_textview).text =
            ""
        findViewById<TextView>(R.id.term_search_current_level_textview).text =
            ""
        findViewById<TextView>(R.id.term_search_next_level_textview).text =
            ""
        findViewById<TextView>(R.id.next_review_date).text =
            ""
    }

    private fun displayVocabData() {
        clearUI()

        val v: Term = if (VocabRepository.activeLesson) {
            LessonManager.current_lesson.lesson_list[0]
        } else {
            FirebaseManager.loadVocabFromFirebase()
            VocabRepository.currentVocab[0]
        }

        findViewById<TextView>(R.id.term_name_textbox).text =
            v.name
        findViewById<TextView>(R.id.term_unlock_level_textbox).text =
            v.unlock_level.toString()
        for (translations in v.translations) {
            findViewById<TextView>(R.id.display_term_translations_textview).text =
                (findViewById<TextView>(R.id.display_term_translations_textview).text.toString()
                    .plus("\''$translations', "))
        }
        for (mnemonics in v.mnemonics) {
            findViewById<TextView>(R.id.display_term_mnemonics_textview).text =
                (findViewById<TextView>(R.id.display_term_mnemonics_textview).text.toString()
                    .plus("$mnemonics. "))
        }
        findViewById<TextView>(R.id.term_search_current_level_textview).text =
            v.current_level_term.toString()
        findViewById<TextView>(R.id.term_search_next_level_textview).text =
            (v.current_level_term + 1).toString()
        findViewById<TextView>(R.id.next_review_date).text =
            v.next_review.toString()

        //Set images for term genders
        if (v.class_type == LessonTypes.VOCAB) {
            setGenderImages(v as Vocab)
        }



    }

    private fun setGenderImages(v: Vocab) {
        for (gender in v.genders) {
            if (gender == Gender.M) {
                findViewById<ImageView>(R.id.display_term_masculine_imageview).setImageDrawable(
                    null
                )
                findViewById<ImageView>(R.id.display_term_masculine_imageview).setBackgroundResource(
                    R.drawable.opaquemars
                )
            } else if (gender == Gender.F) {
                findViewById<ImageView>(R.id.display_term_masculine_imageview).setImageDrawable(
                    null
                )
                findViewById<ImageView>(R.id.display_term_feminine_imageview).setBackgroundResource(
                    R.drawable.opaquevenus
                )
            }
        }
    }

    private fun enableLeftRightArrows() {
        //First of all check if there is vocab to the left or right of the sorted allVocab list,
        //if not then alpha version of image should be displayed.

        //THIS NEEDS TO CHANGE DEPENDING ON IF IT'S A LESSON OR JUST THE TERM BASE!
        val v: Vocab = VocabRepository.currentVocab[0]

        v_next = null
        v_prev = null

        findViewById<ImageView>(R.id.right_arrow_image).setImageDrawable(null)
        findViewById<ImageView>(R.id.left_arrow_image).setImageDrawable(null)

        //If there is no next element (i.e. reached the end of the list), set image to alpha version and don't allow clickListener.
        if (VocabRepository.allVocab.indexOf(v) + 1 > VocabRepository.allVocab.size - 1) {
            findViewById<ImageView>(R.id.right_arrow_image).setBackgroundResource(R.drawable.alpharightarrow)
            r_clickable = false
        } else {
            v_next = VocabRepository.allVocab[VocabRepository.allVocab.indexOf(v) + 1]
            findViewById<ImageView>(R.id.right_arrow_image).setBackgroundResource(R.drawable.opaquerightarrow)
            r_clickable = true
        }

        //Same check but for previous element (i.e. are we at the start of the list?)
        if (VocabRepository.allVocab.indexOf(v) - 1 < 0) {
            findViewById<ImageView>(R.id.left_arrow_image).setBackgroundResource(R.drawable.alphaleftarrow)
            l_clickable = false
        } else {
            v_prev = VocabRepository.allVocab[VocabRepository.allVocab.indexOf(v) - 1]
            findViewById<ImageView>(R.id.left_arrow_image).setBackgroundResource(R.drawable.opaqueleftarrow)
            l_clickable = true
        }


    }
}



