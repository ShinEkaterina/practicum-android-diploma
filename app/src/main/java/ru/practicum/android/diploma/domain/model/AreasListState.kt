package ru.practicum.android.diploma.domain.model

sealed interface AreasListState {
    data object Loading : AreasListState
    data class Content(val areas: List<AreasModel>) : AreasListState
    data object Error : AreasListState
}
