package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.model.ErrorNetwork
import ru.practicum.android.diploma.domain.model.VacancyModel

interface SearchInteractor {
    fun search(expression: String, page: Int): Flow<Pair<List<VacancyModel>?, ErrorNetwork?>>
}
