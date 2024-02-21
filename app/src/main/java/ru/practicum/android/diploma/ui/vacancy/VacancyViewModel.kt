package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.DetailVacancyInteractor
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.ErrorNetwork

class VacancyViewModel(
    val vacancyInteractor: DetailVacancyInteractor,
) : ViewModel() {

    private val _vacancyState = MutableLiveData<VacancyState>()
    val vacancyState: LiveData<VacancyState> = _vacancyState
    private var vacancy: DetailVacancy? = null

    private fun renderState(state: VacancyState) {
        _vacancyState.postValue(state)
    }
    fun getVacancyDetail(id: String) {

        if (id.isNotEmpty()) {
            renderState(VacancyState.Loading)
            viewModelScope.launch {
                vacancyInteractor
                    .getDetailVacancy(id)
                    .collect { result ->
                        processResult(result.data, result.message)
                    }
            }
        }
    }
    private fun processResult(detailVacancy: DetailVacancy?, errorMessage: ErrorNetwork?) {
        if (detailVacancy != null) {
            vacancy = detailVacancy
        }
        when {
            errorMessage != null -> {
                renderState(VacancyState.Error)
            }
            vacancy==null -> {
                renderState(VacancyState.Error)
            }
            else -> {
                renderState(VacancyState.Content(vacancy!!))
            }
        }
    }

}
