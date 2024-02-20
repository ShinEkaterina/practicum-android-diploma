package ru.practicum.android.diploma.commons.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyModel

interface FavoriteInteractor {
    fun getMockResults(): Flow<ArrayList<VacancyModel>>
}