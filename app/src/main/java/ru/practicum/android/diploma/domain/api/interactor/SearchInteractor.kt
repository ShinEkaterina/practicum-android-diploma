package ru.practicum.android.diploma.domain.api.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.model.VacanciesModel

interface SearchInteractor {

    fun getVacancies(
        vacancyName: String,
        page: Int,
        amount: Int,
        filter: HashMap<String, String>
    ): Flow<Resource<VacanciesModel>>

}
