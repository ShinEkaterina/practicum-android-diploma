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
import ru.practicum.android.diploma.domain.model.NetworkError
import ru.practicum.android.diploma.domain.model.IndustriesModel
import ru.practicum.android.diploma.util.isConnected
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_OK
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val headHunterService: HeadHunterServiceApi,
    private val context: Context
) : NetworkClient {

    private fun handleNetworkException(e: Exception, networkError: NetworkError): Response {
        return Response().apply { responseCode = networkError.code }
    }

    override suspend fun getVacancies(
        dto: VacanciesSearchByNameRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply {
                responseCode = NetworkError.NO_CONNECTIVITY.code
            }
        }
        return try {
            withContext(Dispatchers.IO) {
                headHunterService.searchVacancies(dto.name, dto.page, dto.amount).apply {
                    responseCode = HTTP_OK
                }
            }
        } catch (exception: HttpException) {
            Response().apply { responseCode = exception.code() }
        } catch (exception: ConnectException) {
            handleNetworkException(exception, NetworkError.NO_CONNECTIVITY)
        } catch (exception: SocketTimeoutException) {
            handleNetworkException(exception, NetworkError.NO_CONNECTIVITY)
        } catch (exception: UnknownHostException) {
            handleNetworkException(exception, NetworkError.NO_CONNECTIVITY)
        }
    }

    override suspend fun getDetailVacancy(
        dto: VacancyDetailedRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = NetworkError.NO_CONNECTIVITY.code }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchConcreteVacancy(dto.id).apply {
                    responseCode = NetworkError.UNKNOWN_ERROR.code
                }
            } catch (exception: HttpException) {
                handleNetworkException(exception, NetworkError.INTERNAL_SERVER_ERROR)
            }
        }
    }

    override suspend fun getSimilarVacancies(
        dto: VacanciesSimilarRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = NetworkError.NO_CONNECTIVITY.code }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchSimilarVacancies(dto.id).apply {
                    responseCode = HTTP_OK
                }
            } catch (exception: HttpException) {
                handleNetworkException(exception, NetworkError.INTERNAL_SERVER_ERROR)
            }
        }
    }

    override suspend fun getIndustries(): Resource<List<IndustriesModel>> {
        if (!isConnected(context)) {
            return Resource.Error(NetworkError.NO_CONNECTIVITY)
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
                Resource.Error(NetworkError.getErrorMessage(exception.message.toString()))
            }
        }
    }

    override suspend fun getAreas(): Resource<Map<AreasModel, List<AreasModel>>> {
        if (!isConnected(context)) {
            return Resource.Error(NetworkError.NO_CONNECTIVITY)
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
                Resource.Error(NetworkError.getErrorMessage(exception.message.toString()))
            }
        }
    }

}
