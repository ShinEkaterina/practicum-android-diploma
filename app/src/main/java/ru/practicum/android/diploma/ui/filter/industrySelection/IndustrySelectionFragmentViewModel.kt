package ru.practicum.android.diploma.ui.filter.industrySelection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.model.IndustriesListState

class IndustrySelectionFragmentViewModel(private val filtrationInteractor: FiltrationInteractor) : ViewModel() {
    private val _industriesListState = MutableLiveData<IndustriesListState>()
    val industriesListState: LiveData<IndustriesListState> = _industriesListState

    fun getIndustries() {
        viewModelScope.launch {
            filtrationInteractor
                .getIndustries()
                .collect { result ->
                    when {
                        result.data != null -> {
                            Log.i("TEST_REY", result.data.toString())
                        }
                        result.message != null -> {
                            Log.i("TEST_REY", result.message.toString())
                        }
                    }
                }
        }
    }
}
