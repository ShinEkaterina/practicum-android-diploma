package ru.practicum.android.diploma.data.dto

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import ru.practicum.android.diploma.data.dto.field.ContactsDto
import ru.practicum.android.diploma.data.dto.field.EmployerDto
import ru.practicum.android.diploma.data.dto.field.KeySkillsDto
import ru.practicum.android.diploma.data.dto.field.PhonesDto
import ru.practicum.android.diploma.data.dto.field.SalaryDto
import ru.practicum.android.diploma.data.dto.respone.AreasResponse
import ru.practicum.android.diploma.data.dto.respone.IndustriesResponse
import ru.practicum.android.diploma.data.dto.respone.SearchResponse
import ru.practicum.android.diploma.data.dto.respone.VacancyDetailedResponse
import ru.practicum.android.diploma.domain.model.AreasModel
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

    private fun getContactsPhones(contacts: ContactsDto?): List<Pair<String, String>> =
        contacts?.phones?.map { createPhone(it) } ?: listOf()

    private fun getEmployerName(employer: EmployerDto?): String =
        employer?.name ?: ""

    private fun getEmployerLogoUrl(employer: EmployerDto?): String =
        employer?.logoUrls?.logoUrl240 ?: ""

    private fun getReadableNumber(number: Int?): String {
        if (number == null) return "0"

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
            scheduleName = response.schedule?.name ?: "",
            address = response.address?.raw ?: "",
            urlVacancy = response.urlVacancies
        )
    }

    private fun dtoToModel(vacancyDto: VacancyDetailedDto): VacancyModel {
        return VacancyModel(
            id = vacancyDto.id,
            vacancyName = vacancyDto.name,
            city = vacancyDto.area.name,
            salary = getSalaryString(vacancyDto.salary),
            companyName = getEmployerName(vacancyDto.employer),
            logoUrls = arrayListOf(vacancyDto.employer?.logoUrls?.logoUrl240),
            details = null,
        )
    }

    fun convertorToSearchList(
        searchList: SearchResponse
    ): VacanciesModel {
        return VacanciesModel(
            foundAsNumber = searchList.found ?: 0,
            foundAsString = getReadableNumber(searchList.found),
            pages = searchList.pages ?: 0,
//            currentPages = searchList.page,
            vacancies = searchList.items?.map { vacancyDto -> dtoToModel(vacancyDto) }
        )
    }

    fun convertorToVacanciesModel(
        vacanciesDto: SearchResponse
    ): VacanciesModel {
        val vacancies = vacanciesDto.items
        val domainVacancies = arrayListOf<VacancyModel>()

        vacancies?.forEach { vacancy ->
            domainVacancies.add(
                VacancyModel(
                    id = vacancy.id,
                    vacancyName = vacancy.name,
                    city = vacancy.area.name,
                    salary = getSalaryString(vacancy.salary),
                    companyName = getEmployerName(vacancy.employer),
                    logoUrls = if (vacancy.employer?.logoUrls == null) {
                        arrayListOf()
                    } else {
                        arrayListOf(
                            vacancy.employer.logoUrls.logoUrl90,
                            vacancy.employer.logoUrls.logoUrl240,
                            vacancy.employer.logoUrls.logoUrlOrigin
                        )
                    }
                )
            )
        }

        return VacanciesModel(
            vacanciesDto.pages ?: 0,
            vacanciesDto.found ?: 0,
            getReadableNumber(
                vacanciesDto.found
            ),
            domainVacancies
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

    fun converterAreasResponseToAreasModelList(response: List<AreasResponse>): Map<AreasModel, List<AreasModel>> {
        val mapAreasModel = mutableMapOf<AreasModel, List<AreasModel>>()
        response.forEach {
            mapAreasModel.put(
                AreasModel(
                    id = it.id,
                    parentId = null,
                    name = it.name,
                    areas = null
                ),
                createAreasList(it.areas)
            )
        }
        return mapAreasModel
    }

    private fun createPhone(phone: PhonesDto): Pair<String, String> {
        val phoneNumber = "+${phone.country}" +
            " (${phone.city})" +
            " ${phone.number?.dropLast(FOUR)}" +
            "-${phone.number?.drop(THREE)?.dropLast(TWO)}" +
            "-${phone.number?.drop(FIVE)}"
        return if (phone.comment != null) {
            Pair(phoneNumber, phone.comment)

        } else {
            Pair(phoneNumber, "")
        }
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

    private fun createAreasList(nestedAreasList: List<AreasModel>?): List<AreasModel> {
        val listAreasModel = mutableListOf<AreasModel>()
        nestedAreasList?.forEach {
            if (it.areas?.isEmpty() == true) {
                listAreasModel.add(
                    AreasModel(
                        id = it.id,
                        parentId = it.parentId,
                        name = it.name,
                        areas = null
                    )
                )
            } else {
                listAreasModel.addAll(createAreasList(it.areas))
            }
        }
        return listAreasModel.sortedBy { it.name }
    }

    companion object {
        const val NO_SALARY = "Зарплата не указана"
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
    }
}
