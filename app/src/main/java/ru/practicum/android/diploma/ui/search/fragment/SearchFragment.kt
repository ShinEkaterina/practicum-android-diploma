package ru.practicum.android.diploma.ui.search.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
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

        vacanciesAdapter = VacanciesAdapter(viewModel.loadedVacancies) { vacancy ->
            clickDebounce(vacancy)
        }

        binding?.foundVacanciesList?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vacanciesAdapter
        }

        binding?.searchFieldClearButton?.setOnClickListener {
            hideKeyboard()
            binding?.inputSearchForm?.setText("")
        }

        binding?.foundVacanciesContainer?.setOnScrollChangeListener { v, _, scrollY, _, _ ->
            val nestedScrollView = v as NestedScrollView
            if (viewModel.isScrollableDown && scrollY == nestedScrollView.getChildAt(0).measuredHeight - nestedScrollView.measuredHeight) {
                viewModel.onLastItemReached()
            } else {
                viewModel.isScrollableDown = true
            }
        }

        binding?.foundVacanciesList?.itemAnimator = null
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
                    binding?.searchFieldSearchImage?.isVisible = false
                    binding?.searchFieldClearButton?.isVisible = true
                }
                viewModel.loadedVacancies.clear()
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
        binding?.searchFoundVacanciesWrapper?.isVisible = false
        binding?.foundVacanciesContainer?.isVisible = false
        binding?.foundVacanciesProgressBarPagination?.isVisible = false

        // Hiding placeholders
        binding?.defaultPlaceholderImage?.isVisible = false
        binding?.searchNoInternet?.isVisible = false
        binding?.searchNothingFound?.isVisible = false
        // ---

        binding?.searchProgressBar?.isVisible = false

        // Hiding search field components
        binding?.searchFieldSearchImage?.isVisible = false
        binding?.searchFieldClearButton?.isVisible = false
    }

    private fun startVacancyActivity(
        id: Long
    ) {

    }

    private fun hideKeyboard() {
        val service = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        service?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun render(
        state: SearchRenderState
    ) {
        if (state != SearchRenderState.PaginationNoInternet) {
            hideAllComponents()
        } else {
            binding?.searchProgressBar?.isVisible = false
        }
        if (state != SearchRenderState.Placeholder) {
            hideKeyboard()
        }

        binding?.searchFieldClearButton?.isVisible = true

        when (state) {
            is SearchRenderState.Loading -> {
                binding?.foundVacanciesContainer?.scrollY = 0
                binding?.searchProgressBar?.isVisible = true
            }

            is SearchRenderState.NothingFound -> binding?.searchNothingFound?.isVisible = true

            is SearchRenderState.Placeholder -> {
                binding?.searchFieldSearchImage?.isVisible = true
                binding?.searchFieldClearButton?.isVisible = false
                binding?.defaultPlaceholderImage?.isVisible = true
            }

            is SearchRenderState.NoInternet -> binding?.searchNoInternet?.isVisible = true

            is SearchRenderState.Success -> {
                binding?.searchFoundVacanciesWrapper?.isVisible = true
                binding?.searchFoundVacancies?.text = resources.getQuantityString(R.plurals.vacancies, viewModel.vacanciesAmount.toInt(), viewModel.vacanciesAmountAsString)

                binding?.foundVacanciesContainer?.isVisible = true
                vacanciesAdapter?.notifyItemRangeChanged(viewModel.loadedVacancies.size, Constant.PER_PAGE_ITEMS.toInt())
            }

            is SearchRenderState.PaginationNoInternet -> {
                Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
                viewModel.toSuccessState()
            }

            is SearchRenderState.PaginationLoading -> {
                binding?.searchFoundVacanciesWrapper?.isVisible = true
                binding?.searchFoundVacancies?.text = resources.getQuantityString(R.plurals.vacancies, viewModel.vacanciesAmount.toInt(), viewModel.vacanciesAmountAsString)

                binding?.foundVacanciesContainer?.isVisible = true
                binding?.foundVacanciesProgressBarPagination?.isVisible = true
            }
        }
    }

}
