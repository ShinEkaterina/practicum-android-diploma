package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.search.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository

val repositoryModule = module {

    single<SearchRepository> {
        SearchRepositoryImpl(
            networkClient = get()
        )
    }

}
