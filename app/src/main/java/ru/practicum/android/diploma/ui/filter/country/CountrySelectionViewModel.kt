package ru.practicum.android.diploma.ui.filter.country

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.CountriesListState
import ru.practicum.android.diploma.domain.model.FilterParameters

class CountrySelectionViewModel(private val filtrationInteractor: FiltrationInteractor) : ViewModel() {
    private val _countriesListState = MutableLiveData<CountriesListState>()
    private var filterParameters = FilterParameters()
    private var areasMap = mapOf<AreasModel, List<AreasModel>>()
    val countriesListState: LiveData<CountriesListState> = _countriesListState

    init {
        getFilterParameters()
    }

    fun getCountries() {
        viewModelScope.launch {
            _countriesListState.postValue(CountriesListState.Loading)
            filtrationInteractor
                .getAreas()
                .collect { result ->
                    when {
                        result.data != null -> {
                            areasMap = result.data
                            _countriesListState.postValue(CountriesListState.Content(getCountriesList()))
                        }

                        result.message != null -> {
                            _countriesListState.postValue(CountriesListState.Error)
                        }
                    }
                }
        }
    }

    fun setFilterParameters(country: AreasModel) {
        filterParameters = filterParameters.copy(idCountry = country.id)
        filterParameters = filterParameters.copy(nameCountry = country.name)
        filterParameters = filterParameters.copy(idRegion = null)
        filterParameters = filterParameters.copy(nameRegion = null)
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

    private fun getCountriesList(): List<AreasModel> {
        val listAreasModel = mutableListOf<AreasModel>()
        areasMap.forEach { (areasModel, _) ->
            listAreasModel.add(areasModel)
        }
        return listAreasModel
    }
}
