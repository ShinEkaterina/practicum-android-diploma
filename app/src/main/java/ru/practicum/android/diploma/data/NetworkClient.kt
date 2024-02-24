package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.request.VacanciesSimilarRequest
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.data.dto.request.VacancyDetailedRequest
import ru.practicum.android.diploma.data.dto.respone.Response
import ru.practicum.android.diploma.domain.model.IndustriesModel

interface NetworkClient {

    suspend fun doRequest(
        dto: Any
    ): Response

    suspend fun getDetailVacancy(dto: VacancyDetailedRequest): Response

    suspend fun getIndustries(): Resource<List<IndustriesModel>>
    suspend fun getSimilarVacancies(dto: VacanciesSimilarRequest): Response
}
