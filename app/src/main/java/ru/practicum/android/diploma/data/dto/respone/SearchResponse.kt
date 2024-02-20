package ru.practicum.android.diploma.data.dto.respone

import ru.practicum.android.diploma.data.dto.VacancyDetailedDto

data class SearchResponse(
    val found: Int?,
    val page: Int?,
    val pages: Int?,
    val items: List<VacancyDetailedDto>?,
) : Response()
