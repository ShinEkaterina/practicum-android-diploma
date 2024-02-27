package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.api.repository.FiltrationRepository
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.IndustriesModel

class FiltrationInteractorImpl(
    private val repository: FiltrationRepository
) : FiltrationInteractor {

    override fun getFilterParametersFromStorage(): Flow<FilterParameters> {
        return repository.getFilterParametersFromStorage()
    }

    override fun setFilterParametersToStorage(
        filterParameters: FilterParameters
    ): Flow<Boolean> {
        return repository.setFilterParametersToStorage(filterParameters)
    }

    override fun getIndustries(): Flow<Resource<List<IndustriesModel>>> {
        return repository.getIndustries()
    }

    override fun getAreas(): Flow<Resource<Map<AreasModel, List<AreasModel>>>> {
        return repository.getAreas()
    }

}
