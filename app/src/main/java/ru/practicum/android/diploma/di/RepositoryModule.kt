package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.Convertors
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.network.HeadHunterServiceApi
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.search.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository

val repositoryModule = module {

    single<SearchRepository> {
        SearchRepositoryImpl(
            networkClient = get(),
            context = androidContext()
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            headHunterService = get(),
            context = get()
        )
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeadHunterServiceApi::class.java)
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(
            networkClient = get(),
            context = get()
        )
    }

}
