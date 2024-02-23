package ru.practicum.android.diploma.ui.search.fragment.sate

import ru.practicum.android.diploma.domain.model.VacanciesModel

sealed interface SearchRenderState {

    data object Placeholder : SearchRenderState

    data class Loading(
        val isPagination: Boolean
    ) : SearchRenderState

    data class NoInternet(
        val isPagination: Boolean
    ) : SearchRenderState

    data object NothingFound : SearchRenderState

    data class Success(
        val vacancies: VacanciesModel
    ) : SearchRenderState

}
