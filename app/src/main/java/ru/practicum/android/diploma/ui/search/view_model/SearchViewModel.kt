package ru.practicum.android.diploma.ui.search.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.fragment.sate.SearchRenderState
import ru.practicum.android.diploma.util.Constant
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor
): ViewModel() {

    private val renderStateLiveDate = MutableLiveData<SearchRenderState>(SearchRenderState.Placeholder)
    fun observeRenderState(): LiveData<SearchRenderState> = renderStateLiveDate

    private var currentPage = 0L

    val loadedVacancies = arrayListOf<VacancyModel>()

    private var foundPages = 0L

    var vacanciesAmount = 0L

    var vacanciesAmountAsString = ""

    var showToast = true

    val searchDebounce = debounce<String?>(
        delayInMillis = Constant.SEARCH_DEBOUNCE_WAIT,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { searchString ->
        searchRequest(searchString)
    }

    private suspend fun searchRequest(
        searchString: String?
    ) {
        if (searchString != null && (currentPage == 0L || renderStateLiveDate.value !is SearchRenderState.Loading)) {
            Log.e("123", searchString.toString())
            renderStateLiveDate.postValue(SearchRenderState.Loading(currentPage != 0L))
            searchInteractor.searchVacancies(searchString, currentPage++, Constant.PER_PAGE_ITEMS).collect { response ->
                val responseCode = response.second
                if (responseCode == Constant.SUCCESS_RESULT_CODE) {
                    val foundVacancies = response.first
                    foundPages = foundVacancies.pages
                    vacanciesAmount = foundVacancies.foundAsNumber
                    vacanciesAmountAsString = foundVacancies.foundAsString
                    if (foundVacancies.foundAsNumber > 0) {
                        loadedVacancies.addAll(foundVacancies.vacancies)
                        renderStateLiveDate.postValue(
                            SearchRenderState.Success(
                                vacancies = foundVacancies
                            )
                        )
                    } else {
                        renderStateLiveDate.postValue(SearchRenderState.NothingFound)
                    }
                } else {
                    showToast = true
                    renderStateLiveDate.postValue(SearchRenderState.NoInternet(currentPage - 1 != 0L))
                }
            }
        }
    }

    fun clearAllInput() {
        searchDebounce(null)
        renderStateLiveDate.postValue(SearchRenderState.Placeholder)
    }

    fun onLastItemReached(
        searchString: String
    ) {
        if (currentPage < foundPages) {
            viewModelScope.launch {
                searchRequest(searchString)
            }
        }
    }

    fun startVacanciesSearch(
        searchString: String
    ) {
        currentPage = 0
        if (searchString.isNotEmpty()) {
            searchDebounce(searchString)
        } else {
            searchDebounce(null)
        }
    }

}
