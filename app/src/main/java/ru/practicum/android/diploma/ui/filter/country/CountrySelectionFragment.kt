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
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.AreasListState

class CountrySelectionFragment() :Fragment() {
    private val viewModel: CountrySelectionViewModel by viewModel()
    private var _binding: FragmentCountrySelectionBinding? = null
    private val binding: FragmentCountrySelectionBinding
        get() = _binding!!
    private var areasAdapter: AreasAdapter? = null
    private val areasList = ArrayList<AreasModel>()

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
        viewModel.areasListState.observe(viewLifecycleOwner) {
            areasListState(it)
        }
        viewModel.getCountry()
        binding.countryToolbars.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        areasAdapter = AreasAdapter(areasList) { area ->
            viewModel.setAreaFilters(area)
            findNavController().navigateUp()
        }
        binding.industryRecyclerView.adapter = areasAdapter
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun areasListState(state: AreasListState) {
        when (state) {
            is AreasListState.Loading -> {
                failedToGetListMessage(false)
            }
            is AreasListState.Content -> {
                failedToGetListMessage(false)
                areasList.addAll(state.areas)
                areasAdapter?.notifyDataSetChanged()
            }
            is AreasListState.Error -> {
                failedToGetListMessage(true)
            }
        }
    }
    private fun failedToGetListMessage(isVisible: Boolean) {
        if (isVisible) {
            with(binding) {
                serverErrorLayout.isVisible = true
                tvError.isVisible = true
                binding.industryRegionButton.isVisible = false
            }
        } else {
            with(binding) {
                serverErrorLayout.isVisible = false
                tvError.isVisible = false
                binding.industryRegionButton.isVisible = true
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
