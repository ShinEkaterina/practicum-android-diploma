package ru.practicum.android.diploma.domain.api.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.IndustriesModel

interface FiltrationInteractor {
    fun getFilterParametersFromStorage(): Flow<FilterParameters>
    fun setFilterParametersToStorage(filterParameters: FilterParameters): Flow<Boolean>
    fun getIndustries(): Flow<Resource<List<IndustriesModel>>>
}
