package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites_table")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val areaName: String,
    val areaUrl: String,
    val contactsEmail: String,
    val contactsName: String,
    val contactsPhones: List<String>,
    val description: String,
    val employerName: String,
    val employmentName: String,
    val experienceName: String,
    val keySkillsNames: List<String>,
    val salary: String = ",",
    /*    val salaryCurrency: String,
        val salaryFrom: Int?,
        val salaryTo: Int?,
        val salaryGross: Boolean,*/
    val scheduleName: String
)
