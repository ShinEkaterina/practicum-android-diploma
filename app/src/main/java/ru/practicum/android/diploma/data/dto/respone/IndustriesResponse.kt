package ru.practicum.android.diploma.data.dto.respone

import ru.practicum.android.diploma.domain.model.IndustriesModel

data class IndustriesResponse(
    val id: String,
    val name: String,
    val industries: List<IndustriesModel>?
)
