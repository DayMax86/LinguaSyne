package com.example.linguasyne

class Verb(term_id: String,
           term_name: String,
           term_unlock_level: Int,
           term_translations: List<String>,
           term_mnemonics: List<String>) : Term(term_id,term_name,term_unlock_level,term_translations,term_mnemonics)