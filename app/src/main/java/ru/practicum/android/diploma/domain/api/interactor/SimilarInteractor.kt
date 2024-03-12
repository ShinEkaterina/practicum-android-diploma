package ru.practicum.android.diploma.domain.api.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.model.NetworkError
import ru.practicum.android.diploma.domain.model.VacancyModel

interface SimilarInteractor {

    suspend fun getSimilarVacancy(
        id: String
    ): Flow<Pair<List<VacancyModel>?, NetworkError?>>
    suspend fun getOpenVacancy(
        id: String
    ): Flow<Pair<List<VacancyModel>?, NetworkError?>>

}
