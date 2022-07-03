package jt.projects.gbweatherapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

// принимает текст для вывода или как строку, или как id Resources (String)
fun <T> View.showSnackBarShort(
    text: T,
) {
    val t = (text as? Int)?.let { resources.getText(text as Int) } ?: text.toString()
    Snackbar.make(this, t, Snackbar.LENGTH_SHORT)
        .show()
}


// принимает текст для вывода или как строку, или как id Resources (String)
fun <T, R> View.showSnackBarWithAction(
    text: T,
    actionText: R,
    action: () -> Unit
) {
    val t1 = (text as? Int)?.let { resources.getText(text as Int) } ?: text.toString()
    val t2 =
        (actionText as? Int)?.let { resources.getText(actionText as Int) } ?: actionText.toString()
    Snackbar
        .make(this, t1, Snackbar.LENGTH_INDEFINITE)
        .setAction(t2) { action.invoke() }
        .show()
}