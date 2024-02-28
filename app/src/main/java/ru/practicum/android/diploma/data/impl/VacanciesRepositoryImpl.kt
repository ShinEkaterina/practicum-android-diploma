package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.Convertors
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchByNameRequest
import ru.practicum.android.diploma.data.dto.request.VacanciesSimilarRequest
import ru.practicum.android.diploma.data.dto.request.VacancyDetailedRequest
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.data.dto.respone.VacancyDetailedResponse
import ru.practicum.android.diploma.domain.api.repository.VacanciesRepository
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.Error
import ru.practicum.android.diploma.domain.model.VacanciesModel
import ru.practicum.android.diploma.util.Constant.SUCCESS_RESULT_CODE

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient
) : VacanciesRepository {

    override fun getVacancies(
        expression: String,
        page: Int,
        amount: Int
    ): Flow<Resource<VacanciesModel>> = flow {
        val response =
            networkClient.getVacancies(VacanciesSearchByNameRequest(expression, page, amount))
        if (response.responseCode == SUCCESS_RESULT_CODE) {
            emit(
                Resource.Success(
                    Convertors()
                        .convertorToVacanciesModel(response as SearchResponse)
                )
            )
        } else {
            emit(Resource.Error(Error.INTERNAL_SERVER_ERROR))
        }
    }

    override fun getDetailVacancy(
        id: String
    ): Flow<Resource<DetailVacancy>> = flow {
        val response = networkClient.getDetailVacancy(VacancyDetailedRequest(id))
        if (response.responseCode == SUCCESS_RESULT_CODE) {
            val information =
                Convertors().responseToDetailModel(response as VacancyDetailedResponse)
            emit(Resource.Success(information))
        } else {
            emit(Resource.Error(Error.INTERNAL_SERVER_ERROR))
        }
    }

    override fun getSimilarVacancies(
        id: String
    ): Flow<Resource<VacanciesModel>> = flow {
        val response = networkClient.getSimilarVacancies(VacanciesSimilarRequest(id))
        when (response.responseCode) {
            SUCCESS_RESULT_CODE -> {
                emit(Resource.Success(Convertors().convertorToSearchList(response as SearchResponse)))
            }

            Error.NO_CONNECTIVITY.code -> {
                emit(Resource.Error(Error.NO_CONNECTIVITY))
            }

            Error.NOT_FOUND.code -> {
                emit(Resource.Error(Error.NOT_FOUND))
            }

            else -> {
                emit(Resource.Error(Error.INTERNAL_SERVER_ERROR))
            }
        }
    }

}
