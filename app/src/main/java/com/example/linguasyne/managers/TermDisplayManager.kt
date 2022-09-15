package com.example.linguasyne.managers

import android.content.Context
import android.content.Intent
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.classes.Vocab

object TermDisplayManager {

    var vocabList: List<Vocab> = RevisionSessionManager.currentSession.sl


}