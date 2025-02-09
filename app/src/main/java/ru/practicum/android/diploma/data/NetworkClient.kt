package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchByNameRequest
import ru.practicum.android.diploma.data.dto.request.VacanciesSimilarRequest
import ru.practicum.android.diploma.data.dto.request.VacancyDetailedRequest
import ru.practicum.android.diploma.data.dto.respone.Response
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.IndustriesModel

interface NetworkClient {

    suspend fun getVacancies(
        dto: VacanciesSearchByNameRequest
    ): Response

    suspend fun getDetailVacancy(
        dto: VacancyDetailedRequest
    ): Response

    suspend fun getIndustries(): Resource<List<IndustriesModel>>

    suspend fun getSimilarVacancies(
        dto: VacanciesSimilarRequest
    ): Response

    suspend fun getAreas(): Resource<Map<AreasModel, List<AreasModel>>>

}
