package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.SearchInteractor
import ru.practicum.android.diploma.domain.api.repository.VacanciesRepository
import ru.practicum.android.diploma.domain.model.VacanciesModel

class SearchInteractorImpl(
    private val searchRepository: VacanciesRepository
) : SearchInteractor {

    override fun getVacancies(
        vacancyName: String,
        page: Int,
        amount: Int,
        filter: HashMap<String, String>
    ): Flow<Resource<VacanciesModel>> {
        return searchRepository.getVacancies(vacancyName, page, amount, filter)
    }

}
