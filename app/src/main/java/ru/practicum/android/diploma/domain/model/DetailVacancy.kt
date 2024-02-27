package ru.practicum.android.diploma.domain.model

data class DetailVacancy(
    val id: String,
    val name: String,
    val areaName: String,
    val areaUrl: String,
    val contactsEmail: String = "",
    val contactsName: String = "",
    val contactsPhones: List<Pair<String, String>> = listOf(),
    val description: String,
    val employerName: String = "",
    val employmentName: String = "",
    val experienceName: String = "",
    val keySkillsNames: List<String> = listOf(),
    val salary: String = "",
    // val salaryCurrency: String = "",
    //  val salaryFrom: Int?,
    // val salaryTo: Int?,
    //  val salaryGross: Boolean = false,
    val scheduleName: String = "",
    val isFavorite: Boolean = false

)
