package ru.practicum.android.diploma.domain.api.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.model.FilterParameters

interface FiltrationInteractor {
    fun getFilterParametersFromStorage(): Flow<FilterParameters>
    fun setFilterParametersToStorage(filterParameters: FilterParameters): Flow<Boolean>
}
