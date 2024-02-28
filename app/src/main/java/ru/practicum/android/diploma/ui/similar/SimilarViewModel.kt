package ru.practicum.android.diploma.ui.similar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.SimilarInteractor
import ru.practicum.android.diploma.domain.model.Error
import ru.practicum.android.diploma.domain.model.VacancyModel

class SimilarViewModel(
    val similarInteractor: SimilarInteractor,
) : ViewModel() {

    private val _vacancyState = MutableLiveData<SimilarState>()
    val similarState: LiveData<SimilarState> = _vacancyState
    private val vacanciesList = mutableListOf<VacancyModel>()
    private fun renderState(state: SimilarState) {
        _vacancyState.postValue(state)
    }

    fun getSimilarVacancies(id: String) {
        if (id.isNotEmpty()) {
            renderState(SimilarState.Loading)
            viewModelScope.launch {
                similarInteractor
                    .getSimilarVacancy(id)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }
    private fun processResult(vacancyies: List<VacancyModel>?, errorMessage: Error?) {
        if (vacancyies != null) {
            vacanciesList.addAll(vacancyies)
            renderState(SimilarState.Content(vacanciesList))
        }
        when (errorMessage) {
            Error.INTERNAL_SERVER_ERROR -> {
                renderState(SimilarState.ErrorServer)
            }
            Error.NOT_FOUND -> {
                renderState(SimilarState.NotFound)
            }
            Error.NO_CONNECTIVITY -> {
                renderState(SimilarState.NotInternet)
            }
            else -> {
                renderState(SimilarState.Content(vacanciesList))
            }
        }
    }
}
