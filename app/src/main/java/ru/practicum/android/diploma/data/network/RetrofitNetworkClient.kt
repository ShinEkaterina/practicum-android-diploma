package ru.practicum.android.diploma.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.Convertors
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchByNameRequest
import ru.practicum.android.diploma.data.dto.request.VacanciesSimilarRequest
import ru.practicum.android.diploma.data.dto.request.VacancyDetailedRequest
import ru.practicum.android.diploma.data.dto.respone.Response
import ru.practicum.android.diploma.data.dto.respone.Response.Companion.BAD_REQUEST_RESULT_CODE
import ru.practicum.android.diploma.data.dto.respone.Response.Companion.NO_INTERNET_RESULT_CODE
import ru.practicum.android.diploma.data.dto.respone.Response.Companion.SUCCESS_RESULT_CODE
import ru.practicum.android.diploma.domain.model.ErrorMessage
import ru.practicum.android.diploma.domain.model.IndustriesModel
import ru.practicum.android.diploma.util.isConnected

class RetrofitNetworkClient(
    private val headHunterService: HeadHunterServiceApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(
        dto: Any
    ): Response {
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacanciesSearchByNameRequest -> {
                        headHunterService.searchVacancies(dto.name, dto.page, dto.amount).apply {
                            responseCode = SUCCESS_RESULT_CODE
                        }
                    }

                    else -> {
                        Response().apply {
                            responseCode = BAD_REQUEST_RESULT_CODE
                        }
                    }
                }
            } catch (exception: HttpException) {
                Response().apply { responseCode = exception.code() }

            }
        }
    }
    override suspend fun getDetailVacancy(dto: VacancyDetailedRequest): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = NO_INTERNET_RESULT_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchConcreteVacancy(dto.id).apply {
                    responseCode = SUCCESS_RESULT_CODE
                }
            } catch (exception: HttpException) {
                Response().apply { responseCode = exception.code() }
            }
        }
    }

    override suspend fun getSimilarVacancies(dto: VacanciesSimilarRequest): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = NO_INTERNET_RESULT_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchSimilarVacancies(dto.id).apply {
                    responseCode = SUCCESS_RESULT_CODE
                }
            } catch (exception: HttpException) {
                Response().apply { responseCode = exception.code() }

    override suspend fun getIndustries(): Resource<List<IndustriesModel>> {
        if (!isConnected(context)) {
            return Resource.Error(ErrorMessage.NO_CONNECTIVITY_MESSAGE)
        }
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(
                    Convertors()
                        .converterIndustriesResponseToIndustriesModelList(
                            headHunterService.getIndustries()
                        )
                )
            } catch (exception: HttpException) {
                Resource.Error(ErrorMessage.getErrorMessage(exception.message.toString()))
            }
        }
    }
}
