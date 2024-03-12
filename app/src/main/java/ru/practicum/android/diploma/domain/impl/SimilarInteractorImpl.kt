package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.SimilarInteractor
import ru.practicum.android.diploma.domain.api.repository.VacanciesRepository
import ru.practicum.android.diploma.domain.model.NetworkError
import ru.practicum.android.diploma.domain.model.VacancyModel

class SimilarInteractorImpl(
    private val repository: VacanciesRepository
) : SimilarInteractor {

    override suspend fun getSimilarVacancy(
        id: String
    ): Flow<Pair<List<VacancyModel>?, NetworkError?>> {
        return repository.getSimilarVacancies(id).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data?.vacancies, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
    override suspend fun getOpenVacancy(
        id: String
    ): Flow<Pair<List<VacancyModel>?, NetworkError?>> {
        return repository.getOpenVacancies(id).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data?.vacancies, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

}
