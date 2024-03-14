package ru.practicum.android.diploma.ui.employer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.EmployerInteractor
import ru.practicum.android.diploma.domain.model.EmployerModel
import ru.practicum.android.diploma.domain.model.NetworkError

class EmployerViewModel(
    val employerInteractor: EmployerInteractor,
) : ViewModel() {

    private val _employerState = MutableLiveData<EmployerState>()
    val employerState: LiveData<EmployerState> = _employerState
    private var employer: EmployerModel? = null

    private fun renderState(state: EmployerState) {
        _employerState.postValue(state)
    }

    fun getEmployer(id: String) {
        if (id.isNotEmpty()) {
            renderState(EmployerState.Loading)
            viewModelScope.launch {
                employerInteractor
                    .getEmployer(id)
                    .collect {
                        processResult(it.data, it.message)
                    }
            }
        }
    }
    private fun processResult(emploeyrs: EmployerModel?, errorMessage: NetworkError?) {
        if (emploeyrs != null) {
            employer = emploeyrs
            renderState(EmployerState.Content(employer!!))
        }
        when (errorMessage) {
            NetworkError.INTERNAL_SERVER_ERROR -> {
                renderState(EmployerState.ErrorServer)
            }
            NetworkError.NO_CONNECTIVITY -> {
                renderState(EmployerState.NotInternet)
            }
            else -> {
                renderState(EmployerState.Content(employer!!))
            }
        }
    }
}
