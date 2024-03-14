package ru.practicum.android.diploma.ui.employer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailEmployerBinding
import ru.practicum.android.diploma.domain.model.EmployerModel
import ru.practicum.android.diploma.ui.similar.OpenVacanciesFragment

class EmployerFragment : Fragment() {
    private var idEmployer: String? = null
    private var _binding: FragmentDetailEmployerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EmployerViewModel by viewModel<EmployerViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailEmployerBinding.inflate(inflater, container, false)

        binding.employerToolbars.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idEmployer = requireArguments().getString(ID_EMPLOYER)
        if (idEmployer != null) viewModel.getEmployer(idEmployer!!)
        viewModel.employerState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        binding.buttonOpenVacancy.setOnClickListener {
            if (idEmployer != null) {
                findNavController().navigate(
                    R.id.action_detailEmployer_to_openVacanciesFragment,
                    OpenVacanciesFragment.createArgs(idEmployer!!)
                )
            }
        }

    }

    private fun render(stateLiveData: EmployerState) {
        when (stateLiveData) {
            is EmployerState.Loading -> loading()
            is EmployerState.Content -> content(stateLiveData.employerModel)
            is EmployerState.NotInternet -> connectionError()
            is EmployerState.ErrorServer -> errorServer()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(employer: EmployerModel) {
        with(binding) {
            employerName.text = employer.name
            siteUrlData.text = employer.site
            buttonOpenVacancy.text = getString(R.string.open_vacancies) + " " + employer.openVacancies.toString()
            Glide.with(requireContext())
                .load(employer.logoUrl)
                .placeholder(R.drawable.ic_logo)
                .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.margin_8)))
                .into(ivCompany)

            companyCity.text = employer.area
            createDiscription(employer.description)

        }
    }

    private fun createDiscription(description: String?) {
        binding.tvDescription.text = HtmlCompat.fromHtml(
            description?.replace(Regex("<li>\\s<p>|<li>"), "<li>\u00A0") ?: "",
            HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
        )
    }

    private fun loading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            fragmentNotifications.visibility = View.GONE
            placeholderServerError.visibility = View.GONE
            noInternetPlaceholder.visibility = View.GONE
        }
    }

    private fun content(data: EmployerModel) {
        initViews(data)
        with(binding) {
            progressBar.visibility = View.GONE
            fragmentNotifications.visibility = View.VISIBLE
            placeholderServerError.visibility = View.GONE
            noInternetPlaceholder.visibility = View.GONE
        }
    }

    private fun errorServer() {
        with(binding) {
            progressBar.visibility = View.GONE
            fragmentNotifications.visibility = View.GONE
            placeholderServerError.visibility = View.VISIBLE
            noInternetPlaceholder.visibility = View.GONE
        }
    }

    private fun connectionError() {
        with(binding) {
            progressBar.visibility = View.GONE
            fragmentNotifications.visibility = View.GONE
            placeholderServerError.visibility = View.GONE
            noInternetPlaceholder.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ID_EMPLOYER = "idEmployer"
        fun createArgs(employerId: String): Bundle =
            bundleOf(ID_EMPLOYER to employerId)

    }
}
