package ru.practicum.android.diploma.domain.model

data class VacanciesModel(
    val pages: Long,
    val found: Long,
    val vacancies: ArrayList<VacancyModel>
)
