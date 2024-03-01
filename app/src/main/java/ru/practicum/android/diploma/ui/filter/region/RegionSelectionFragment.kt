package ru.practicum.android.diploma.ui.filter.region

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionSelectionBinding
import ru.practicum.android.diploma.domain.model.AreasModel
import ru.practicum.android.diploma.domain.model.RegionsListState

class RegionSelectionFragment : Fragment() {
    private val viewModel: RegionSelectionViewModel by viewModel()
    private var _binding: FragmentRegionSelectionBinding? = null
    private val binding: FragmentRegionSelectionBinding
        get() = _binding!!
    private var regionsAdapter: RegionsAdapter? = null
    private val regionsList = ArrayList<AreasModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionSelectionBinding.inflate(
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
        initializationAdapter()
        viewModel.regionsListState.observe(viewLifecycleOwner) {
            regionsListState(it)
        }

        viewModel.getRegions()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Обязательный метод интерфейса
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Обязательный метод интерфейса
            }
        }

        searchRegionsEditTextListeners(
            simpleTextWatcher,
            inputMethodManager
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializationButtonsListener() {
        with(binding) {
            regionSelectionToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            clearButton.isEnabled = false
            clearButton.setOnClickListener {
                inputSearchRegion.setText("")
            }
        }
    }

    private fun initializationAdapter() {
        with(binding) {
            regionsAdapter = RegionsAdapter(regionsList) { region ->
                viewModel.setFilterParameters(region)
                findNavController().navigateUp()
            }

            rvRegions.adapter = regionsAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun regionsListState(state: RegionsListState) {
        with(binding) {
            when (state) {
                is RegionsListState.Loading -> {
                    failedToGetListMessage(false)
                    progressBar.isVisible = true
                }

                is RegionsListState.Content -> {
                    failedToGetListMessage(false)
                    progressBar.isVisible = false
                    regionsList.clear()
                    regionsList.addAll(state.regions)
                    regionsAdapter?.notifyDataSetChanged()
                    llNotRegions.isVisible = regionsList.isEmpty()
                }

                is RegionsListState.Error -> {
                    failedToGetListMessage(true)
                    progressBar.isVisible = false
                }
            }
        }
    }

    private fun failedToGetListMessage(isVisible: Boolean) {
        binding.llNotListRegions.isVisible = isVisible
    }

    private fun searchRegionsEditTextListeners(
        simpleTextWatcher: TextWatcher,
        inputMethodManager: InputMethodManager?
    ) {
        with(binding) {
            inputSearchRegion.addTextChangedListener(simpleTextWatcher)
            inputSearchRegion.setOnKeyListener { _, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                    inputSearchRegion.clearFocus()
                }
                false
            }
            inputSearchRegion.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    inputMethodManager?.hideSoftInputFromWindow(
                        inputSearchRegion.windowToken,
                        0
                    )
                    clearButton.setImageResource(R.drawable.ic_search_24)
                    clearButton.isEnabled = false
                } else {
                    clearButton.setImageResource(R.drawable.ic_clear_24)
                    clearButton.isEnabled = true
                }
            }
        }
    }
}
