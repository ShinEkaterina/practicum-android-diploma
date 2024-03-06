package ru.practicum.android.diploma.domain.model

sealed interface CountriesListState {
    data object Loading : CountriesListState
    data class Content(val countries: List<AreasModel>) : CountriesListState
    data object Error : CountriesListState
}
