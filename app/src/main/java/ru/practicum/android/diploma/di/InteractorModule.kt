package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.interactor.DetailVacancyInteractor
import ru.practicum.android.diploma.domain.api.interactor.FavoriteInteractor
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.api.interactor.SearchInteractor
import ru.practicum.android.diploma.domain.impl.DetailVacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.FiltrationInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl

val interactorModule = module {

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
    factory<DetailVacancyInteractor> {
        DetailVacancyInteractorImpl(repository = get())
    }

    factory<FiltrationInteractor> {
        FiltrationInteractorImpl(repository = get())
    }
}
