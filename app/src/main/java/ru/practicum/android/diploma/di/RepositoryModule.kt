package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.covertors.VacancyDbConvertor
import ru.practicum.android.diploma.data.impl.FavoriteVcRepositoryImpl
import ru.practicum.android.diploma.data.impl.FiltrationRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.network.HeadHunterServiceApi
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.api.repository.FavoriteVcRepository
import ru.practicum.android.diploma.domain.api.repository.FiltrationRepository
import ru.practicum.android.diploma.domain.api.repository.VacanciesRepository
import ru.practicum.android.diploma.util.Constant

val repositoryModule = module {

    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    single<HeadHunterServiceApi> {
        Retrofit.Builder()
            .baseUrl(Constant.HH_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeadHunterServiceApi::class.java)
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(
            networkClient = get(),
            application = get()
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            headHunterService = get(),
            context = get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences(
                Constant.FILTRATION_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    factory<FiltrationRepository> {
        FiltrationRepositoryImpl(
            networkClient = get(),
            filterStorage = get()
        )
    }

    single<FavoriteVcRepository> {
        FavoriteVcRepositoryImpl(
            appDatabase = get(),
            converter = get()
        )
    }

    single {
        VacancyDbConvertor()
    }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database_2.db"
        ).addMigrations().build()
    }

}
