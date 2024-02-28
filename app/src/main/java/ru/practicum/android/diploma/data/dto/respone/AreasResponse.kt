package ru.practicum.android.diploma.data.dto.respone

import ru.practicum.android.diploma.domain.model.AreasModel

data class AreasResponse(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<AreasModel>?
)
