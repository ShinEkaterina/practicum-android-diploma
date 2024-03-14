package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.EmployerInteractor
import ru.practicum.android.diploma.domain.api.repository.VacanciesRepository
import ru.practicum.android.diploma.domain.model.EmployerModel

class EmployerInteractorImpl(val repository: VacanciesRepository) : EmployerInteractor {

    override suspend fun getEmployer(id: String): Flow<Resource<EmployerModel>> {
        return repository.getEmployer(id)
    }
}
