package ru.practicum.android.diploma.domain.api.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.model.EmployerModel

interface EmployerInteractor {
    suspend fun getEmployer(id: String): Flow<Resource<EmployerModel>>
}
