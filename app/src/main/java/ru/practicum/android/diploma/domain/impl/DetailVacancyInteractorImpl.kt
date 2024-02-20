package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.DetailVacancyInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.model.DetailVacancy

class DetailVacancyInteractorImpl(private val repository: VacanciesRepository) : DetailVacancyInteractor {
    override suspend fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>> {
        return repository.getDetailVacancy(id)
    }
}
