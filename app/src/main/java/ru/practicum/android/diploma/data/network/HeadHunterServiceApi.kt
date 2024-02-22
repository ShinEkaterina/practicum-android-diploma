package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.data.dto.respone.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.respone.VacancyDetailedResponse

interface HeadHunterServiceApi {

    @GET("/vacancies")
    suspend fun searchVacancies(
        @Query("text") name: String,
        @Query("page") page: Int,
        @Query("per_page") amount: Long
    ): SearchResponse

    @GET("/vacancies/{id}/similar_vacancies")
    suspend fun searchSimilarVacancies(
        @Path("id") id: String,
        @Query("page") page: Long,
        @Query("per_page") amount: Long
    ): VacanciesSearchResponse
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun searchConcreteVacancy(
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailedResponse

}
