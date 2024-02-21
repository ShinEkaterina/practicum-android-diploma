package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacancyModel

interface FavoriteVcRepository {
    suspend fun add(vacancy: DetailVacancy)
    suspend fun delete(vacancyId: String)
    suspend fun getAll(): Flow<List<VacancyModel>>

    suspend fun getDetailVacancy(id: String): Flow<DetailVacancy?>
    fun checkFavorite(vacancyId: String): Flow<Boolean>

}