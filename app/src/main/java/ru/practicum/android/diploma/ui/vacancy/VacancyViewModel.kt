package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.Resource
import ru.practicum.android.diploma.domain.api.interactor.DetailVacancyInteractor
import ru.practicum.android.diploma.domain.api.interactor.FavoriteInteractor
import ru.practicum.android.diploma.domain.model.DetailVacancy

class VacancyViewModel(
    val vacancyInteractor: DetailVacancyInteractor,
    val favoriteInteractor: FavoriteInteractor,
) : ViewModel() {

    private val _vacancyState = MutableLiveData<VacancyState>()
    val vacancyState: LiveData<VacancyState> = _vacancyState
    private var vacancy: DetailVacancy? = null
    private var isFavoriteState: MutableLiveData<Boolean> = MutableLiveData(false)
    private var currentVacancy: DetailVacancy? = null

    fun getIsFavorite(): LiveData<Boolean> = isFavoriteState

    private fun renderState(state: VacancyState) {
        _vacancyState.postValue(state)
    }

    /*    fun getVacancyDetail(id: String):DetailVacancy {
            if (id.isNotEmpty()) {
                viewModelScope.launch {
                    val isFavorite = favoriteInteractor.checkFavorite(id).firstOrNull() ?: false
                    if (isFavorite) {
                        when (val vacancyFromNetwork = vacancyInteractor.getDetailVacancy(id).first()) {
                            is Resource.Success -> {
                                vacancyFromNetwork.data?.let {
                                    favoriteInteractor.update(it)
                                }
                                processResult(vacancyFromNetwork)
                            }

                            is Resource.Error -> {
                                val vacFavorite = favoriteInteractor.getDetailVacancy(id).first()
                                if (vacFavorite != null) {
                                    renderState(VacancyState.Content(vacFavorite))
                                }
                            }
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
        }*/

    fun showVacancyDetail(id: String) {
        if (id.isNotEmpty()) {
            renderState(VacancyState.Loading)
            viewModelScope.launch {
                val isFavorite = favoriteInteractor.checkFavorite(id).firstOrNull() ?: false
                if (isFavorite) {
                    isFavoriteState.postValue(isFavorite)
                    when (val vacancyFromNetwork = vacancyInteractor.getDetailVacancy(id).first()) {
                        is Resource.Success -> {
                            currentVacancy = vacancyFromNetwork.data
                            vacancyFromNetwork.data?.let {
                                favoriteInteractor.update(it)
                            }
                            processResult(vacancyFromNetwork)
                        }

                        is Resource.Error -> {
                            val vacFavorite = favoriteInteractor.getDetailVacancy(id).first()
                            currentVacancy = vacFavorite
                            if (vacFavorite != null) {
                                renderState(VacancyState.Content(vacFavorite))
                            }
                        }
                    }
                } else {
                    val vacancy = vacancyInteractor.getDetailVacancy(id).first()
                    currentVacancy = vacancy.data
                    processResult(vacancy)

                }
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            val favorite = isFavoriteState.value ?: false

            if (favorite) {
                currentVacancy?.let {
                    favoriteInteractor.delete(it.id)
                    isFavoriteState.postValue(it.isFavorite)
                }
            } else {
                currentVacancy?.let {
                    favoriteInteractor.add(it)
                    isFavoriteState.postValue(it.isFavorite)
                }
            }
            renderFavorite(!favorite)
        }
    }

    private fun renderFavorite(favorite: Boolean) {
        isFavoriteState.postValue(favorite)
    }

    private fun processResult(result: Resource<DetailVacancy>) {
        when (result) {
            is Resource.Success -> {
                vacancy = result.data
                renderState(VacancyState.Content(vacancy!!))
            }

            is Resource.Error -> {
                renderState(VacancyState.Error)

            }
        }
    }



}
