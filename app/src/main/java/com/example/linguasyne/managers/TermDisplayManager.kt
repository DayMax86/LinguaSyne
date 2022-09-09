package com.example.linguasyne.managers

import android.content.Context
import android.content.Intent
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.classes.Term

object TermDisplayManager {

    var termList: List<Term> = RevisionSessionManager.currentSession.sl


}