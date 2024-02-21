package ru.practicum.android.diploma.domain.model

data class FilterParameters(
    val idCountry: String?,
    val nameCountry: String?,
    val idRegion: String?,
    val nameRegion: String?,
    val idIndustry: String?,
    val nameIndustry: String?,
    val expectedSalary: Int?,
    val isDoNotShowWithoutSalary: Boolean
)
