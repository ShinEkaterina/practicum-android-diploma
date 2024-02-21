package ru.practicum.android.diploma.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.adapter.VacanciesAdapter
import ru.practicum.android.diploma.ui.search.viewholder.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var vacancyClickDebounce: ((VacancyModel) -> Unit)? = null
    private var vacancyAdapter: VacanciesAdapter? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        vacancyAdapter = VacanciesAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }

        initInputSearchForm()

        vacancyClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            val vacancyId = it.id
            findNavController().navigate(
                R.id.action_searchFragment2_to_vacancyFragment3,
                VacancyFragment.createArgs(vacancyId))
        }
        vacancyAdapter = VacanciesAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }
        recyclerView = binding.rvSearch
        recyclerView!!.adapter = vacancyAdapter

        ////для теста
        binding.filterButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment2_to_filterSettingsFragment
            )
        }
        ////

    }

    private fun initInputSearchForm() {
        binding.inputSearchForm.doOnTextChanged { query: CharSequence?, _, _, _ ->
            if (query.isNullOrEmpty()) {
                binding.clearButton.visibility = GONE
                binding.searchImage.visibility = VISIBLE
            } else {
                binding.clearButton.visibility = VISIBLE
                binding.searchImage.visibility = GONE
            }
            viewModel.search(query.toString())
        }

        binding.inputSearchForm.requestFocus()
        onClearIconClick()

    }

    private fun render(stateLiveData: SearchState) {
        when (stateLiveData) {
            is SearchState.Loading -> loading()
            is SearchState.SearchContent -> searchIsOk(stateLiveData.vacancys)
            is SearchState.Error -> connectionError()
            is SearchState.EmptyScreen -> defaultSearch()
        }
    }

    private fun onClearIconClick() {
        binding.clearButton.setOnClickListener {
            binding.inputSearchForm.setText("")
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputSearchForm.windowToken,
                0
            )
            binding.inputSearchForm.clearFocus()
        }
    }

    private fun defaultSearch() {
        recyclerView?.visibility = GONE
        binding.placeholderImage.visibility = VISIBLE
        binding.placeholderImage.setImageResource(R.drawable.ic_error_server)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        binding.progressBar.visibility = VISIBLE
        recyclerView?.visibility = GONE
        binding.placeholderImage.visibility = GONE
        vacancyAdapter?.notifyDataSetChanged()

    }

    private fun searchIsOk(data: List<VacancyModel>) {
        binding.progressBar.visibility = GONE
        recyclerView?.visibility = VISIBLE
        binding.placeholderImage.visibility = GONE
        binding.clearButton.visibility = GONE
        vacancyAdapter?.vacancies?.addAll(data)

    }

    private fun connectionError() {
        binding.progressBar.visibility = GONE
        recyclerView?.visibility = GONE
        binding.placeholderImage.visibility = GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}
