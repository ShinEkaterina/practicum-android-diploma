package ru.practicum.android.diploma.data.dto

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.util.Log
import ru.practicum.android.diploma.data.dto.field.ContactsDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.data.dto.field.SalaryDto
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.data.dto.respone.VacancyDetailedResponse
import ru.practicum.android.diploma.domain.model.DetailVacancy
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


    private fun getSalaryString(salaryDto: SalaryDto?): String {
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

    private fun createPhone(phone: PhonesDto): String {
        return "+${phone.country}" + " (${phone.city})" + " ${phone.number}"
    }

    private fun createKeySkills(keySkills: List<KeySkillsDto>): List<String> {
        //  return keySkills.map { it.name } ?: emptyList()
        return keySkills.map { it.name }.filter { it.isNotEmpty() }
    }
}
