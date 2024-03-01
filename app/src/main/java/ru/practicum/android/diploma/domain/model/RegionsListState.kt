package ru.practicum.android.diploma.domain.model

sealed interface RegionsListState {
    data object Loading : RegionsListState
    data class Content(val regions: List<AreasModel>) : RegionsListState
    data object Error : RegionsListState
}
