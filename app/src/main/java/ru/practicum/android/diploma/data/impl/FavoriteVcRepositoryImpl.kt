package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.covertors.VacancyDbConvertor
import ru.practicum.android.diploma.domain.api.repository.FavoriteVcRepository
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacancyModel

class FavoriteVcRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: VacancyDbConvertor
) : FavoriteVcRepository {
    override suspend fun add(vacancy: DetailVacancy) {
        appDatabase.vacancyDao().saveVacancy(converter.map(vacancy))
    }

    override suspend fun update(vacancy: DetailVacancy) {
        appDatabase.vacancyDao().updateVacancy(converter.map(vacancy))
    }

    override suspend fun delete(vacancyId: String) {
        appDatabase.vacancyDao().deleteVacancy(vacancyId)
    }

    override suspend fun getAll(): Flow<List<VacancyModel>> {
        val vacancies = appDatabase.vacancyDao().getVacancyList()
        val vacanciesModel = converter.map(vacancies)
        return vacanciesModel
    }

    override suspend fun getDetailVacancy(id: String): Flow<DetailVacancy?> {
        return flow {
            val vac = appDatabase.vacancyDao().getVacancyById(id).first()
            if (vac == null) {
                emit(null)
            } else {
                vac.let {
                    emit(converter.map(it))
                }
            }

        }
    }

    override fun checkFavorite(vacancyId: String): Flow<Boolean> {
        return flow {
            val answer = appDatabase.vacancyDao().isVacancyFavorite(vacancyId) != null
            emit(answer)
        }
    }

}