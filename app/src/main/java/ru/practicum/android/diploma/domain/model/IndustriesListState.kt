package ru.practicum.android.diploma.domain.model

sealed interface IndustriesListState {
    data object Loading : IndustriesListState
    data class Content(val industries: List<IndustriesModel>) : IndustriesListState
    data object Error : IndustriesListState
}
