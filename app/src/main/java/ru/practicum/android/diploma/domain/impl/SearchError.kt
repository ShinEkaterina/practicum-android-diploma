package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchResponse

data class SearchError(
    val responseCode: Int
) : SearchResponse
