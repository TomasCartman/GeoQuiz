package com.dashboarder.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton : Button
    private lateinit var falseButton : Button
    private lateinit var cheatButton : Button
    private lateinit var nextImageButton : ImageButton
    private lateinit var previousImageButton : ImageButton
    private lateinit var questionTextView : TextView
    private lateinit var remainingCheatsTextView : TextView
    private var score = 0
    private var count = 0

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate(Bundle?) called")

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextImageButton = findViewById(R.id.next_imageButton)
        previousImageButton = findViewById(R.id.previous_imageButton)
        questionTextView = findViewById(R.id.question_text_view)
        remainingCheatsTextView = findViewById(R.id.remaining_cheats_textView)

        remainingCheatsTextView.text = quizViewModel.remainingCheats.toString()
            .plus(" Remaining cheats")

        trueButton.setOnClickListener {
                view: View ->
                    checkAnswer(true)
        }

        falseButton.setOnClickListener {
                view: View ->
                    checkAnswer(false)
        }

        nextImageButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            view ->
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val options =
                        ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)

                    startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
                } else {
                    startActivityForResult(intent, REQUEST_CODE_CHEAT)
                }

        }

        previousImageButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK) return
        if(requestCode == REQUEST_CODE_CHEAT) {
            val userHasCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.currentUserHasCheated = userHasCheated
            if(userHasCheated) {
                quizViewModel.remainingCheats -= 1
                remainingCheatsTextView.text = quizViewModel.remainingCheats.toString()
                    .plus(" Remaining cheats")
                checkButtonIsEnable()
            }
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        checkButtonIsEnable()
    }

    private fun checkButtonIsEnable() {
        if(quizViewModel.currentUserHasAnswered) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
        if(quizViewModel.remainingCheats <= 0) cheatButton.isEnabled = false
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        //var messageResId = 0
        var questionBankSize = quizViewModel.questionBankSize

        quizViewModel.currentUserHasAnswered = true

        count -= 1
        val messageResId = when {
            quizViewModel.currentUserHasCheated -> {
                R.string.judgment_toast
            }
            userAnswer == correctAnswer -> {
                quizViewModel.currentUserIsCorrect = true
                score += 1
                R.string.correct_toast
            }
            else -> {
                quizViewModel.currentUserIsCorrect = false
                R.string.incorrect_toast
            }
        }

        checkButtonIsEnable()
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if(count + questionBankSize == 0) {
            val percent = score.toDouble()/quizViewModel.questionBankSize * 100
            Toast.makeText(this, "Your score was: $percent%", Toast.LENGTH_LONG)
                .show()
        }
    }
}