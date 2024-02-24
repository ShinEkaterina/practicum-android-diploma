package ru.practicum.android.diploma.domain.api.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacanciesModel

interface VacanciesRepository {
    fun search(expression: String, page: Int): Flow<Resource<VacanciesModel>>
    fun getDetailVacancy(
        id: String
    ): Flow<Resource<DetailVacancy>>
    fun getSimilarVacancies(id: String): Flow<Resource<VacanciesModel>>
}
