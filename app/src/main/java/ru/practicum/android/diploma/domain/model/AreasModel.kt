package ru.practicum.android.diploma.domain.model

import com.google.gson.annotations.SerializedName

data class AreasModel(
    val id: String,
    @SerializedName("parent_id")
    val parentId: String?,
    val name: String,
    val areas: List<AreasModel>?
)
