package ru.practicum.android.diploma.data.search

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Convertors
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.VacanciesSearchDtoResponse
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchByNameRequest
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.api.SearchResponse
import ru.practicum.android.diploma.domain.impl.SearchError
import ru.practicum.android.diploma.domain.impl.SearchSuccess

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context
) : SearchRepository {

    override fun searchVacancies(
        vacancyName: String,
        page: Long,
        amount: Long
    ): Flow<SearchResponse> = flow {
        val response = networkClient.doRequest(VacanciesSearchByNameRequest(vacancyName, page, amount))
        if (response.responseCode == 200) {
            emit(SearchSuccess(Convertors(context).convertorToVacanciesModel(response as VacanciesSearchDtoResponse)))
        } else {
            emit(SearchError(response.responseCode))
        }
    }

}
