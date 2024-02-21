package ru.practicum.android.diploma.domain.model

data class VacanciesModel(
    val pages: Long,
    val foundAsNumber: Long,
    val foundAsString: String,
    val vacancies: ArrayList<VacancyModel>
)
