package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.domain.model.FilterParameters
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.domain.model.FilterParametersState

class FilterSettingsFragment : Fragment() {
    private val viewModel: FilterSettingsFragmentViewModel by viewModel()
    private var _binding: FragmentFilterSettingsBinding? = null
    private val binding: FragmentFilterSettingsBinding
        get() = _binding!!
    private var filterParameters: FilterParameters? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterSettingsBinding.inflate(
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
        viewModel.filterParametersState.observe(viewLifecycleOwner) {
            filterParametersState(it)
        }

        viewModel.getFilterParameters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        // тест
//        viewModel.setFilterParameters(
//            FilterParameters(
//                idCountry = "13",
//                nameCountry = "Россия",
//                idRegion = "1620",
//                nameRegion = "Республика Марий Эл",
//                idIndustry = "5.461",
//                nameIndustry = "Авиаперевозки",
//                expectedSalary = 100000,
//                isDoNotShowWithoutSalary = true
//            )
//        )
        viewModel.setFilterParameters(
            FilterParameters(
                idCountry = null,
                nameCountry = null,
                idRegion = null,
                nameRegion = null,
                idIndustry = null,
                nameIndustry = null,
                expectedSalary = null,
                isDoNotShowWithoutSalary = false
            )
        )
    }

    private fun filterParametersState(state: FilterParametersState) {
        when (state) {
            is FilterParametersState.Content -> updateFilterSettingsScreen(state.filterParameters)
            is FilterParametersState.Updating -> {}
        }
    }

    private fun updateFilterSettingsScreen(newFilterParameters: FilterParameters) {
        filterParameters = newFilterParameters
        Log.i("TEST_REY", filterParameters.toString())
    }
}
