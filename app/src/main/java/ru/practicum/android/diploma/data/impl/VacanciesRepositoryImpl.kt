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
import ru.practicum.android.diploma.domain.model.NetworkError
import ru.practicum.android.diploma.domain.model.VacanciesModel
import java.net.HttpURLConnection.HTTP_OK

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
        if (response.responseCode == HTTP_OK) {
            emit(
                Resource.Success(
                    Convertors()
                        .convertorToVacanciesModel(response as SearchResponse)
                )
            )
        } else if (response.responseCode == NetworkError.NO_CONNECTIVITY.code) {
            emit(Resource.Error(NetworkError.NO_CONNECTIVITY))
        } else {
            emit(Resource.Error(NetworkError.INTERNAL_SERVER_ERROR))
        }
    }

    override fun getDetailVacancy(
        id: String
    ): Flow<Resource<DetailVacancy>> = flow {
        val response = networkClient.getDetailVacancy(VacancyDetailedRequest(id))
        if (response.responseCode == HTTP_OK) {
            val information =
                Convertors().responseToDetailModel(response as VacancyDetailedResponse)
            emit(Resource.Success(information))
        } else {
            emit(Resource.Error(NetworkError.INTERNAL_SERVER_ERROR))
        }
    }

    override fun getSimilarVacancies(
        id: String
    ): Flow<Resource<VacanciesModel>> = flow {
        val response = networkClient.getSimilarVacancies(VacanciesSimilarRequest(id))
        when (response.responseCode) {
            HTTP_OK -> {
                emit(Resource.Success(Convertors().convertorToSearchList(response as SearchResponse)))
            }

            NetworkError.NO_CONNECTIVITY.code -> {
                emit(Resource.Error(NetworkError.NO_CONNECTIVITY))
            }

            NetworkError.NOT_FOUND.code -> {
                emit(Resource.Error(NetworkError.NOT_FOUND))
            }

            else -> {
                emit(Resource.Error(NetworkError.INTERNAL_SERVER_ERROR))
            }
        }
    }

}
