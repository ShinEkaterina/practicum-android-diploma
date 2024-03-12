package ru.practicum.android.diploma.data.dto.respone

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.field.AreaDto
import ru.practicum.android.diploma.data.dto.field.LogoUrlDto

data class EmployerRespone(
    val id: String,
    val name: String,
    val area: AreaDto?,
    val description: String,
    @SerializedName("site_url")
    val site: String,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrlDto,
    @SerializedName("open_vacancies")
    val openVacancies: Int?,
) : Response()
