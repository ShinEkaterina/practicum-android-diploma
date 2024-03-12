package ru.practicum.android.diploma.domain.model

data class EmployerModel(
    val id: String,
    val name: String,
    val area: String? = "",
    val description: String,
    val site: String,
    val logoUrl: String,
    val openVacancies: Int = 0,
)
