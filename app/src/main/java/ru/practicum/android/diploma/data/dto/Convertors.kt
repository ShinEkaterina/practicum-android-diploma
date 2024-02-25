package ru.practicum.android.diploma.data.dto

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import ru.practicum.android.diploma.data.dto.field.ContactsDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.data.dto.field.SalaryDto
import ru.practicum.android.diploma.data.dto.respone.IndustriesResponse
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.data.dto.respone.VacancyDetailedResponse
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.domain.model.IndustriesModel
import ru.practicum.android.diploma.domain.model.VacanciesModel
import ru.practicum.android.diploma.domain.model.VacancyModel
import java.util.Locale

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

    private fun getReadableNumber(number: Int): String {
        // Создаем экземпляр DecimalFormat, явно приводя NumberFormat к нужному типу
        val formatter = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols // Получаем текущие символы форматирования

        symbols.groupingSeparator = ' ' // Устанавливаем пробел в качестве разделителя групп
        formatter.decimalFormatSymbols =
            symbols // Применяем обновленные символы форматирования обратно к форматтеру

        return formatter.format(number)
    }

    /*    private fun getSalaryString(salaryDto: SalaryDto?): String {
            salaryDto?.let {
                val currency = Currency.toCurrency(it.currency ?: "")
                if (currency == Currency.NONE) {
                    Log.d("INFO", "Unknown currency")
                    return "Зарплата не указана"
                } else if (it.from != null && it.to != null) {
                    return "От ${getReadableNumber(it.from)} до ${getReadableNumber(it.to)} ${currency.symbol}"
                } else if (it.from == null && it.to != null) {
                    return "До ${getReadableNumber(it.to)} ${currency.symbol}"
                } else if (it.from != null) {
                    return "От ${getReadableNumber(it.from)} ${currency.symbol}"
                }
            }
            return "Зарплата не указана"

        }*/

/*    private fun getSalaryString(salaryDto: SalaryDto?): String {
        val currency = Currency.toCurrency(salaryDto?.currency ?: "")

        if (salaryDto == null || currency == Currency.NONE) {
            return NO_SALARY
        }

        val from = salaryDto.from?.let { "От ${getReadableNumber(it)}" } ?: ""
        val to = salaryDto.to?.let { "до ${getReadableNumber(it)}" } ?: ""
        val salaryRange =
            if (from.isNotBlank() || to.isNotBlank()) "$from $to".trim() else NO_SALARY

        return if (salaryRange != NO_SALARY) "$salaryRange ${currency.symbol}" else salaryRange
    }*/

    private fun getSalaryString(salaryDto: SalaryDto?): String {
        val currency = Currency.toCurrency(salaryDto?.currency ?: "")
        if (salaryDto == null || currency == Currency.NONE) return NO_SALARY

        val from = salaryDto.from?.let { "От ${getReadableNumber(it)}" }.orEmpty()
        val to = salaryDto.to?.let { "до ${getReadableNumber(it)}" }.orEmpty()
        val salaryRange = listOf(from, to).filter { it.isNotBlank() }.joinToString(" ").trim()

        return if (salaryRange.isNotEmpty()) "$salaryRange ${currency.symbol}" else NO_SALARY
    }

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
            salary = getSalaryString(response.salary),
            scheduleName = response.schedule?.name ?: ""
        )
    }

    private fun dtoToModel(vacancyDto: VacancyDetailedDto): VacancyModel {
        return VacancyModel(
            id = vacancyDto.id,
            vacancyName = vacancyDto.name,
            city = vacancyDto.area.name,
            salary = getSalaryString(vacancyDto.salary),
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

    fun converterIndustriesResponseToIndustriesModelList(response: List<IndustriesResponse>): List<IndustriesModel> {
        val listIndustriesModel = mutableListOf<IndustriesModel>()
        response.forEach {
            listIndustriesModel.add(
                IndustriesModel(
                    id = it.id,
                    name = it.name,
                    industries = null
                )
            )
            listIndustriesModel.addAll(createIndustriesList(it.industries))
        }
        return listIndustriesModel.sortedBy { it.name }
    }

    private fun createPhone(phone: PhonesDto): String {
        return "+${phone.country}" + " (${phone.city})" + " ${phone.number}"
    }

    private fun createKeySkills(keySkills: List<KeySkillsDto>): List<String> {
        //  return keySkills.map { it.name } ?: emptyList()
        return keySkills.map { it.name }.filter { it.isNotEmpty() }
    }

    private fun createIndustriesList(nestedIndustriesList: List<IndustriesModel>?): List<IndustriesModel> {
        val listIndustriesModel = mutableListOf<IndustriesModel>()
        nestedIndustriesList?.forEach {
            listIndustriesModel.add(
                IndustriesModel(
                    id = it.id,
                    name = it.name,
                    industries = null
                )
            )
        }
        return listIndustriesModel
    }

    companion object {
        const val NO_SALARY = "Зарплата не указана"
    }
}
