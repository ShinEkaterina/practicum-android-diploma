package ru.practicum.android.diploma.domain.model

data class AreasModel(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: ArrayList<AreasModel>
)
