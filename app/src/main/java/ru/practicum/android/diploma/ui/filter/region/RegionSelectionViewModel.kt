package ru.practicum.android.diploma.ui.filter.region

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.RegionsListState

class RegionSelectionViewModel(private val filtrationInteractor: FiltrationInteractor) : ViewModel() {
    private val _regionsListState = MutableLiveData<RegionsListState>()
    private var filterParameters = FilterParameters()
    private var areasMap = mapOf<AreasModel, List<AreasModel>>()
    private val originalRegionsList = ArrayList<AreasModel>()
    private val filteredRegionsList = ArrayList<AreasModel>()
    val regionsListState: LiveData<RegionsListState> = _regionsListState

    init {
        getFilterParameters()
    }

    fun getRegions() {
        viewModelScope.launch {
            _regionsListState.postValue(RegionsListState.Loading)
            filtrationInteractor
                .getAreas()
                .collect { result ->
                    when {
                        result.data != null -> {
                            areasMap = result.data
                            _regionsListState.postValue(RegionsListState.Content(getRegionsList()))
                            originalRegionsList.addAll(getRegionsList())
                        }

                        result.message != null -> {
                            _regionsListState.postValue(RegionsListState.Error)
                        }
                    }
                }
        }
    }

    fun setFilterParameters(region: AreasModel) {
        filterParameters = filterParameters.copy(idCountry = region.parentId)
        filterParameters = filterParameters.copy(nameCountry = getCountryName(region.parentId))
        filterParameters = filterParameters.copy(idRegion = region.id)
        filterParameters = filterParameters.copy(nameRegion = region.name)
        viewModelScope.launch(Dispatchers.IO) {
            filtrationInteractor
                .setFilterParametersToStorage(filterParameters)
                .collect { isSet ->
                    Log.i("TEST_REY", "фильтры сохранены?: $isSet")
                }
        }
    }

    fun filter(searchQuery: String?) {
        filteredRegionsList.clear()
        if (searchQuery.isNullOrEmpty()) {
            _regionsListState.postValue(RegionsListState.Content(originalRegionsList))
        } else {
            for (item in originalRegionsList) {
                if (item.name.contains(searchQuery, true)) {
                    filteredRegionsList.add(item)
                }
            }
            _regionsListState.postValue(RegionsListState.Content(filteredRegionsList))
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

    private fun getRegionsList(): List<AreasModel> {
        val listAreasModel = mutableListOf<AreasModel>()
        if (filterParameters.nameCountry == null) {
            areasMap.forEach { (_, areasModels) ->
                listAreasModel.addAll(areasModels)
            }
        } else {
            areasMap.forEach { (areasModel, areasModels) ->
                if (areasModel.name == filterParameters.nameCountry) {
                    listAreasModel.addAll(areasModels)
                }
            }
        }
        return listAreasModel.sortedBy { it.name }
    }

    private fun getCountryName(idCountry: String?): String? {
        areasMap.forEach { (areasModel, _) ->
            if (areasModel.id == idCountry) {
                return areasModel.name
            }
        }
        return null
    }
}
