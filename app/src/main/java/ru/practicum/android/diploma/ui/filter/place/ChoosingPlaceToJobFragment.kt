package ru.practicum.android.diploma.ui.filter.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentPlaceToWorkBinding
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.FilterParametersState

class ChoosingPlaceToJobFragment : Fragment() {
    private val viewModel: ChoosingPlaceToJobViewModel by viewModel()
    private var _binding: FragmentPlaceToWorkBinding? = null
    private val binding: FragmentPlaceToWorkBinding
        get() = _binding!!
    private var filterParameters = FilterParameters()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceToWorkBinding.inflate(
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
        initializationButtonsListener()
        viewModel.filterParametersState.observe(viewLifecycleOwner) {
            filterParametersState(it)
        }

        viewModel.getFilterParameters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterParametersState(state: FilterParametersState) {
        when (state) {
            is FilterParametersState.Content -> updateChoosingPlaceToJobScreen(state.filterParameters)
            is FilterParametersState.Updating -> {}
        }
    }

    private fun updateChoosingPlaceToJobScreen(newFilterParameters: FilterParameters) {
        filterParameters = newFilterParameters
        with(binding) {
            selectedButton.isVisible = filterParameters.nameRegion != null
            if (filterParameters.nameCountry != null) {
                etCountry.setText(filterParameters.nameCountry)
                tiCountry.setEndIconDrawable(R.drawable.ic_clear)
            } else {
                etCountry.setText("")
                tiCountry.setEndIconDrawable(R.drawable.ic_item_arrow)
            }

            if (filterParameters.nameRegion != null) {
                etRegion.setText(filterParameters.nameRegion)
                tiRegion.setEndIconDrawable(R.drawable.ic_clear)
            } else {
                etRegion.setText("")
                tiRegion.setEndIconDrawable(R.drawable.ic_item_arrow)
            }
        }
    }

    private fun initializationButtonsListener() {
        with(binding) {
            backPressedButtonsListener()
            tiCountry.setEndIconOnClickListener {
                if (filterParameters.nameCountry != null) {
                    etCountry.setText("")
                    tiCountry.setEndIconDrawable(R.drawable.ic_item_arrow)
                    filterParameters = filterParameters.copy(idCountry = null)
                    filterParameters = filterParameters.copy(nameCountry = null)
                } else {
                    findNavController().navigate(
                        R.id.action_choosingPlaceToJobFragment_to_countrySelectionFragment
                    )
                }
            }

            tiRegion.setEndIconOnClickListener {
                if (filterParameters.nameRegion != null) {
                    etRegion.setText("")
                    tiRegion.setEndIconDrawable(R.drawable.ic_item_arrow)
                    resetRegionFilterParameters()
                } else {
                    findNavController().navigate(
                        R.id.action_choosingPlaceToJobFragment_to_regionSelectionFragment
                    )
                }
            }

            selectedButton.setOnClickListener {
                if (filterParameters.idCountry == null) {
                    resetRegionFilterParameters()
                }
                viewModel.setFilterParameters(filterParameters)
                findNavController().navigateUp()
            }
        }
    }

    private fun backPressedButtonsListener() {
        with(binding) {
            placeToWorkToolbar.setNavigationOnClickListener {
                viewModel.setFilterParameters(viewModel.getStartFilterParameters())
                findNavController().navigateUp()
            }

            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        viewModel.setFilterParameters(viewModel.getStartFilterParameters())
                        findNavController().navigateUp()
                    }
                }
            )
        }
    }

    private fun resetRegionFilterParameters() {
        filterParameters = filterParameters.copy(idRegion = null)
        filterParameters = filterParameters.copy(nameRegion = null)
    }

    private fun resetCountryFilterParameters() {
        filterParameters = filterParameters.copy(idCountry = null)
        filterParameters = filterParameters.copy(nameCountry = null)
    }
}
