package com.dashboarder.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val questionAnswer: Boolean,
                    var userHasAnswered: Boolean, var userIsCorrect: Boolean)