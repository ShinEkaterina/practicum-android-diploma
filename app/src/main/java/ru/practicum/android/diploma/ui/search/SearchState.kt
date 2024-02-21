package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.model.VacancyModel

sealed interface SearchState {
    data object Loading : SearchState
    data class SearchContent(val vacancys: List<VacancyModel>) : SearchState
    data object Error : SearchState
    data object EmptyScreen : SearchState

}
