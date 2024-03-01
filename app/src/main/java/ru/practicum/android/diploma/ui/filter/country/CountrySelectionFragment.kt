package ru.practicum.android.diploma.ui.filter.country

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountrySelectionBinding
import ru.practicum.android.diploma.domain.model.CountriesListState
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.ui.filter.region.RegionsAdapter

class CountrySelectionFragment : Fragment() {
    private val viewModel: CountrySelectionViewModel by viewModel()
    private var _binding: FragmentCountrySelectionBinding? = null
    private val binding: FragmentCountrySelectionBinding
        get() = _binding!!
    private var countriesAdapter: CountriesAdapter? = null
    private val countiesList = ArrayList<AreasModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountrySelectionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        binding.countryToolbars.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        initializationAdapter()

        viewModel.countriesListState.observe(viewLifecycleOwner) {
            countriesListState(it)
        }

        viewModel.getCountries()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun countriesListState(state: CountriesListState) {
        with(binding) {
            when (state) {
                is CountriesListState.Loading -> {
                    failedToGetListMessage(false)
                    progressBar.isVisible = true
                }

                is CountriesListState.Content -> {
                    failedToGetListMessage(false)
                    progressBar.isVisible = false
                    countiesList.clear()
                    countiesList.addAll(state.countries)
                    countriesAdapter?.notifyDataSetChanged()
                }

                is CountriesListState.Error -> {
                    failedToGetListMessage(true)
                }
            }
        }
    }

    private fun failedToGetListMessage(isVisible: Boolean) {
        binding.llNotListCountry.isVisible = isVisible
    }

    private fun initializationAdapter() {
        with(binding) {
            countriesAdapter = CountriesAdapter(countiesList) { country ->
                viewModel.setFilterParameters(country)
                findNavController().navigateUp()
            }

            rvRegions.adapter = countriesAdapter
        }
    }
}
