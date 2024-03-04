package ru.practicum.android.diploma.ui.search.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.adapter.VacanciesAdapter
import ru.practicum.android.diploma.ui.search.fragment.sate.SearchRenderState
import ru.practicum.android.diploma.ui.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.Constant
import ru.practicum.android.diploma.util.debounce

class SearchFragment : Fragment() {

    private val clickDebounce = debounce<VacancyModel>(
        delayInMillis = Constant.VACANCY_ITEM_CLICK_DEBOUNCE,
        coroutineScope = lifecycleScope,
        useLastParam = false
    ) { vacancy ->
        startVacancyActivity(vacancy.id)
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

        setFragmentResultListener("apply_filter") { _, bundle ->
            val selectedSort = bundle.getBoolean("apply_filter")
            if (selectedSort) {
                viewModel.startVacanciesSearch(binding?.inputSearchForm?.text.toString())
            }
        }

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

        binding?.filterButton?.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment2_to_filterSettingsFragment
            )
        }

        binding?.foundVacanciesList?.addOnScrollListener(object : OnScrollListener() {

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding?.foundVacanciesList?.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                    val itemsCount = vacanciesAdapter?.itemCount
                    if (itemsCount != null && pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }

        })

        binding?.foundVacanciesList?.itemAnimator = null
    }

    override fun onResume() {
        super.onResume()
        watcher = object : TextWatcher {
            override fun beforeTextChanged(
                str: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { /* cannot be removed */ }

            override fun afterTextChanged(
                str: Editable?
            ) { /* cannot be removed */ }

            override fun onTextChanged(
                searchText: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                hideAllComponents()
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
        binding?.foundVacanciesList?.isVisible = false

        // Hiding placeholders
        binding?.defaultPlaceholderImage?.isVisible = false
        binding?.searchNoInternet?.isVisible = false
        binding?.searchServerError?.isVisible = false
        binding?.searchNothingFound?.isVisible = false
        // ---

        binding?.searchProgressBar?.isVisible = false

        // Hiding search field components
        binding?.searchFieldSearchImage?.isVisible = false
        binding?.searchFieldClearButton?.isVisible = false
    }

    private fun startVacancyActivity(
        id: String
    ) {
        findNavController().navigate(
            R.id.action_searchFragment2_to_vacancyFragment3,
            VacancyFragment.createArgs(id)
        )
    }

    private fun hideKeyboard() {
        val service = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        service?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderSuccess(
        state: SearchRenderState.Success
    ) {
        if (state.resetScroll) {
            binding?.foundVacanciesList?.scrollToPosition(0)
        }

        binding?.searchFoundVacanciesWrapper?.isVisible = true
        binding?.searchFoundVacancies?.text = resources.getQuantityString(
            R.plurals.vacancies,
            viewModel.vacanciesAmount,
            viewModel.vacanciesAmountAsString
        )

        binding?.foundVacanciesList?.isVisible = true
        vacanciesAdapter?.notifyDataSetChanged()
    }

    private fun renderNoInternet() {
        binding?.searchNoInternet?.isVisible = true
    }

    private fun renderDefault() {
        binding?.searchFieldSearchImage?.isVisible = true
        binding?.searchFieldClearButton?.isVisible = false
        binding?.defaultPlaceholderImage?.isVisible = true
    }
    private fun renderServerError() {
        binding?.searchServerError?.isVisible = true

    }

    private fun renderNothingFound() {
        binding?.searchFoundVacancies?.text = getString(R.string.no_vacancies_found)
        binding?.searchFoundVacanciesWrapper?.isVisible = true
        binding?.searchNothingFound?.isVisible = true
    }

    private fun renderLoading() {
        binding?.foundVacanciesList?.scrollY = 0
        binding?.searchProgressBar?.isVisible = true
    }

    private fun renderPaginationNoInternet() {
        Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        viewModel.loadedVacancies.removeLast()
        vacanciesAdapter?.notifyItemRemoved(viewModel.loadedVacancies.size)
        Handler(Looper.getMainLooper()).postDelayed(1) {
            viewModel.loadedVacancies.add(null)
            viewModel.toSuccessState()
        }
    }

    private fun renderPaginationLoading() {
        binding?.searchFoundVacanciesWrapper?.isVisible = true
        binding?.searchFoundVacancies?.text = resources.getQuantityString(
            R.plurals.vacancies,
            viewModel.vacanciesAmount,
            viewModel.vacanciesAmountAsString
        )

        binding?.foundVacanciesList?.isVisible = true
    }

    private fun render(
        state: SearchRenderState
    ) {
        if (state !is SearchRenderState.PaginationNoInternet) {
            hideAllComponents()
        } else {
            binding?.searchProgressBar?.isVisible = false
        }
        if (state != SearchRenderState.Default) {
            hideKeyboard()
        }

        binding?.searchFieldClearButton?.isVisible = true

        when (state) {
            is SearchRenderState.Loading -> renderLoading()

            is SearchRenderState.NothingFound -> renderNothingFound()

            is SearchRenderState.Default -> renderDefault()

            is SearchRenderState.NoInternet -> renderNoInternet()

            is SearchRenderState.Success -> renderSuccess(state)

            is SearchRenderState.PaginationNoInternet -> renderPaginationNoInternet()

            is SearchRenderState.PaginationLoading -> renderPaginationLoading()

            is SearchRenderState.ServerError -> renderServerError()
        }
    }

}
