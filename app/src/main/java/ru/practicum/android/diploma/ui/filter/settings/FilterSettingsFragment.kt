package ru.practicum.android.diploma.ui.filter.settings

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
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.domain.model.FilterParameters
import ru.practicum.android.diploma.domain.model.FilterParametersState

class FilterSettingsFragment : Fragment() {
    private val viewModel: FilterSettingsFragmentViewModel by viewModel()
    private var _binding: FragmentFilterSettingsBinding? = null
    private val binding: FragmentFilterSettingsBinding
        get() = _binding!!
    private var filterParameters = FilterParameters()

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
        initializationButtonsListener()
        viewModel.filterParametersState.observe(viewLifecycleOwner) {
            filterParametersState(it)
        }

        viewModel.getFilterParameters()

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
                onTextChangedAction(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // Обязательный метод интерфейса
            }
        }
        expectedSalaryEditTextListeners(
            simpleTextWatcher,
            inputMethodManager
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterParametersState(state: FilterParametersState) {
        when (state) {
            is FilterParametersState.Content -> updateFilterSettingsScreen(state.filterParameters)
            is FilterParametersState.Updating -> {}
        }
    }

    private fun updateFilterSettingsScreen(newFilterParameters: FilterParameters) {
        filterParameters = newFilterParameters
        with(binding) {
            resetButton.isVisible = viewModel.isFilterParametersNotEmpty(filterParameters)
            applyButton.isVisible = viewModel.isFilterParametersUpdated(filterParameters)
        }
        updatePlaceToJobSettingsScreen(filterParameters)
        updateIndustrySettingsScreen(filterParameters)
        updateExpectedSalarySettingsScreen(filterParameters)
        updateIsDoNotShowWithoutSalarySettingsScreen(filterParameters)
    }

    @SuppressLint("SetTextI18n")
    private fun updatePlaceToJobSettingsScreen(filterParameters: FilterParameters) {
        with(binding) {
            if (filterParameters.nameCountry != null) {
                val nameCountry = filterParameters.nameCountry
                etPlaceToJob.setText(nameCountry)
                if (filterParameters.nameRegion != null) {
                    val nameRegion = filterParameters.nameRegion
                    etPlaceToJob.setText("$nameCountry, $nameRegion")
                }
                tiPlaceToJob.setEndIconDrawable(R.drawable.ic_clear)
            } else {
                etPlaceToJob.setText("")
                tiPlaceToJob.setEndIconDrawable(R.drawable.ic_item_arrow)
            }
        }
    }

    private fun updateIndustrySettingsScreen(filterParameters: FilterParameters) {
        with(binding) {
            if (filterParameters.nameIndustry != null) {
                etIndustry.setText(filterParameters.nameIndustry)
                tiIndustry.setEndIconDrawable(R.drawable.ic_clear)
            } else {
                etIndustry.setText("")
                tiIndustry.setEndIconDrawable(R.drawable.ic_item_arrow)
            }
        }
    }

    private fun updateExpectedSalarySettingsScreen(filterParameters: FilterParameters) {
        with(binding) {
            if (filterParameters.expectedSalary != null) {
                etExpectedSalary.setText(filterParameters.expectedSalary.toString())
            } else {
                etExpectedSalary.setText("")
            }
        }
    }

    private fun updateIsDoNotShowWithoutSalarySettingsScreen(filterParameters: FilterParameters) {
        with(binding) {
            if (filterParameters.isDoNotShowWithoutSalary) {
                ivDoNotShowWithoutSalary.setImageResource(R.drawable.ic_check_box_checked)
            } else {
                ivDoNotShowWithoutSalary.setImageResource(R.drawable.ic_check_box_unchecked)
            }
        }
    }

    private fun initializationButtonsListener() {
        backPressedButtonsListener()
        placeToJobButtonListener()
        industryButtonListener()
        expectedSalaryButtonListener()
        doNotShowWithoutSalaryButtonListener()
        applyAndResetButtonsListener()
    }

    private fun backPressedButtonsListener() {
        with(binding) {
            filterToolbars.setNavigationOnClickListener {
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

    private fun placeToJobButtonListener() {
        with(binding) {
            tiPlaceToJob.setEndIconOnClickListener {
                if (filterParameters.nameCountry != null) {
                    etPlaceToJob.setText("")
                    tiPlaceToJob.setEndIconDrawable(R.drawable.ic_item_arrow)
                    viewModel.resetPlaceToJobParameters(filterParameters)
                } else {
                    findNavController().navigate(
                        R.id.action_filterSettingsFragment_to_choosingPlaceToJobFragment
                    )
                }
            }
        }
    }

    private fun industryButtonListener() {
        with(binding) {
            tiIndustry.setEndIconOnClickListener {
                if (filterParameters.nameIndustry != null) {
                    etIndustry.setText("")
                    tiIndustry.setEndIconDrawable(R.drawable.ic_item_arrow)
                    viewModel.resetIndustryParameters(filterParameters)
                } else {
                    findNavController().navigate(
                        R.id.action_filterSettingsFragment_to_industrySelectionFragment
                    )
                }
            }
        }
    }

    private fun expectedSalaryButtonListener() {
        with(binding) {
            tiExpectedSalary.setEndIconOnClickListener {
                etExpectedSalary.setText("")
                filterParameters = filterParameters.copy(expectedSalary = null)
                viewModel.setFilterParameters(filterParameters)
            }
        }
    }

    private fun doNotShowWithoutSalaryButtonListener() {
        with(binding) {
            ivDoNotShowWithoutSalary.setOnClickListener {
                filterParameters = if (filterParameters.isDoNotShowWithoutSalary) {
                    filterParameters.copy(isDoNotShowWithoutSalary = false)
                } else {
                    filterParameters.copy(isDoNotShowWithoutSalary = true)
                }
                viewModel.setFilterParameters(filterParameters)
            }
        }
    }

    private fun applyAndResetButtonsListener() {
        with(binding) {
            applyButton.setOnClickListener {
                findNavController().navigateUp()
                setFragmentResult(
                    "apply_filter",
                    bundleOf("apply_filter" to true)
                )
            }
            resetButton.setOnClickListener {
                filterParameters = viewModel.defaultFilterParameters()
                viewModel.setFilterParameters(filterParameters)
            }
        }
    }

    private fun expectedSalaryEditTextListeners(
        simpleTextWatcher: TextWatcher,
        inputMethodManager: InputMethodManager?
    ) {
        with(binding) {
            etExpectedSalary.addTextChangedListener(simpleTextWatcher)
            etExpectedSalary.setOnKeyListener { _, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                    etExpectedSalary.clearFocus()
                }
                false
            }
            etExpectedSalary.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    inputMethodManager?.hideSoftInputFromWindow(
                        etExpectedSalary.windowToken,
                        0
                    )
                    viewModel.setFilterParameters(filterParameters)
                    tiExpectedSalary.isEndIconVisible = false
                } else {
                    tiExpectedSalary.isEndIconVisible = filterParameters.expectedSalary != null
                }
            }
        }
    }

    private fun onTextChangedAction(s: CharSequence?) {
        with(binding) {
            tiExpectedSalary.isEndIconVisible = etExpectedSalary.hasFocus() && s?.isEmpty() == false
            filterParameters = if (s.isNullOrEmpty()) {
                filterParameters.copy(expectedSalary = null)
            } else {
                filterParameters.copy(expectedSalary = s.toString().toInt())
            }
        }
    }
}
