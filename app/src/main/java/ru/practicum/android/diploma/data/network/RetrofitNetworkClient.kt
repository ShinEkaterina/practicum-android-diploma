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
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.Error
import ru.practicum.android.diploma.domain.model.IndustriesModel
import ru.practicum.android.diploma.util.isConnected
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_OK
import java.net.SocketTimeoutException

class RetrofitNetworkClient(
    private val headHunterService: HeadHunterServiceApi,
    private val context: Context
) : NetworkClient {

    private fun handleNetworkException(e: Exception, error: Error): Response {
        return Response().apply { responseCode = error.code }
    }

    /*    override suspend fun getVacancies(
            dto: VacanciesSearchByNameRequest
        ): Response {
            if (!isConnected(context)) {
                return Response().apply {
                    responseCode = NO_INTERNET_RESULT_CODE
                }
            }
            return withContext(Dispatchers.IO) {
                try {
                    headHunterService.searchVacancies(dto.name, dto.page, dto.amount).apply {
                        responseCode = SUCCESS_RESULT_CODE
                    }
                } catch (exception: HttpException) {
                    Response().apply {
                        responseCode = SERVER_ERROR_RESULT_CODE
                    }
                } catch (exception: ConnectException) {
                    handleNetworkException(exception)
                }
            } catch (exception: SocketTimeoutException) {
                Response().apply {
                    responseCode = NO_INTERNET_RESULT_CODE
                }
            }
        }
    }*/

    override suspend fun getVacancies(
        dto: VacanciesSearchByNameRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply {
                responseCode = Error.NO_CONNECTIVITY.code
            }
        }
        return try {
            withContext(Dispatchers.IO) {
                headHunterService.searchVacancies(dto.name, dto.page, dto.amount).apply {
                    responseCode = HTTP_OK
                }
            }
        } catch (exception: HttpException) {
            handleNetworkException(exception, Error.NO_CONNECTIVITY)
        } catch (exception: ConnectException) {
            handleNetworkException(exception, Error.NO_CONNECTIVITY)
        } catch (exception: SocketTimeoutException) {
            handleNetworkException(exception, Error.NO_CONNECTIVITY)
        }
    }

    override suspend fun getDetailVacancy(
        dto: VacancyDetailedRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = Error.NO_CONNECTIVITY.code }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchConcreteVacancy(dto.id).apply {
                    responseCode = HTTP_OK
                }
            } catch (exception: HttpException) {
                handleNetworkException(exception, Error.INTERNAL_SERVER_ERROR)
            }
        }
    }

    override suspend fun getSimilarVacancies(
        dto: VacanciesSimilarRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = Error.NO_CONNECTIVITY.code }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchSimilarVacancies(dto.id).apply {
                    responseCode = HTTP_OK
                }
            } catch (exception: HttpException) {
                handleNetworkException(exception, Error.INTERNAL_SERVER_ERROR)
            }
        }
    }

    override suspend fun getIndustries(): Resource<List<IndustriesModel>> {
        if (!isConnected(context)) {
            return Resource.Error(Error.NO_CONNECTIVITY)
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
                Resource.Error(Error.getErrorMessage(exception.message.toString()))
            }
        }
    }

    override suspend fun getAreas(): Resource<Map<AreasModel, List<AreasModel>>> {
        if (!isConnected(context)) {
            return Resource.Error(Error.NO_CONNECTIVITY)
        }
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(
                    Convertors()
                        .converterAreasResponseToAreasModelList(
                            headHunterService.getAreas()
                        )
                )
            } catch (exception: HttpException) {
                Resource.Error(Error.getErrorMessage(exception.message.toString()))
            }
        }
    }

}
