package ru.practicum.android.diploma.ui.employer

import ru.practicum.android.diploma.domain.model.EmployerModel

sealed interface EmployerState {
    data object Loading : EmployerState
    data class Content(val employerModel: EmployerModel) : EmployerState
    data object ErrorServer : EmployerState
    data object NotInternet : EmployerState

}
