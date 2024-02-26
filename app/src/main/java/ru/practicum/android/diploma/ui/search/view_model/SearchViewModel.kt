package ru.practicum.android.diploma.ui.search.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.impl.SearchSuccess
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.fragment.sate.SearchRenderState
import ru.practicum.android.diploma.util.Constant
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.isConnected

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val application: Application
): ViewModel() {

    private val renderStateLiveDate = MutableLiveData<SearchRenderState>()
    fun observeRenderState(): LiveData<SearchRenderState> = renderStateLiveDate

    private var currentPage = 0L

    private var foundPages = 0L

    val loadedVacancies = arrayListOf<VacancyModel?>()

    var vacanciesAmount = 0L

    var vacanciesAmountAsString = ""

    private var paginationStringRequest = ""

    private var loadingPaginationJob: Job? = null

    val searchDebounce = debounce<String?>(
        delayInMillis = Constant.SEARCH_DEBOUNCE_WAIT_MILLIS,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { searchString ->
        if (searchString != null) {
            if (loadingPaginationJob?.isCompleted == false) {
                loadingPaginationJob?.cancel()
            }
            renderStateLiveDate.postValue(SearchRenderState.Loading)
            currentPage = 0
            searchInteractor.searchVacancies(searchString, 0L, Constant.PER_PAGE_ITEMS).collect { response ->
                if (response is SearchSuccess) {
                    foundPages = response.vacancies.pages
                    vacanciesAmount = response.vacancies.foundAsNumber
                    vacanciesAmountAsString = response.vacancies.foundAsString
                    paginationStringRequest = searchString

                    if (vacanciesAmount > 0) {
                        loadedVacancies.clear()
                        loadedVacancies.addAll(response.vacancies.vacancies)

                        if (foundPages != 1L) {
                            loadedVacancies.add(null)
                        }

                        renderStateLiveDate.postValue(SearchRenderState.Success(true))
                    } else {
                        renderStateLiveDate.postValue(SearchRenderState.NothingFound)
                    }
                } else {
                    renderStateLiveDate.postValue(SearchRenderState.NoInternet)
                }
            }
        }
    }

    private fun paginationRequest() {
        loadingPaginationJob = viewModelScope.launch {
            delay(Constant.PAGINATION_AWAIT)
            if (!isConnected(application.applicationContext)) {
                renderStateLiveDate.postValue(SearchRenderState.PaginationNoInternet)
            }
            if (renderStateLiveDate.value !is SearchRenderState.Loading) {
                renderStateLiveDate.postValue(SearchRenderState.PaginationLoading)
                searchInteractor.searchVacancies(paginationStringRequest, ++currentPage, Constant.PER_PAGE_ITEMS).collect { response ->
                    if (response is SearchSuccess) {
                        loadedVacancies.removeLast()
                        loadedVacancies.addAll(response.vacancies.vacancies)

                        if (currentPage < foundPages) {
                            loadedVacancies.add(null)
                        }

                        renderStateLiveDate.postValue(SearchRenderState.Success(false))
                    } else {
                        renderStateLiveDate.postValue(SearchRenderState.PaginationNoInternet)
                    }
                }
            }
        }
    }

    fun toSuccessState() {
        renderStateLiveDate.postValue(SearchRenderState.Success(false))
    }

    fun clearAllInput() {
        renderStateLiveDate.postValue(SearchRenderState.Default)
    }

    fun onLastItemReached() {
        if (currentPage < foundPages && loadingPaginationJob?.isCompleted != false) {
            paginationRequest()
        }
    }

    fun startVacanciesSearch(
        searchString: String
    ) {
        currentPage = 0
        if (searchString.isNotEmpty()) {
            searchDebounce(searchString)
        } else {
            loadingPaginationJob?.cancel()
            searchDebounce(null)
            renderStateLiveDate.postValue(SearchRenderState.Default)
        }
    }

}
