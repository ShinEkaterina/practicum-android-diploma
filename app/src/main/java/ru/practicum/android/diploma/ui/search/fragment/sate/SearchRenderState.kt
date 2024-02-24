package ru.practicum.android.diploma.ui.search.fragment.sate

sealed interface SearchRenderState {

    data object Placeholder : SearchRenderState

    data object Loading : SearchRenderState

    data object NoInternet : SearchRenderState

    data object NothingFound : SearchRenderState

    data object PaginationLoading : SearchRenderState

    data object PaginationNoInternet : SearchRenderState

    data object Success : SearchRenderState

}
