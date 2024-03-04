package ru.practicum.android.diploma.domain.api.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.model.DetailVacancy

interface DetailVacancyInteractor {
    suspend fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>>
    suspend fun call(number: String)
    suspend fun sendEmail(email: String, name: String)
    suspend fun shareVacancy(url: String)
}
