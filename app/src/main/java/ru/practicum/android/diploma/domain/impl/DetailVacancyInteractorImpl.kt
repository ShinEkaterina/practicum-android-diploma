package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.DetailVacancyInteractor
import ru.practicum.android.diploma.domain.api.repository.VacanciesRepository
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.sharing.ExternalNavigator

class DetailVacancyInteractorImpl(
    private val repository: VacanciesRepository,
    private val externalNavigator: ExternalNavigator
) :
    DetailVacancyInteractor {
    override suspend fun getDetailVacancy(id: String): Flow<Resource<DetailVacancy>> {
        return repository.getDetailVacancy(id)
    }

    override suspend fun call(number: String) {
        return externalNavigator.call(number)
    }

    override suspend fun sendEmail(email: String, name: String) {
        return externalNavigator.sendEmail(email, name)
    }

    override suspend fun shareVacancy(url: String) {
        externalNavigator.shareVacancy(url)
    }
}
