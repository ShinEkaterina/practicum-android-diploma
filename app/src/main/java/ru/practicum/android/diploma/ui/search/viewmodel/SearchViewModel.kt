package ru.practicum.android.diploma.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.api.interactor.SearchInteractor
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.NetworkError
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.fragment.sate.SearchRenderState
import ru.practicum.android.diploma.util.Constant
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filtrationInteractor: FiltrationInteractor
) : ViewModel() {

    private val renderStateLiveDate = MutableLiveData<SearchRenderState>()
    fun observeRenderState(): LiveData<SearchRenderState> = renderStateLiveDate

    private var filter: FilterParameters? = null

    private var currentPage = 0

    private var foundPages = 0

    val loadedVacancies = arrayListOf<VacancyModel?>()

    var vacanciesAmount = 0

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
            searchInteractor.getVacancies(searchString, currentPage++, Constant.PER_PAGE_ITEMS, getFilter())
                .collect { response ->
                    if (response is Resource.Success<*>) {
                        foundPages = response.data?.pages ?: 0
                        vacanciesAmount = response.data?.foundAsNumber ?: 0
                        vacanciesAmountAsString = response.data?.foundAsString ?: "0"
                        paginationStringRequest = searchString

                        if (vacanciesAmount > 0) {
                            loadedVacancies.clear()
                            loadedVacancies.addAll(response.data?.vacancies ?: arrayListOf())

                            if (foundPages != 1) {
                                loadedVacancies.add(null)
                            }

                            renderStateLiveDate.postValue(SearchRenderState.Success(true))
                        } else {
                            renderStateLiveDate.postValue(SearchRenderState.NothingFound)
                        }
                    } else if (response is Resource.Error && response.message == NetworkError.NO_CONNECTIVITY) {
                        renderStateLiveDate.postValue(SearchRenderState.NoInternet)

                    } else {
                        renderStateLiveDate.postValue(SearchRenderState.ServerError)
                    }
                }
        }
    }

    private fun render(
        s: SearchRenderState
    ) {
        renderStateLiveDate.postValue(s)
    }

    private fun paginationRequest() {
        loadingPaginationJob = viewModelScope.launch {
            delay(Constant.PAGINATION_AWAIT)
            if (renderStateLiveDate.value !is SearchRenderState.Loading) {
                renderStateLiveDate.postValue(SearchRenderState.PaginationLoading)
                searchInteractor.getVacancies(
                    paginationStringRequest,
                    currentPage++,
                    Constant.PER_PAGE_ITEMS,
                    getFilter()
                ).collect { response ->
                    if (response is Resource.Success<*>) {
                        loadedVacancies.removeLast()
                        loadedVacancies.addAll(response.data?.vacancies ?: arrayListOf())

                        if (currentPage < foundPages) {
                            loadedVacancies.add(null)
                        }

                        renderStateLiveDate.postValue(SearchRenderState.Success(false))
                    } else if (response.message == NetworkError.NO_CONNECTIVITY) {
                        render(SearchRenderState.PaginationNoInternet)

                    } else {
                        render(SearchRenderState.ServerError)
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
    fun getFilter(): HashMap<String, String> {
        viewModelScope.launch {
            filtrationInteractor
                .getFilterParametersFromStorage()
                .collect { filterParams ->
                    filter = filterParams
                }
        }
        val filters = HashMap<String, String>()

        filter?.idRegion?.let {
            filters["area"] = filter?.idRegion!!
        }

        filter?.idIndustry?.let {
            filters["industry"] = filter?.idIndustry!!
        }

        filter?.expectedSalary?.let {
            filters["salary"] = filter?.expectedSalary.toString()
        }
        filter?.isDoNotShowWithoutSalary?.let {
            filters["only_with_salary"] = filter?.isDoNotShowWithoutSalary.toString()
        }
        return filters
    }
    fun isFilterParametersNotEmpty(): Boolean {
        viewModelScope.launch {
            filtrationInteractor
                .getFilterParametersFromStorage()
                .collect { filterParams ->
                    filter = filterParams
                }
        }
        return filter?.idCountry != null ||
            filter?.nameCountry != null ||
            filter?.idRegion != null ||
            filter?.idIndustry != null ||
            filter?.nameIndustry != null ||
            filter?.expectedSalary != null ||
            filter?.isDoNotShowWithoutSalary == true
    }
}
