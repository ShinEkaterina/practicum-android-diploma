package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl

val interactorModule = module {

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(favoriteRepository = get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
}
