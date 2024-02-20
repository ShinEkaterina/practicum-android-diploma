package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.model.VacanciesModel

class SearchInteractorImpl(
    private val searchRepository: SearchRepository
) : SearchInteractor {

    override fun searchVacancies(
        vacancyName: String,
        page: Long,
        amount: Long
    ): Flow<Pair<VacanciesModel, Int>> {
        return searchRepository.searchVacancies(vacancyName, page, amount)
    }

}
