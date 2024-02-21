package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.DetailVacancyInteractor
import ru.practicum.android.diploma.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.domain.model.DetailVacancy

class VacancyViewModel(
    val vacancyInteractor: DetailVacancyInteractor,
    val favoriteInteractor: FavoriteInteractor,
) : ViewModel() {

    private val _vacancyState = MutableLiveData<VacancyState>()
    val vacancyState: LiveData<VacancyState> = _vacancyState
    private var vacancy: DetailVacancy? = null

    private fun renderState(state: VacancyState) {
        _vacancyState.postValue(state)
    }

    fun getVacancyDetail(id: String) {
        if (id.isNotEmpty()) {
            renderState(VacancyState.Loading)
            viewModelScope.launch {
                val isFavorite = favoriteInteractor.checkFavorite(id).firstOrNull() ?: false
                if (isFavorite) {
                    val vacFavorite = favoriteInteractor.getDetailVacancy(id).first()
                    if (vacFavorite != null) {
                        renderState(VacancyState.Content(vacFavorite))
                    }
                } else {
                    vacancyInteractor
                        .getDetailVacancy(id)
                        .collect { resource ->
                            processResult(resource)
                        }
                }
            }
        }
    }

    private fun processResult(result: Resource<DetailVacancy>) {
        if (result.data != null) {
            vacancy = result.data
            renderState(VacancyState.Content(vacancy!!))
        } else {
            renderState(VacancyState.Error)
        }
    }
}
