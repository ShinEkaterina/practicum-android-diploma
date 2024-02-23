package ru.practicum.android.diploma.ui.search.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
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

    private var scrollListener: OnScrollListener? = null

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
        scrollListener = object : OnScrollListener() {

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding?.rvSearch?.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
                    val itemsCount = vacanciesAdapter?.itemCount

                    if (pos != null && itemsCount != null && pos >= itemsCount - 1) {
                        viewModel.onLastItemReached(binding?.inputSearchForm?.text.toString())
                    }
                }
            }

        }
        scrollListener?.let { binding?.rvSearch?.addOnScrollListener(it) }

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
                viewModel.loadedVacancies.clear()
                viewModel.startVacanciesSearch(binding?.inputSearchForm?.text.toString())
            }
        }
        binding?.inputSearchForm?.addTextChangedListener(watcher)
    }

    override fun onPause() {
        super.onPause()

        binding?.inputSearchForm?.removeTextChangedListener(watcher)
        scrollListener?.let { binding?.rvSearch?.removeOnScrollListener(it) }
    }

    private fun hideAllComponents() {
        val marginParams = binding?.progressBar?.layoutParams as? MarginLayoutParams
        marginParams?.bottomMargin = 0
        binding?.progressBar?.layoutParams = marginParams

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
            is SearchRenderState.NoInternet -> {
                if (state.isPagination) {
                    if (viewModel.showToast) {
                        viewModel.showToast = false
                        Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
                    }
                    binding?.searchFoundVacancies?.text = resources.getQuantityString(R.plurals.vacancies, viewModel.vacanciesAmount.toInt(), viewModel.vacanciesAmountAsString)
                    binding?.rvSearch?.isVisible = true
                    binding?.searchFoundVacanciesWrapper?.isVisible = true
                } else {
                    binding?.noInternetImage?.isVisible = true
                }
            }
            is SearchRenderState.Loading -> {
                binding?.progressBar?.isVisible = true
                if (state.isPagination) {
                    val marginParams = binding?.progressBar?.layoutParams as? MarginLayoutParams
                    marginParams?.bottomMargin = (16 * resources.displayMetrics.density + 0.5f).toInt()
                    binding?.progressBar?.layoutParams = marginParams
                    binding?.searchFoundVacancies?.text = resources.getQuantityString(R.plurals.vacancies, viewModel.vacanciesAmount.toInt(), viewModel.vacanciesAmountAsString)
                    binding?.rvSearch?.isVisible = true
                    binding?.searchFoundVacanciesWrapper?.isVisible = true
                }
            }
            is SearchRenderState.Success -> {
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
