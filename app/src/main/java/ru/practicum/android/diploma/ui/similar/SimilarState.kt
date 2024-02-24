package ru.practicum.android.diploma.ui.similar

import ru.practicum.android.diploma.domain.model.VacancyModel

sealed interface SimilarState {
    data object Loading : SimilarState
    data class Content(val vacancies: List<VacancyModel>) : SimilarState
    data object NotInternet : SimilarState
    data object NotFound : SimilarState
    data object ErrorServer : SimilarState

}
