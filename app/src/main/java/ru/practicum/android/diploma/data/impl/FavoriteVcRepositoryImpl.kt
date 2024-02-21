package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.covertors.VacancyDbConvertor
import ru.practicum.android.diploma.domain.api.FavoriteVcRepository
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacancyModel

class FavoriteVcRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: VacancyDbConvertor
) : FavoriteVcRepository {
    override suspend fun add(vacancy: DetailVacancy) {
        appDatabase.vacancyDao().saveVacancy(converter.map(vacancy))
    }

    override suspend fun delete(vacancyId: String) {
        appDatabase.vacancyDao().deleteVacancy(vacancyId)
    }

    override fun getAll(): Flow<List<VacancyModel>> {
        val vacancies = appDatabase.vacancyDao().getVacancyList()
        val vacanciesModel = converter.map(vacancies)
        return vacanciesModel
    }


    override fun checkFavorite(vacancyId: String): Flow<Boolean> {
        return flow {
            val answer = appDatabase.vacancyDao().isVacancyFavorite(vacancyId) != null
            emit(answer)
        }
    }

}