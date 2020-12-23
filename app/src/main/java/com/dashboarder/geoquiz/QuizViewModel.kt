package com.dashboarder.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    var currentIndex = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true, false, false, false),
        Question(R.string.question_oceans, true, false, false, false),
        Question(R.string.question_mideast, false, false, false, false),
        Question(R.string.question_africa, false, false, false, false),
        Question(R.string.question_americas, true, false, false, false),
        Question(R.string.question_asia, true, false, false, false)
    )

    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].questionAnswer
    val currentQuestionText: Int get() = questionBank[currentIndex].textResId
    val questionBankSize: Int get() = questionBank.size
    var currentUserHasAnswered: Boolean get() = questionBank[currentIndex].userHasAnswered
        set(value) { questionBank[currentIndex].userHasAnswered = value }
    var currentUserIsCorrect: Boolean get() = questionBank[currentIndex].userIsCorrect
        set(value) { questionBank[currentIndex].userIsCorrect = value }
    var currentUserHasCheated: Boolean get() = questionBank[currentIndex].userHasCheated
        set(value) { questionBank[currentIndex].userHasCheated = value }

    fun moveToNext() {
        if(currentIndex < questionBank.size - 1) currentIndex += 1
    }

    fun moveToPrevious() {
        if(currentIndex > 0) currentIndex -= 1
    }
}