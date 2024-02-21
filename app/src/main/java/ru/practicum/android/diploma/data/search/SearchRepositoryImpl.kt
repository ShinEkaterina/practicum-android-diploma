package ru.practicum.android.diploma.data.search

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Convertors
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.VacanciesSearchDtoResponse
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchByNameRequest
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.model.VacanciesModel
import ru.practicum.android.diploma.util.Constant

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context
) : SearchRepository {

    override fun searchVacancies(
        vacancyName: String,
        page: Long,
        amount: Long
    ): Flow<Pair<VacanciesModel, Int>> = flow {
        val response = networkClient.doRequest(VacanciesSearchByNameRequest(vacancyName, page, amount))
        if (response.responseCode == 200) {
            emit(
                Pair(
                    Convertors(context).convertorToVacanciesModel(response as VacanciesSearchDtoResponse),
                    response.responseCode
                )
            )
        } else {
            emit(
                Pair(
                    VacanciesModel(-1, -1, "", arrayListOf()),
                    Constant.SERVER_ERROR
                )
            )
        }
    }

}
