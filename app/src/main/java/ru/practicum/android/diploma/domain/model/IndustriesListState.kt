package ru.practicum.android.diploma.domain.model

sealed interface IndustriesListState {
    object Loading: IndustriesListState
    data class Content(val industries: List<IndustriesModel>): IndustriesListState
    object Error: IndustriesListState
}
