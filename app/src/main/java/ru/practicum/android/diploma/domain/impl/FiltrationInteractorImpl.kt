package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.api.repository.FiltrationRepository
import ru.practicum.android.diploma.domain.model.FilterParameters

class FiltrationInteractorImpl(private val repository: FiltrationRepository) :
    FiltrationInteractor {
    override fun getFilterParametersFromStorage(): Flow<FilterParameters> {
        return repository.getFilterParametersFromStorage()
    }

    override fun setFilterParametersToStorage(filterParameters: FilterParameters): Flow<Boolean> {
        return repository.setFilterParametersToStorage(filterParameters)
    }
}
