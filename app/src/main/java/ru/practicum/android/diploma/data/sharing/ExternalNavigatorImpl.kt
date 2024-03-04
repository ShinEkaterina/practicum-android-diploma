package ru.practicum.android.diploma.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.domain.sharing.ExternalNavigator

class ExternalNavigatorImpl(
    private val context: Context
) : ExternalNavigator {

    override fun call(number: String) {
        val call = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", number, null)
        )
        call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(call)
    }

    override fun sendEmail(email: String, name: String) {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse("mailto:")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        // intent.putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, name)
        context.startActivity(intent)
    }

    override fun shareVacancy(url: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        context.startActivity(intent)
    }
}
