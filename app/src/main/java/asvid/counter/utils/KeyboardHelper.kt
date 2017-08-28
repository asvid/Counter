package asvid.counter.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun startAlphaAnimation(view: View, context: Context) {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view.windowToken, 0)
}