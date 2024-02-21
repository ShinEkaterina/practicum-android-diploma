package ru.practicum.android.diploma.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.storage.FilterStorage
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.util.Constant.FILTRATION_KEY

class FilterStorageImpl(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
): FilterStorage {
    override fun getFilterParameters(): Flow<FilterParameters> = flow {
        val json = sharedPrefs.getString(FILTRATION_KEY, null)
        if (json == null) {
            emit(FilterParameters(
                idCountry = null,
                nameCountry = null,
                idRegion = null,
                nameRegion = null,
                idIndustry = null,
                nameIndustry = null,
                expectedSalary = null,
                isDoNotShowWithoutSalary = false
            ))
        } else {
            val filterType = object : TypeToken<FilterParameters>() {}.type
            emit(gson.fromJson(
                json,
                filterType
            ))
        }
    }

    override fun setFilterParameters(filterParameters: FilterParameters): Flow<Boolean> = flow {
        removeFilterParameters()
        val json = gson.toJson(filterParameters)
        sharedPrefs.edit()
            .putString(
                FILTRATION_KEY,
                json
            )
            .apply()
        getFilterParameters()
            .collect { filterParams ->
                if (filterParameters == filterParams) {
                    emit(true)
                } else {
                    emit(false)
                }
            }
    }

    private fun removeFilterParameters() {
        sharedPrefs.edit()
            .remove(FILTRATION_KEY)
            .apply()
    }
}
