package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.storage.FilterStorage
import ru.practicum.android.diploma.domain.api.repository.FiltrationRepository
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.IndustriesModel

class FiltrationRepositoryImpl(
    private val filterStorage: FilterStorage,
    private val networkClient: NetworkClient
) : FiltrationRepository {
    override fun getFilterParametersFromStorage(): Flow<FilterParameters> {
        return filterStorage.getFilterParameters()
    }

    override fun setFilterParametersToStorage(filterParameters: FilterParameters): Flow<Boolean> {
        return filterStorage.setFilterParameters(filterParameters)
    }

    override fun getIndustries(): Flow<Resource<List<IndustriesModel>>> = flow {
        emit(networkClient.getIndustries())
    }

    override fun getAreas(): Flow<Resource<Map<AreasModel, List<AreasModel>>>> = flow {
        emit(networkClient.getAreas())
    }
}
