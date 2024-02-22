package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.SearchInteractor
import ru.practicum.android.diploma.domain.api.repository.VacanciesRepository
import ru.practicum.android.diploma.domain.model.ErrorMessage
import ru.practicum.android.diploma.domain.model.VacancyModel

class SearchInteractorImpl(private val repository: VacanciesRepository) : SearchInteractor {
    override fun search(expression: String, page: Int): Flow<Pair<List<VacancyModel>?, ErrorMessage?>> {
        return repository.search(expression, page).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data?.listVacancy, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

}
