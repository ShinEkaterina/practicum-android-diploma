package ru.practicum.android.diploma.ui.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacancyBinding
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.adapter.VacanciesAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce

class SimilarFragment : Fragment() {
    private var _binding: FragmentSimilarVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SimilarViewModel>()
    private var vacancyClickDebounce: ((VacancyModel) -> Unit)? = null
    private var vacancyAdapter: VacanciesAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var vacancyId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarVacancyBinding.inflate(inflater, container, false)
        binding.similarToolbars.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vacancyId = requireArguments().getString(ARGS_VACANCY)
        initialAdapter()
        viewModel.getSimilarVacancies(vacancyId!!)
        viewModel.similarState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun initialAdapter() {
        vacancyAdapter = VacanciesAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }

        vacancyClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            val vacancyId = it.id
            findNavController().navigate(
                R.id.action_similarVacancy_to_vacancyFragment3,
                VacancyFragment.createArgs(vacancyId)
            )
        }

        vacancyAdapter = VacanciesAdapter {
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
            progressBar.visibility = VISIBLE
            recyclerView.visibility = GONE
        }
    }

    private fun notInternet() {
        with(binding) {
            progressBar.visibility = GONE
            noInternetPlaceholder.visibility = VISIBLE
            placeholderServerError.visibility = GONE
            placeholderNoVacancies.visibility = GONE
        }
    }
    private fun serverError() {
        with(binding) {
            progressBar.visibility = GONE
            noInternetPlaceholder.visibility = GONE
            placeholderServerError.visibility = VISIBLE
            placeholderNoVacancies.visibility = GONE
        }
    }
    private fun notFound() {
        with(binding) {
            progressBar.visibility = GONE
            noInternetPlaceholder.visibility = GONE
            placeholderServerError.visibility = GONE
            placeholderNoVacancies.visibility = VISIBLE
        }
    }

    private fun searchIsOk(data: List<VacancyModel>) {
        binding.progressBar.visibility = GONE
        recyclerView?.visibility = VISIBLE
        binding.recyclerView.visibility = VISIBLE
        binding.noInternetPlaceholder.visibility = GONE
        vacancyAdapter?.vacancies?.addAll(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARGS_VACANCY = "vacancyId"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(ARGS_VACANCY to vacancyId)

    }
}
