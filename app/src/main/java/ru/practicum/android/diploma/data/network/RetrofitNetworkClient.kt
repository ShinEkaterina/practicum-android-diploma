package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import ru.practicum.android.diploma.data.dto.respone.Response.Companion.NO_INTERNET_RESULT_CODE
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.ErrorMessage
import ru.practicum.android.diploma.domain.model.IndustriesModel
import ru.practicum.android.diploma.util.Constant

class RetrofitNetworkClient(
    private val headHunterService: HeadHunterServiceApi,
    private val context: Context
) : NetworkClient {

    override suspend fun getVacancies(
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
                    responseCode = Constant.SUCCESS_RESULT_CODE
                }
            } catch (exception: HttpException) {
                Response().apply {
                    responseCode = exception.code()
                }
            }
        }
    }

    override suspend fun getDetailVacancy(
        dto: VacancyDetailedRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = NO_INTERNET_RESULT_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchConcreteVacancy(dto.id).apply {
                    responseCode = Constant.SUCCESS_RESULT_CODE
                }
            } catch (exception: HttpException) {
                Response().apply { responseCode = exception.code() }
            }
        }
    }

    override suspend fun getSimilarVacancies(
        dto: VacanciesSimilarRequest
    ): Response {
        if (!isConnected(context)) {
            return Response().apply { responseCode = NO_INTERNET_RESULT_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                headHunterService.searchSimilarVacancies(dto.id).apply {
                    responseCode = Constant.SUCCESS_RESULT_CODE
                }
            } catch (exception: HttpException) {
                Response().apply { responseCode = exception.code() }
            }
        }
    }

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

    override suspend fun getAreas(): Resource<Map<AreasModel, List<AreasModel>>> {
        if (!isConnected(context)) {
            return Resource.Error(ErrorMessage.NO_CONNECTIVITY_MESSAGE)
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
                Resource.Error(ErrorMessage.getErrorMessage(exception.message.toString()))
            }
        }
    }
    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}
