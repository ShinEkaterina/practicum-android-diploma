package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.field.ContactsDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.data.dto.respone.VacancyDetailedResponse
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

    fun responseToDetailModel(response: VacancyDetailedResponse): DetailVacancy {
        return DetailVacancy(
            id = response.id,
            areaName = response.area.name,
            areaUrl = getEmployerLogoUrl(response.employer),
            contactsEmail = getContactsEmail(response.contacts),
            contactsName = getContactsName(response.contacts),
            contactsPhones = getContactsPhones(response.contacts),
            description = response.description,
            employerName = getEmployerName(response.employer),
            employmentName = response.employment?.name ?: "",
            experienceName = response.experience.name ?: "",
            keySkillsNames = createKeySkills(response.keySkills),
            name = response.name,
            salaryCurrency = response.salary?.currency ?: "",
            salaryFrom = response.salary?.from,
            salaryTo = response.salary?.to,
            salaryGross = false,
            scheduleName = response.schedule?.name ?: ""
        )
    }
    fun dtoToDetailModel(vacancyDto: VacancyDetailedDto): DetailVacancy {
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

    private fun dtoToModel(vacancyDto: VacancyDetailedDto): VacancyModel {
        return VacancyModel(
            id = vacancyDto.id,
            vacancyName = vacancyDto.name,
            city = vacancyDto.area.name,
            salary = "100",
            companyName = null,
            logoUrls = vacancyDto.employer?.logoUrls?.logoUrl240,
            details = null,
        )
    }

    fun convertorToSearchList(searchList: SearchResponse): VacanciesModel {
        return VacanciesModel(
            found = searchList.found,
            maxPages = searchList.pages,
            currentPages = searchList.page,
            listVacancy = searchList.items?.map { vacancyDto -> dtoToModel(vacancyDto) },
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
