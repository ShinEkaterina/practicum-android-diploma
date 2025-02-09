package ru.practicum.android.diploma.ui.filter.industry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentIndustrySelectionBinding
import ru.practicum.android.diploma.domain.model.IndustriesListState
import ru.practicum.android.diploma.domain.model.IndustriesModel

class IndustrySelectionFragment : Fragment() {
    private val viewModel: IndustrySelectionFragmentViewModel by viewModel()
    private var _binding: FragmentIndustrySelectionBinding? = null
    private val binding: FragmentIndustrySelectionBinding
        get() = _binding!!
    private var industriesAdapter: IndustriesAdapter? = null
    private val industriesList = ArrayList<IndustriesModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustrySelectionBinding.inflate(
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
        initializationAdapter()
        viewModel.industriesListState.observe(viewLifecycleOwner) {
            industriesListState(it)
        }

        viewModel.getIndustries()
        binding.industrySelectionToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializationAdapter() {
        industriesAdapter = IndustriesAdapter(industriesList) { industry ->
            viewModel.setFilterParameters(industry)
            findNavController().navigateUp()
        }

        binding.rvIndustries.adapter = industriesAdapter
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun industriesListState(state: IndustriesListState) {
        when (state) {
            is IndustriesListState.Loading -> {
                failedToGetListMessage(false)
            }
            is IndustriesListState.Content -> {
                failedToGetListMessage(false)
                industriesList.addAll(state.industries)
                industriesAdapter?.notifyDataSetChanged()
            }
            is IndustriesListState.Error -> {
                failedToGetListMessage(true)
            }
        }
    }

    private fun failedToGetListMessage(isVisible: Boolean) {
        if (isVisible) {
            with(binding) {
                ivNotListIndustries.isVisible = true
                tvNotListIndustries.isVisible = true
            }
        } else {
            with(binding) {
                ivNotListIndustries.isVisible = false
                tvNotListIndustries.isVisible = false
            }
        }
    }
}
