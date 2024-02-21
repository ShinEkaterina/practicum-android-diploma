package ru.practicum.android.diploma.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FiltrationInteractor
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.FilterParametersState

class FilterSettingsFragmentViewModel(private val filtrationInteractor: FiltrationInteractor): ViewModel() {
    private val _filterParametersState = MutableLiveData<FilterParametersState>()
    val filterParametersState: LiveData<FilterParametersState> = _filterParametersState

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
                            .collect{ filterParams ->
                                _filterParametersState.postValue(FilterParametersState.Content(filterParams))
                            }
                    } else {
                        Log.i("TEST_REY", "ошибка сохранения фильтров")
                    }
                }
        }
    }
}
