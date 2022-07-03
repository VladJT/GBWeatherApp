package jt.projects.gbweatherapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

// принимает текст для вывода или как строку, или как id Resources (String)
fun <T> View.showSnackBarShort(
    text: T,
) {
    Snackbar.make(this, this.getUniString(text), Snackbar.LENGTH_SHORT)
        .show()
}


// принимает текст для вывода SnackBar или как строку, или как id Resources (String)
fun <T, R> View.showSnackBarWithAction(
    text: T,
    actionText: R,
    action: () -> Unit
) {
    Snackbar
        .make(
            this,
            this.getUniString(text),
            Snackbar.LENGTH_INDEFINITE// отображается неопределенное время
        )
        .setAction(this.getUniString(actionText)) { action.invoke() }
        .show()
}

fun <T> View.getUniString(text: T): String {
    return ((text as? Int)?.let { resources.getText(text as Int) } ?: text) as String
}

// Расширяем функционал View для отображения клавиатуры
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

// Расширяем функционал View для скрытия клавиатуры
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}