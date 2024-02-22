package ru.practicum.android.diploma.domain.api.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.model.FilterParameters

interface FiltrationRepository {
    fun getFilterParametersFromStorage(): Flow<FilterParameters>
    fun setFilterParametersToStorage(filterParameters: FilterParameters): Flow<Boolean>
}
