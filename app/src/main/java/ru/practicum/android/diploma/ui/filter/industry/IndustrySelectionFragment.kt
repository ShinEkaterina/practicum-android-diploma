package ru.practicum.android.diploma.ui.filter.industry

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
        initializationButtonsListener()
        initializationAdapter()
        viewModel.industriesListState.observe(viewLifecycleOwner) {
            industriesListState(it)
        }

        viewModel.getIndustries()

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

        searchIndustryEditTextListeners(
            simpleTextWatcher,
            inputMethodManager
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializationAdapter() {
        with(binding) {
            industriesAdapter = IndustriesAdapter(industriesList) { industry ->
                viewModel.setTempFilterParameters(industry)
                selectedButton.isVisible = true
            }

            rvIndustries.adapter = industriesAdapter
        }
    }

    private fun initializationButtonsListener() {
        with(binding) {
            industrySelectionToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            selectedButton.setOnClickListener {
                viewModel.setFilterParameters()
                findNavController().navigateUp()
            }

            clearButton.isEnabled = false
            clearButton.setOnClickListener {
                inputSearchIndustry.setText("")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun industriesListState(state: IndustriesListState) {
        with(binding) {
            when (state) {
                is IndustriesListState.Loading -> {
                    failedToGetListMessage(false)
                    progressBar.isVisible = true
                }

                is IndustriesListState.Content -> {
                    failedToGetListMessage(false)
                    progressBar.isVisible = false
                    industriesList.clear()
                    industriesList.addAll(state.industries)
                    industriesAdapter?.notifyDataSetChanged()
                }

                is IndustriesListState.Error -> {
                    failedToGetListMessage(true)
                    progressBar.isVisible = false
                }
            }
        }
    }

    private fun failedToGetListMessage(isVisible: Boolean) {
        with(binding) {
            if (isVisible) {
                ivNotListIndustries.isVisible = true
                tvNotListIndustries.isVisible = true
            } else {
                ivNotListIndustries.isVisible = false
                tvNotListIndustries.isVisible = false
            }
        }
    }

    private fun searchIndustryEditTextListeners(
        simpleTextWatcher: TextWatcher,
        inputMethodManager: InputMethodManager?
    ) {
        with(binding) {
            inputSearchIndustry.addTextChangedListener(simpleTextWatcher)
            inputSearchIndustry.setOnKeyListener { _, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                    inputSearchIndustry.clearFocus()
                }
                false
            }
            inputSearchIndustry.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    inputMethodManager?.hideSoftInputFromWindow(
                        inputSearchIndustry.windowToken,
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
