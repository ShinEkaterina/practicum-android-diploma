package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.interactor.DetailVacancyInteractor
import ru.practicum.android.diploma.domain.api.interactor.FavoriteInteractor
import ru.practicum.android.diploma.domain.api.interactor.FiltrationInteractor
import ru.practicum.android.diploma.domain.api.interactor.SearchInteractor
import ru.practicum.android.diploma.domain.api.interactor.SimilarInteractor
import ru.practicum.android.diploma.domain.impl.DetailVacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.FiltrationInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.SimilarInteractorImpl

val interactorModule = module {

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(
            searchRepository = get()
        )
    }
    factory<DetailVacancyInteractor> {
        DetailVacancyInteractorImpl(repository = get(), externalNavigator = get())
    }

    factory<FiltrationInteractor> {
        FiltrationInteractorImpl(repository = get())
    }
    factory<SimilarInteractor> {
        SimilarInteractorImpl(repository = get())
    }

}
