package ru.practicum.android.diploma.domain.model

data class FilterParameters(
    val idCountry: String? = null,
    val nameCountry: String? = null,
    val idRegion: String? = null,
    val nameRegion: String? = null,
    val idIndustry: String? = null,
    val nameIndustry: String? = null,
    val expectedSalary: Int? = null,
    val isDoNotShowWithoutSalary: Boolean = false
)
