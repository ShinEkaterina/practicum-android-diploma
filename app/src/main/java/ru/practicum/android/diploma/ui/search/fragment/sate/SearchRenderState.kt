package ru.practicum.android.diploma.ui.search.fragment.sate

sealed interface SearchRenderState {

    data object Default : SearchRenderState

    data object Loading : SearchRenderState

    data object NoInternet : SearchRenderState

    data object NothingFound : SearchRenderState

    data object PaginationLoading : SearchRenderState

    data object ServerError : SearchRenderState

    data object PaginationNoInternet : SearchRenderState

    data class Success(
        val resetScroll: Boolean
    ) : SearchRenderState

}
