package ru.practicum.android.diploma.domain.sharing

interface ExternalNavigator {
    fun call(number: String)
    fun sendEmail(email: String, name: String)
    fun shareVacancy(url: String)
}
