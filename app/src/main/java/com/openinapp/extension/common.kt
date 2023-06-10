package com.openinapp.extension

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(msg: String, duration: Int = Snackbar.LENGTH_INDEFINITE) {
    val snackbar =
        Snackbar.make(this, "" + msg, duration).setAction("Action", null)
    snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.LTGRAY)
    val textView =
        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.BLUE)
    textView.textSize = 18f
    snackbar.show()
}