package ru.practicum.android.diploma.data.dto.request

import ru.practicum.android.diploma.util.Constant.PER_PAGE_ITEMS

data class VacanciesSearchByNameRequest(
    val name: String,
    val page: Int,
    val amount: Int = PER_PAGE_ITEMS
)
