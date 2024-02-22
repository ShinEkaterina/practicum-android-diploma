package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.storage.FilterStorage
import ru.practicum.android.diploma.domain.api.repository.FiltrationRepository
import ru.practicum.android.diploma.domain.model.FilterParameters

class FiltrationRepositoryImpl(private val filterStorage: FilterStorage) : FiltrationRepository {
    override fun getFilterParametersFromStorage(): Flow<FilterParameters> {
        return filterStorage.getFilterParameters()
    }

    override fun setFilterParametersToStorage(filterParameters: FilterParameters): Flow<Boolean> {
        return filterStorage.setFilterParameters(filterParameters)
    }
}
