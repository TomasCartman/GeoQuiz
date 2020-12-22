package com.dashboarder.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton : Button
    private lateinit var falseButton : Button
    private lateinit var nextImageButton : ImageButton
    private lateinit var previousImageButton : ImageButton
    private lateinit var questionTextView : TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true, false, false),
        Question(R.string.question_oceans, true, false, false),
        Question(R.string.question_mideast, false, false, false),
        Question(R.string.question_africa, false, false, false),
        Question(R.string.question_americas, true, false, false),
        Question(R.string.question_asia, true, false, false)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate(Bundle?) called")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextImageButton = findViewById(R.id.next_imageButton)
        previousImageButton = findViewById(R.id.previous_imageButton)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
                view: View ->
                    checkAnswer(true)
        }

        falseButton.setOnClickListener {
                view: View ->
                    checkAnswer(false)
        }

        nextImageButton.setOnClickListener {
            if(currentIndex < questionBank.size  - 1) {
                currentIndex += 1
                updateQuestion()
            }
        }

        previousImageButton.setOnClickListener {
            if(currentIndex > 0) {
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
            }
        }

        questionTextView.setOnClickListener {
            if(currentIndex < questionBank.size  - 1) {
                currentIndex += 1
                updateQuestion()
            }
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        checkButtonIsEnable()
    }

    private fun checkButtonIsEnable() {
        if(questionBank[currentIndex].userHasAnswered) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].questionAnswer
        var messageResId = 0

        if(userAnswer == correctAnswer) {
            questionBank[currentIndex].userHasAnswered = true
            questionBank[currentIndex].userIsCorrect = true
            messageResId = R.string.correct_toast
        } else {
            questionBank[currentIndex].userHasAnswered = true
            questionBank[currentIndex].userIsCorrect = false
            messageResId = R.string.incorrect_toast
        }

        checkButtonIsEnable()
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        var count = questionBank.size
        var score = 0
        for (question in questionBank) {
            if(question.userHasAnswered) {
                count -= 1
                if(question.userIsCorrect) {
                    score += 1
                }
            }
        }
        if(count == 0) {
            val percent = score.toDouble()/questionBank.size * 100
            Toast.makeText(this, "Your score was: $percent%", Toast.LENGTH_LONG)
                .show()
        }
    }
}