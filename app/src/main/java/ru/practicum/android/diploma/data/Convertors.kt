package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.data.dto.VacanciesSearchDto
import ru.practicum.android.diploma.data.dto.VacancyDetailedDto
import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacanciesModel
import ru.practicum.android.diploma.domain.model.VacancyModel
import java.util.Currency

class Convertors {

    companion object {

        fun convertorToVacanciesModel(
            vacanciesDto: VacanciesSearchDto
        ): VacanciesModel {
            val vacancies = vacanciesDto.vacancies
            val domainVacancies = arrayListOf<VacancyModel>()

            vacancies.forEach { vacancy ->
                domainVacancies.add(VacancyModel(
                    id = vacancy.id,
                    vacancyName = vacancy.name,
                    city = vacancy.address?.city ?: "",
                    salary = salaryToString(vacancy.salary),
                    companyName = vacancy.company.name,
                    logoUrls = if (vacancy.company.logoUrls == null) arrayListOf() else arrayListOf(vacancy.company.logoUrls.url90, vacancy.company.logoUrls.url240, vacancy.company.logoUrls.urlOriginal),
                ))
            }

            return VacanciesModel(vacanciesDto.pages, vacanciesDto.found, domainVacancies)
        }

        private fun salaryToString(
            salary: Salary?
        ): String {
            var salaryAsString = ""
            if (salary?.from != null || salary?.to != null) {
                if (salary.from != null) {
                    salaryAsString += "от" + salary.from + " "
                }
                if (salary.to != null) {
                    salaryAsString += "до" + salary.to + " "
                }
                if (salary.currency != null) {
                    salaryAsString += Currency.getInstance(salary.currency).symbol
                }
            }
            return salaryAsString
        }

        fun convertorToDetailVacancy(
            vacancy: VacancyDetailedDto
        ): DetailVacancy {
            return DetailVacancy(
                id = vacancy.id,
                areaName = vacancy.area?.name,
                areaUrl = vacancy.employer?.logoUrls?.logoUrl240,
                contactsEmail = vacancy.contacts?.email,
                contactsName = vacancy.contacts?.name,
                contactsPhones = vacancy.contacts?.phones.let { list -> list?.map { createPhone(it) } },
                comment = null,
                description = vacancy.description,
                employerName = vacancy.employer?.name,
                employmentName = vacancy.employment.name,
                experienceName = vacancy.experience.name,
                keySkillsNames = createKeySkills(vacancy.keySkills),
                name = vacancy.name,
                salaryCurrency = vacancy.salary?.currency,
                salaryFrom = vacancy.salary?.from,
                salaryGross = false,
                salaryTo = vacancy.salary?.to,
                scheduleId = "",
                scheduleName = vacancy.schedule?.name,
            )
        }

        private fun createPhone(
            phone: PhonesDto
        ): String {
            return "+${phone.country}" + " (${phone.city})" + " ${phone.number}"
        }
        private fun createKeySkills(
            keySkills: List<KeySkillsDto>?
        ): List<String?> {
            return keySkills?.map { it.name } ?: emptyList()
        }

    }

}
