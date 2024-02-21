package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.field.ContactsDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacanciesModel
import ru.practicum.android.diploma.domain.model.VacancyModel

class Convertors {

    private fun getContactsEmail(contacts: ContactsDto?): String =
        contacts?.email ?: ""

    private fun getContactsName(contacts: ContactsDto?): String =
        contacts?.name ?: ""

    private fun getContactsPhones(contacts: ContactsDto?): List<String> =
        contacts?.phones?.map { createPhone(it) } ?: listOf()

    private fun getEmployerName(employer: EmployerDto?): String =
        employer?.name ?: ""

    private fun getEmployerLogoUrl(employer: EmployerDto?): String =
        employer?.logoUrls?.logoUrl240 ?: ""

    fun convertorToDetailVacancy(vacancyDto: VacancyDetailedDto): DetailVacancy {
        return DetailVacancy(
            id = vacancyDto.id,
            areaName = vacancyDto.area.name,
            areaUrl = getEmployerLogoUrl(vacancyDto.employer),
            contactsEmail = getContactsEmail(vacancyDto.contacts),
            contactsName = getContactsName(vacancyDto.contacts),
            contactsPhones = getContactsPhones(vacancyDto.contacts),
            description = vacancyDto.description,
            employerName = getEmployerName(vacancyDto.employer),
            employmentName = vacancyDto.employment?.name ?: "",
            experienceName = vacancyDto.experience.name ?: "",
            keySkillsNames = createKeySkills(vacancyDto.keySkills),
            name = vacancyDto.name,
            salaryCurrency = vacancyDto.salary?.currency ?: "",
            salaryFrom = vacancyDto.salary?.from,
            salaryTo = vacancyDto.salary?.to,
            salaryGross = false,
            scheduleName = vacancyDto.schedule?.name ?: ""
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
