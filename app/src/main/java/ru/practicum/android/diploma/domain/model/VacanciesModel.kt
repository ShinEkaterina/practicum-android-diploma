package ru.practicum.android.diploma.domain.model

data class VacanciesModel(
    val found: Int?,
    val maxPages: Int?,
    val currentPages: Int?,
    val listVacancy: List<VacancyModel>?
)
