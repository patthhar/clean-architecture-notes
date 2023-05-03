package me.darthwithap.android.cleanarchitecturenotes.util

import android.util.Log
import me.darthwithap.android.cleanarchitecturenotes.util.Constants.DEBUG
import me.darthwithap.android.cleanarchitecturenotes.util.Constants.TAG

var isUnitTest = false

fun logD(className: String?, message: String) {
  if (DEBUG && !isUnitTest) {
    Log.d(TAG, "$className: $message")
  } else if (DEBUG && isUnitTest) {
    println("$className: $message")
  }
}

