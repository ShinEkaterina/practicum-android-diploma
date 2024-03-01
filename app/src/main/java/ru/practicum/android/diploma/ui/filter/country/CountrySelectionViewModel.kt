package ru.practicum.android.diploma.ui.filter.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.model.AreasListState
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.FilterParameters

class CountrySelectionViewModel(private val filtrationInteractor: FiltrationInteractor) : ViewModel() {
    private val _areasListState = MutableLiveData<AreasListState>()
    private var filterParameters = FilterParameters()
    val areasListState: LiveData<AreasListState> = _areasListState

    init {
        getFilterParameters()
    }

    fun getCountry() {
        viewModelScope.launch {
            _areasListState.postValue(AreasListState.Loading)
            filtrationInteractor
                .getAreas()
                .collect { result ->
                    when {
                        result.data != null -> {
//                            _areasListState.postValue(AreasListState.Content(result.data))
                        }
                        result.message != null -> {
                            _areasListState.postValue(AreasListState.Error)
                        }
                    }
                }
        }
    }

    fun setAreaFilters(area: AreasModel) {
        filterParameters = filterParameters.copy(idCountry = area.id)
        filterParameters = filterParameters.copy(nameCountry = area.name)
        viewModelScope.launch(Dispatchers.IO) {
            filtrationInteractor
                .setFilterParametersToStorage(filterParameters)
                .collect { isSet ->
                }
        }
    }

    private fun getFilterParameters() {
        viewModelScope.launch {
            filtrationInteractor
                .getFilterParametersFromStorage()
                .collect { filterParams ->
                    filterParameters = filterParams
                }
        }
    }
}
