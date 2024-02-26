package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchVacancies(
        vacancyName: String,
        page: Long,
        amount: Long
    ): Flow<SearchResponse>

}
