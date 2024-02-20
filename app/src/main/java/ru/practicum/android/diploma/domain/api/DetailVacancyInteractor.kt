package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.model.DetailVacancy

interface DetailVacancyInteractor {
    suspend fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>>
}
