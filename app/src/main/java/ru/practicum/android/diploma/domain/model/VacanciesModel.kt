package ru.practicum.android.diploma.domain.model

data class VacanciesModel(
    val pages: Int,
    val foundAsNumber: Int,
    val foundAsString: String,
    val vacancies: List<VacancyModel>?
)
