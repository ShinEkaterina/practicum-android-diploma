package ru.practicum.android.diploma.data

import android.content.Context
import android.icu.text.DecimalFormat
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.data.dto.VacanciesSearchDtoResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailedDto
import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.VacanciesModel
import ru.practicum.android.diploma.domain.model.VacancyModel

class Convertors(
    private val context: Context
) {

    fun convertorToVacanciesModel(
        vacanciesDto: VacanciesSearchDtoResponse
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

        return VacanciesModel(vacanciesDto.pages, vacanciesDto.found, formatNumber(vacanciesDto.found), domainVacancies)
    }

    private fun formatNumber(
        x: Long
    ): String {
        val formatter = android.icu.text.NumberFormat.getNumberInstance() as DecimalFormat

        val symbols = formatter.decimalFormatSymbols
        symbols.groupingSeparator = ' '

        formatter.decimalFormatSymbols = symbols

        return formatter.format(x)
    }

    private fun salaryToString(
        salary: Salary?
    ): String {
        var salaryAsString = ""
        if (salary?.from != null || salary?.to != null) {
            if (salary.from != null) {
                salaryAsString += context.getString(R.string.salary_from) + " " + formatNumber(salary.from.toLong()) + " "
            }
            if (salary.to != null) {
                salaryAsString += context.getString(R.string.salary_to) + " " + formatNumber(salary.to.toLong()) + " "
            }
            if (salary.currency != null) {
                salaryAsString += when (salary.currency) {
                    "RUR", "RUB" -> context.getString(R.string.currency_rur_rub)
                    "BYR", "BIN" -> context.getString(R.string.currency_byr_bin)
                    "USD" -> context.getString(R.string.currency_usd)
                    "EUR" -> context.getString(R.string.currency_eur)
                    "KZT" -> context.getString(R.string.currency_kzt)
                    "UAH" -> context.getString(R.string.currency_uah)
                    "AZN" -> context.getString(R.string.currency_azn)
                    "UZS" -> context.getString(R.string.currency_uzs)
                    "GEL" -> context.getString(R.string.currency_gel)
                    "KGT" -> context.getString(R.string.currency_kgt)
                    else -> context.getString(R.string.currency_unknown)
                }
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