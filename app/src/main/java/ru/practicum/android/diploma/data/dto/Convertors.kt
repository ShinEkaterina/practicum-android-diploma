package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacanciesModel
import ru.practicum.android.diploma.domain.model.VacancyModel

class Convertors {
    fun convertorToDetailVacancy(vacancyDto: VacancyDetailedDto): DetailVacancy {
        return DetailVacancy(
            id = vacancyDto.id,
            areaName = vacancyDto.area.name,
            areaUrl = vacancyDto.employer?.logoUrls?.logoUrl240 ?: "",
            contactsEmail = vacancyDto.contacts?.email ?: "",
            contactsName = vacancyDto.contacts?.name ?: "",
            contactsPhones = vacancyDto.contacts?.phones?.let { list -> list?.map { createPhone(it) } }
                ?: listOf(),
            description = vacancyDto.description,
            employerName = vacancyDto.employer?.name ?: "",
            employmentName = vacancyDto.employment?.name ?: "",
            experienceName = vacancyDto.experience.name ?: "",
            keySkillsNames = createKeySkills(vacancyDto.keySkills),
            name = vacancyDto.name,
            salaryCurrency = vacancyDto.salary?.currency ?: "",
            salaryFrom = vacancyDto.salary?.from,
            salaryGross = false,
            salaryTo = vacancyDto.salary?.to,
            scheduleName = vacancyDto.schedule?.name ?: "",
        )
    }

    private fun convertorToVacancy(vacancy: VacancyDetailedDto): VacancyModel {
        return VacancyModel(
            id = vacancy.id,
            vacancyName = vacancy.name,
            city = vacancy.area.name,
            salary = "100",
            companyName = null,
            logoUrls = vacancy.employer?.logoUrls?.logoUrl240,
            details = null,
        )
    }

    fun convertorToSearchList(searchList: SearchResponse): VacanciesModel {
        return VacanciesModel(
            found = searchList.found,
            maxPages = searchList.pages,
            currentPages = searchList.page,
            listVacancy = searchList.items?.map { vacancyDto -> convertorToVacancy(vacancyDto) },
        )
    }

    private fun createPhone(phone: PhonesDto): String {
        return "+${phone.country}" + " (${phone.city})" + " ${phone.number}"
    }

    private fun createKeySkills(keySkills: List<KeySkillsDto>): List<String> {
        //  return keySkills.map { it.name } ?: emptyList()
        return keySkills.mapNotNull { it.name }.filter { it.isNotEmpty() } ?: emptyList()
    }
}
