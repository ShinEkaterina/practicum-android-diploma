package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchResponse
import ru.practicum.android.diploma.domain.model.VacanciesModel

data class SearchSuccess(
    val vacancies: VacanciesModel
) : SearchResponse
