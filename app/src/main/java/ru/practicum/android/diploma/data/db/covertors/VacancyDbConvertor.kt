package ru.practicum.android.diploma.data.db.covertors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacancyModel

class VacancyDbConvertor {
    fun map(vacancy: DetailVacancy): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            areaName = vacancy.areaName,
            areaUrl = vacancy.areaUrl,
            contactsEmail = vacancy.contactsEmail,
            contactsName = vacancy.contactsName,
            contactsPhones = vacancy.contactsPhones,
            description = vacancy.description,
            employerName = vacancy.employerName,
            employmentName = vacancy.employmentName,
            experienceName = vacancy.experienceName,
            keySkillsNames = vacancy.keySkillsNames,
            salaryCurrency = vacancy.salaryCurrency,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            salaryGross = vacancy.salaryGross,
            scheduleName = vacancy.scheduleName
        )
    }

    fun map(vacancy: VacancyEntity): DetailVacancy {
        return DetailVacancy(
            id = vacancy.id,
            name = vacancy.name,
            areaName = vacancy.areaName,
            areaUrl = vacancy.areaUrl,
            contactsEmail = vacancy.contactsEmail,
            contactsName = vacancy.contactsName,
            contactsPhones = vacancy.contactsPhones,
            description = vacancy.description,
            employerName = vacancy.employerName,
            employmentName = vacancy.employmentName,
            experienceName = vacancy.experienceName,
            keySkillsNames = vacancy.keySkillsNames,
            salaryCurrency = vacancy.salaryCurrency,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            salaryGross = vacancy.salaryGross,
            scheduleName = vacancy.scheduleName
        )
    }

    private fun mapToModel(vacancy: VacancyEntity): VacancyModel {
        return VacancyModel(
            id = vacancy.id,
            vacancyName = vacancy.name,
            city = vacancy.areaName,
            salary = "Какая то зп",
            companyName = vacancy.employerName,
            logoUrls = null,
            details = null
        )
    }

    fun map(vacancies: Flow<List<VacancyEntity>>): Flow<List<VacancyModel>> =
        vacancies.map { list ->
            list.map { vacancyEntity ->
                mapToModel(vacancyEntity)
            }
        }

}
