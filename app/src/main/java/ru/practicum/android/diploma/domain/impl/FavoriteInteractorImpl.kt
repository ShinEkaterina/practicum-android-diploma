package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.api.interactor.FavoriteInteractor
import ru.practicum.android.diploma.domain.api.repository.FavoriteVcRepository
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.Details
import ru.practicum.android.diploma.domain.model.VacancyModel

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteVcRepository,

    ) : FavoriteInteractor {

/*    override fun getMockResults(): Flow<ArrayList<VacancyModel>> {
        val vacancies = arrayListOf<VacancyModel>(
            VacancyModel(
                id = "1",
                vacancyName = "Software Engineer",
                city = "San Francisco",
                salary = "$100,000 - $120,000",
                companyName = "Tech Co.",
                logoUrls = "",
                details = Details(
                    experience = "3+ years",
                    employment = "Full-time",
                    description = "We are seeking a talented software engineer to join our team.",
                    keySkills = arrayListOf("Java", "Kotlin", "Android", "Git"),
                    managerName = "John Doe",
                    email = "john.doe@example.com",
                    phonesAndComments = arrayListOf(
                        Pair("123-456-7890", "Call for inquiries"),
                        Pair("987-654-3210", "Text only")
                    )
                )
            )
            // Add more VacancyModel objects if needed
        )

        return flow {
            emit(vacancies)
        }
    }*/

    override suspend fun add(vacancy: DetailVacancy) {
        favoriteRepository.add(vacancy) }

    override suspend fun delete(vacancyId: String) {
        favoriteRepository.delete(vacancyId)
    }

    override suspend fun getAll(): Flow<List<VacancyModel>> {
        return favoriteRepository.getAll()
    }

    override suspend fun getDetailVacancy(vacancyId: String): Flow<DetailVacancy?> {
        return favoriteRepository.getDetailVacancy(vacancyId)
    }

    override fun checkFavorite(vacancyId: String): Flow<Boolean> {
        return favoriteRepository.checkFavorite(vacancyId)
    }
}
