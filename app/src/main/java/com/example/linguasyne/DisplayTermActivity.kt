package com.example.linguasyne

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import com.example.linguasyne.enums.Gender

class DisplayTerm : AppCompatActivity() {

    private var r_clickable = false
    private var l_clickable = false

    private lateinit var v: Term
    private lateinit var vSource: List<Term>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.display_term_translations_textview).movementMethod =
            ScrollingMovementMethod()
        findViewById<TextView>(R.id.display_term_mnemonics_textview).movementMethod =
            ScrollingMovementMethod()

        vSource =
            if (LessonManager.activeLesson) { //this is used in multiple methods so is in onCreate
                //set source to lesson
                LessonManager.current_lesson.lesson_list
            } else { //otherwise take from the normal term base
                //set source to all vocab
                VocabRepository.allVocab
            }

        //if the data to display is for a lesson, then take from the repo's lesson list
        v = if (LessonManager.activeLesson) {
            LessonManager.current_lesson.lesson_list[0]
        } else { //otherwise take from the normal term base
            FirebaseManager.loadVocabFromFirebase()
            VocabRepository.currentVocab[0]
        }

        displayVocabData()
        enableLeftRightArrows()

        findViewById<ImageView>(R.id.left_arrow_image).setOnClickListener {
            if (l_clickable) {
                if (!LessonManager.activeLesson) {
                    VocabRepository.filterById(vSource[vSource.indexOf(v) - 1].id.toString())
                } else {
                    v = LessonManager.current_lesson.lesson_list[vSource.indexOf(v) - 1]
                }
                enableLeftRightArrows()
                displayVocabData()
            }
        }
        findViewById<ImageView>(R.id.right_arrow_image).setOnClickListener {
            if (r_clickable) {
                if (!LessonManager.activeLesson) {
                    VocabRepository.filterById(vSource[vSource.indexOf(v) + 1].id.toString())
                } else {
                    v = LessonManager.current_lesson.lesson_list[vSource.indexOf(v) + 1]
                }
                enableLeftRightArrows()
                displayVocabData()
            }
        }

    }

    private fun clearUI() {
        findViewById<TextView>(R.id.term_name_textbox).text = ""
        findViewById<TextView>(R.id.term_unlock_level_textbox).text = ""
        findViewById<TextView>(R.id.display_term_translations_textview).text = ""
        findViewById<TextView>(R.id.display_term_mnemonics_textview).text = ""
        findViewById<TextView>(R.id.term_search_current_level_textview).text = ""
        findViewById<TextView>(R.id.term_search_next_level_textview).text = ""
        findViewById<TextView>(R.id.next_review_date).text = ""
    }

    private fun displayVocabData() {
        clearUI()

        //populate the text fields with the term's data
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

        //Set images for term genders - only applicable for nouns (vocab)
        if (v.class_type == LessonTypes.VOCAB) {
            setGenderImages(v as Vocab)
        }
    }

    private fun setGenderImages(v: Vocab) { //this needs to be fixed so that the images line up properly
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

        //

        //v_next = null
        //v_prev = null

        findViewById<ImageView>(R.id.right_arrow_image).setImageDrawable(null)
        findViewById<ImageView>(R.id.left_arrow_image).setImageDrawable(null)

        //If there is no next element (i.e. reached the end of the list), set image to alpha version and don't allow clickListener.
        if (vSource.indexOf(v) + 1 > vSource.size - 1) {
            findViewById<ImageView>(R.id.right_arrow_image).setBackgroundResource(R.drawable.alpharightarrow)
            r_clickable = false
        } else {
            //v_next = vSource[vSource.indexOf(v) + 1]
            findViewById<ImageView>(R.id.right_arrow_image).setBackgroundResource(R.drawable.opaquerightarrow)
            r_clickable = true
        }

        //Same check but for previous element (i.e. are we at the start of the list?)
        if (vSource.indexOf(v) - 1 < 0) {
            findViewById<ImageView>(R.id.left_arrow_image).setBackgroundResource(R.drawable.alphaleftarrow)
            l_clickable = false
        } else {
            //v_prev = vSource[vSource.indexOf(v) - 1]
            findViewById<ImageView>(R.id.left_arrow_image).setBackgroundResource(R.drawable.opaqueleftarrow)
            l_clickable = true
        }


    }
}



