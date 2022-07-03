package jt.projects.gbweatherapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBarShort(
    text: String,
) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
        .show()
}

fun View.showSnackBarWithAction(
    text: String,
    actionText: String,
    action: () -> Unit
) {
    Snackbar
        .make(this, text, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText) { action.invoke() }
        .show()
}