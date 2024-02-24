package ru.practicum.android.diploma.ui.filter.industrySelection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.IndustriesListState
import ru.practicum.android.diploma.domain.model.IndustriesModel

class IndustrySelectionFragmentViewModel(private val filtrationInteractor: FiltrationInteractor) : ViewModel() {
    private val _industriesListState = MutableLiveData<IndustriesListState>()
    private var filterParameters = FilterParameters()
    val industriesListState: LiveData<IndustriesListState> = _industriesListState

    init {
        getFilterParameters()
    }

    fun getIndustries() {
        viewModelScope.launch {
            _industriesListState.postValue(IndustriesListState.Loading)
            filtrationInteractor
                .getIndustries()
                .collect { result ->
                    when {
                        result.data != null -> {
                            _industriesListState.postValue(IndustriesListState.Content(result.data))
                        }

                        result.message != null -> {
                            _industriesListState.postValue(IndustriesListState.Error)
                        }
                    }
                }
        }
    }

    fun setFilterParameters(industry: IndustriesModel) {
        filterParameters = filterParameters.copy(idIndustry = industry.id)
        filterParameters = filterParameters.copy(nameIndustry = industry.name)
        viewModelScope.launch(Dispatchers.IO) {
            filtrationInteractor
                .setFilterParametersToStorage(filterParameters)
                .collect { isSet ->
                    Log.i("TEST_REY", "фильтры сохранены?: $isSet")
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
