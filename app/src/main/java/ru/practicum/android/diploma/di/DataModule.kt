package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FilterStorageImpl
import ru.practicum.android.diploma.data.storage.FilterStorage

val dataModule = module {

    single { Gson() }

    single<FilterStorage> {
        FilterStorageImpl(
            sharedPrefs = get(),
            gson = get()
        )
    }

}
