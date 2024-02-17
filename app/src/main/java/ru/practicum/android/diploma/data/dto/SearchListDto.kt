package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.network.Response

data class SearchListDto(
    @SerializedName("items")
    val results: ArrayList<VacancyDto>,
    val page: Int?,
    val pages: Int?,
    val found: Int?
) : Response()
