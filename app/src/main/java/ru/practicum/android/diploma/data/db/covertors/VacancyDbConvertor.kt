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
            contactsPhones = vacancy.contactsPhones.map { createPhoneString(it) },
            description = vacancy.description,
            employerName = vacancy.employerName,
            employmentName = vacancy.employmentName,
            experienceName = vacancy.experienceName,
            keySkillsNames = vacancy.keySkillsNames,
            salary = vacancy.salary,
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
            contactsPhones = vacancy.contactsPhones.map { createPhonePair(it) },
            description = vacancy.description,
            employerName = vacancy.employerName,
            employmentName = vacancy.employmentName,
            experienceName = vacancy.experienceName,
            keySkillsNames = vacancy.keySkillsNames,
            salary = vacancy.salary,
            scheduleName = vacancy.scheduleName
        )
    }

    private fun mapToModel(vacancy: VacancyEntity): VacancyModel {
        return VacancyModel(
            id = vacancy.id,
            vacancyName = vacancy.name,
            city = vacancy.areaName,
            salary = vacancy.salary,
            companyName = vacancy.employerName,
            logoUrls = arrayListOf(vacancy.areaUrl, "", ""),
            details = null
        )
    }

    fun map(vacancies: Flow<List<VacancyEntity>>): Flow<List<VacancyModel>> =
        vacancies.map { list ->
            list.map { vacancyEntity ->
                mapToModel(vacancyEntity)
            }
        }
    fun createPhoneString(pair: Pair<String, String>): String {
        return "${pair.first}\n" + pair.second
    }
    fun createPhonePair(string: String): Pair<String, String> {
        val list = string.split("\n")
        return Pair(list[0], list[1])
    }

}
