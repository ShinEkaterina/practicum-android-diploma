package ru.practicum.android.diploma.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.practicum.android.diploma.R

@SuppressLint("InflateParams")
fun showToast(
    fragment: Fragment,
    layoutInflater: LayoutInflater,
    message: String
) {
    val successSnackbar = Snackbar.make(fragment.requireView(), "", Snackbar.LENGTH_LONG)
    val customSuccessSnackbar = layoutInflater.inflate(R.layout.custom_toast, null)

    val snackbarContent = customSuccessSnackbar?.findViewById<TextView>(R.id.custom_toast_content)

    snackbarContent?.text = message

    successSnackbar.view.setBackgroundColor(Color.TRANSPARENT)

    val snackbarLayout = successSnackbar.view as Snackbar.SnackbarLayout

    snackbarLayout.setPadding(0, 0, 0, 0)
    snackbarLayout.addView(customSuccessSnackbar, 0)

    successSnackbar.show()
}
