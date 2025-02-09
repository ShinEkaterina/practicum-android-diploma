package ru.practicum.android.diploma.ui.vacancy

import ru.practicum.android.diploma.domain.model.DetailVacancy

sealed interface VacancyState {
    data object Loading : VacancyState
    data class Content(val vacancy: DetailVacancy) : VacancyState
    data object ErrorServer : VacancyState
    data object NotInternet : VacancyState

}
