package ru.practicum.android.diploma.ui.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentOpenVacanciesBinding
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.adapter.VacanciesAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce

class OpenVacanciesFragment : Fragment() {

    private var _binding: FragmentOpenVacanciesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SimilarViewModel>()
    private var vacancyClickDebounce: ((VacancyModel) -> Unit)? = null
    private var vacancyAdapter: VacanciesAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var employerId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenVacanciesBinding.inflate(inflater, container, false)
        binding.openVacanciesToolbars.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        employerId = requireArguments().getString(ARGS_EMPLOYER)
        initialAdapter()
        viewModel.getOpenVacancies(employerId!!)
        viewModel.similarState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun initialAdapter() {
        vacancyAdapter = VacanciesAdapter(arrayListOf()) {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }

        vacancyClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            val vacancyId = it.id
            findNavController().navigate(
                R.id.action_openVacanciesFragment_to_vacancyFragment3,
                VacancyFragment.createArgs(vacancyId)
            )
        }

        vacancyAdapter = VacanciesAdapter(arrayListOf()) {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }
        recyclerView = binding.recyclerView
        recyclerView!!.adapter = vacancyAdapter
    }

    private fun render(stateLiveData: SimilarState) {
        when (stateLiveData) {
            is SimilarState.Loading -> loading()
            is SimilarState.Content -> searchIsOk(stateLiveData.vacancies)
            is SimilarState.NotInternet -> notInternet()
            is SimilarState.ErrorServer -> serverError()
            is SimilarState.NotFound -> notFound()
        }
    }

    private fun loading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    private fun notInternet() {
        with(binding) {
            progressBar.visibility = View.GONE
            noInternetPlaceholder.visibility = View.VISIBLE
            placeholderServerError.visibility = View.GONE
            placeholderNoVacancies.visibility = View.GONE
        }
    }

    private fun serverError() {
        with(binding) {
            progressBar.visibility = View.GONE
            noInternetPlaceholder.visibility = View.GONE
            placeholderServerError.visibility = View.VISIBLE
            placeholderNoVacancies.visibility = View.GONE
        }
    }

    private fun notFound() {
        with(binding) {
            progressBar.visibility = View.GONE
            noInternetPlaceholder.visibility = View.GONE
            placeholderServerError.visibility = View.GONE
            placeholderNoVacancies.visibility = View.VISIBLE
        }
    }

    private fun searchIsOk(data: List<VacancyModel>) {
        binding.progressBar.visibility = View.GONE
        recyclerView?.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.noInternetPlaceholder.visibility = View.GONE

        vacancyAdapter?.vacancies?.clear()
        vacancyAdapter?.vacancies?.addAll(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARGS_EMPLOYER = "employerId"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(ARGS_EMPLOYER to vacancyId)

    }

}
