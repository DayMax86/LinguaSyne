package com.daymax86.linguasyne.enums

object ReviewTimes { //Times in hours of next review
    const val NOW = 0
    const val ONE_DAY = 24
    const val TWO_DAYS = 48
    const val ONE_WEEK = 168
    const val TWO_WEEKS = 336
    const val ONE_MONTH = 730
    const val TWO_MONTHS = 1460
    const val FOUR_MONTHS = 2920
    const val EIGHT_MONTHS = 5840
    const val ONE_YEAR = 8760
    const val NEVER = -1 //Negative to differentiate when a term is fully learnt
}