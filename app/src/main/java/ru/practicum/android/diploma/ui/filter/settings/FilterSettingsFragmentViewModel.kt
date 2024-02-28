package ru.practicum.android.diploma.ui.filter.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.FilterParametersState

class FilterSettingsFragmentViewModel(private val filtrationInteractor: FiltrationInteractor) : ViewModel() {
    private val _filterParametersState = MutableLiveData<FilterParametersState>()
    private var startFilterParameters = FilterParameters()
    val filterParametersState: LiveData<FilterParametersState> = _filterParametersState

    init {
        viewModelScope.launch {
            filtrationInteractor
                .getFilterParametersFromStorage()
                .collect { filterParams ->
                    startFilterParameters = filterParams
                }
        }
    }

    fun getFilterParameters() {
        _filterParametersState.postValue(FilterParametersState.Updating)
        viewModelScope.launch {
            filtrationInteractor
                .getFilterParametersFromStorage()
                .collect { filterParams ->
                    _filterParametersState.postValue(FilterParametersState.Content(filterParams))
                }
        }
    }

    fun setFilterParameters(filterParameters: FilterParameters) {
        _filterParametersState.postValue(FilterParametersState.Updating)
        viewModelScope.launch {
            filtrationInteractor
                .setFilterParametersToStorage(filterParameters)
                .collect { isSet ->
                    if (isSet) {
                        filtrationInteractor
                            .getFilterParametersFromStorage()
                            .collect { filterParams ->
                                _filterParametersState.postValue(FilterParametersState.Content(filterParams))
                            }
                    }
                }
        }
    }

    fun getStartFilterParameters(): FilterParameters {
        return this.startFilterParameters
    }

    fun isFilterParametersNotEmpty(filterParameters: FilterParameters): Boolean {
        return filterParameters.idCountry != null ||
            filterParameters.nameCountry != null ||
            filterParameters.idRegion != null ||
            filterParameters.idIndustry != null ||
            filterParameters.nameIndustry != null ||
            filterParameters.expectedSalary != null ||
            filterParameters.isDoNotShowWithoutSalary
    }

    fun isFilterParametersUpdated(filterParameters: FilterParameters): Boolean {
        return this.startFilterParameters != filterParameters
    }

    fun defaultFilterParameters(): FilterParameters {
        return FilterParameters(
            idCountry = null,
            nameCountry = null,
            idRegion = null,
            nameRegion = null,
            idIndustry = null,
            nameIndustry = null,
            expectedSalary = null,
            isDoNotShowWithoutSalary = false
        )
    }

    fun resetPlaceToJobParameters(filterParameters: FilterParameters) {
        var copyFilterParameters = filterParameters
        copyFilterParameters = copyFilterParameters.copy(idCountry = null)
        copyFilterParameters = copyFilterParameters.copy(nameCountry = null)
        copyFilterParameters = copyFilterParameters.copy(idRegion = null)
        copyFilterParameters = copyFilterParameters.copy(nameRegion = null)
        setFilterParameters(copyFilterParameters)
    }

    fun resetIndustryParameters(filterParameters: FilterParameters) {
        var copyFilterParameters = filterParameters
        copyFilterParameters = copyFilterParameters.copy(idIndustry = null)
        copyFilterParameters = copyFilterParameters.copy(nameIndustry = null)
        setFilterParameters(copyFilterParameters)
    }
}
