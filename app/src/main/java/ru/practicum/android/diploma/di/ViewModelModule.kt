package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favorites.FavoritesViewModel
import ru.practicum.android.diploma.ui.filter.industry.IndustrySelectionFragmentViewModel
import ru.practicum.android.diploma.ui.filter.place.ChoosingPlaceToJobViewModel
import ru.practicum.android.diploma.ui.filter.settings.FilterSettingsFragmentViewModel
import ru.practicum.android.diploma.ui.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.ui.similar.SimilarViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyViewModel

val viewModelModule = module {

    viewModel {
        VacancyViewModel(
            vacancyInteractor = get(),
            favoriteInteractor = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            favoriteInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchInteractor = get()
        )
    }

    viewModel {
        FilterSettingsFragmentViewModel(filtrationInteractor = get())
    }

    viewModel {
        IndustrySelectionFragmentViewModel(filtrationInteractor = get())
    }

    viewModel {
        SimilarViewModel(similarInteractor = get())
    }

    viewModel {
        ChoosingPlaceToJobViewModel(filtrationInteractor = get())
    }
}
