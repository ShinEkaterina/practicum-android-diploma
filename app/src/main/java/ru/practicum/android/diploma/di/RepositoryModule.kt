package ru.practicum.android.diploma.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.network.HeadHunterServiceApi
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.util.Constant

val repositoryModule = module {
    single<HeadHunterServiceApi> {
        Retrofit.Builder()
            .baseUrl(Constant.HH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeadHunterServiceApi::class.java)
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(networkClient = get())
    }
    single<NetworkClient> {
        RetrofitNetworkClient(get(), context = get())
    }
}
