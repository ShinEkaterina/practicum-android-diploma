package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.model.VacanciesModel

interface SearchRepository {

    fun searchVacancies(
        vacancyName: String,
        page: Long,
        amount: Long
    ): Flow<Pair<VacanciesModel, Int>>

}
