package ru.practicum.android.diploma.domain.model

data class IndustriesModel(
    val id: String,
    val name: String,
    val industries: ArrayList<IndustriesModel>?
)
