package ru.practicum.android.diploma.ui.search.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.adapter.VacanciesAdapter
import ru.practicum.android.diploma.ui.search.fragment.sate.SearchRenderState
import ru.practicum.android.diploma.ui.search.view_model.SearchViewModel
import ru.practicum.android.diploma.util.Constant
import ru.practicum.android.diploma.util.debounce

class SearchFragment : Fragment() {

    private val clickDebounce = debounce<VacancyModel>(
        delayInMillis = Constant.VACANCY_ITEM_CLICK_DEBOUNCE,
        coroutineScope = lifecycleScope,
        useLastParam = false
    ) { vacancy ->
        startVacancyActivity(vacancy.id.toLong())
    }

    private var vacanciesAdapter: VacanciesAdapter? = null

    private var binding: FragmentSearchBinding? = null

    private val viewModel: SearchViewModel by viewModel()

    private var watcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeRenderState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        vacanciesAdapter = VacanciesAdapter(ArrayList()) { vacancy ->
            clickDebounce(vacancy)
        }

        binding?.rvSearch?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vacanciesAdapter
        }

        binding?.clearButton?.setOnClickListener {
            hideKeyboard()
            binding?.inputSearchForm?.setText("")
        }
    }

    override fun onResume() {
        super.onResume()
        watcher = object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(str: Editable?) {}

            override fun onTextChanged(
                searchText: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (searchText.isNullOrEmpty()) {
                    viewModel.clearAllInput()
                } else {
                    binding?.searchImage?.isVisible = false
                    binding?.clearButton?.isVisible = true
                }
                viewModel.startVacanciesSearch(binding?.inputSearchForm?.text.toString())
            }
        }
        binding?.inputSearchForm?.addTextChangedListener(watcher)
    }

    override fun onPause() {
        super.onPause()
        binding?.inputSearchForm?.removeTextChangedListener(watcher)
    }

    private fun hideAllComponents() {
        binding?.rvSearch?.isVisible = false
        binding?.placeholderImage?.isVisible = false
        binding?.noInternetImage?.isVisible = false
        binding?.progressBar?.isVisible = false
        binding?.nothingFoundImage?.isVisible = false
        binding?.searchFoundVacanciesWrapper?.isVisible = false
    }

    private fun startVacancyActivity(
        id: Long
    ) {

    }

    private fun hideKeyboard() {
        val service = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        service?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(
        state: SearchRenderState
    ) {
        Toast.makeText(requireContext(), state.toString(), Toast.LENGTH_LONG).show()
        hideKeyboard()
        hideAllComponents()
        binding?.clearButton?.isVisible = true
        binding?.searchImage?.isVisible = false
        when (state) {
            is SearchRenderState.NothingFound -> {
                binding?.searchFoundVacancies?.text = getString(R.string.no_vacancies_found)
                binding?.searchFoundVacanciesWrapper?.isVisible = true
                binding?.nothingFoundImage?.isVisible = true
            }
            is SearchRenderState.NoInternet -> binding?.noInternetImage?.isVisible = true
            is SearchRenderState.Loading -> binding?.progressBar?.isVisible = true
            is SearchRenderState.Success -> {
                vacanciesAdapter?.vacancies?.clear()
                vacanciesAdapter?.vacancies?.addAll(state.vacancies.vacancies)
                vacanciesAdapter?.notifyDataSetChanged()
                binding?.searchFoundVacancies?.text = resources.getQuantityString(R.plurals.vacancies, state.vacancies.foundAsNumber.toInt(), state.vacancies.foundAsString)
                binding?.rvSearch?.isVisible = true
                binding?.searchFoundVacanciesWrapper?.isVisible = true
            }
            is SearchRenderState.Placeholder -> {
                binding?.clearButton?.isVisible = false
                binding?.searchImage?.isVisible = true
                binding?.placeholderImage?.isVisible = true
            }
        }
    }

}
