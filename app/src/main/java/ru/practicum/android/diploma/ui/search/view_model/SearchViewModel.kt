package ru.practicum.android.diploma.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.ui.search.fragment.sate.SearchRenderState
import ru.practicum.android.diploma.util.Constant
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor
): ViewModel() {

    private val renderStateLiveDate = MutableLiveData<SearchRenderState>(SearchRenderState.Placeholder)
    fun observeRenderState(): LiveData<SearchRenderState> = renderStateLiveDate

    private var currentPage = 0L

    val searchDebounce = debounce<String?>(
        delayInMillis = Constant.SEARCH_DEBOUNCE_WAIT,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { searchString ->
        if (searchString != null) {
            searchInteractor.searchVacancies(searchString, currentPage, Constant.PER_PAGE_ITEMS).collect { response ->
                val responseCode = response.second
                if (responseCode == Constant.SUCCESS_RESULT_CODE) {
                    val foundVacancies = response.first
                    if (foundVacancies.foundAsNumber > 0) {
                        renderStateLiveDate.postValue(
                            SearchRenderState.Success(
                                vacancies = foundVacancies
                            )
                        )
                    } else {
                        renderStateLiveDate.postValue(SearchRenderState.NothingFound)
                    }
                } else {
                    renderStateLiveDate.postValue(SearchRenderState.NoInternet)
                }
            }
        }
    }

    fun clearAllInput() {
        renderStateLiveDate.postValue(SearchRenderState.Placeholder)
    }

    fun startVacanciesSearch(
        searchString: String
    ) {
        if (searchString.isNotEmpty()) {
            searchDebounce(searchString)
        } else {
            searchDebounce(null)
        }
    }

}
