package ru.practicum.android.diploma.data.storage

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.model.FilterParameters

interface FilterStorage {
    fun getFilterParameters(): Flow<FilterParameters>
    fun setFilterParameters(filterParameters: FilterParameters): Flow<Boolean>
}
