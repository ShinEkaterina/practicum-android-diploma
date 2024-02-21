package ru.practicum.android.diploma.domain.model

sealed interface FilterParametersState {
    data class Content(val filterParameters: FilterParameters): FilterParametersState
    data object Updating: FilterParametersState
}
