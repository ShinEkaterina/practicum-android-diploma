package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.model.DetailVacancy
import ru.practicum.android.diploma.ui.similar.SimilarFragment

class VacancyFragment : Fragment() {

    private var vacancyId: String? = null
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel<VacancyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        binding.vacancyToolbars.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacancyId = requireArguments().getString(ARGS_VACANCY)
        if (vacancyId != null) viewModel.showVacancyDetail(vacancyId!!)
        viewModel.vacancyState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        binding.buttonSimilarVacancy.setOnClickListener {
            if (vacancyId != null) {
                findNavController().navigate(
                    R.id.action_vacancyFragment3_to_similarVacancy,
                    SimilarFragment.createArgs(vacancyId!!)
                )
            }
        }
        binding.buttonAddToFavorites.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.getIsFavorite().observe(viewLifecycleOwner) { isFavorite ->
            changeLikeButton(isFavorite)
        }
    }

    private fun changeLikeButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.buttonAddToFavorites.setImageResource(R.drawable.ic_favorite_on)
        } else {
            binding.buttonAddToFavorites.setImageResource(R.drawable.ic_favorite)
        }
        binding.contactPersonPhoneData.setOnClickListener {
            val phoneNumber = binding.contactPersonPhoneData.text.toString()

            if (binding.contactPersonPhoneData.selectionStart != -1 &&
                binding.contactPersonPhoneData.selectionEnd != -1
            ) {
                viewModel.call(phoneNumber)
            }

        }

        binding.contactPersonEmailData.setOnClickListener {
            val emailAddress = binding.contactPersonEmailData.text.toString()
            val personName = binding.contactPersonData.text.toString()
            viewModel.sendEmail(emailAddress, personName)
        }
    }

    private fun render(stateLiveData: VacancyState) {
        when (stateLiveData) {
            is VacancyState.Loading -> loading()
            is VacancyState.Content -> content(stateLiveData.vacancy)
            is VacancyState.NotInternet -> connectionError()
            is VacancyState.ErrorServer -> errorServer()
        }
    }

    private fun initViews(vacancy: DetailVacancy) {
        with(binding) {
            jobName.text = vacancy.name
            jobSalary.text = vacancy.salary

            Glide.with(requireContext())
                .load(vacancy.areaUrl)
                .placeholder(R.drawable.ic_logo)
                .fitCenter()
                .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.margin_8)))
                .into(ivCompany)

            companyName.text = vacancy.employerName
            if (vacancy.address.isEmpty()) {
                companyCity.text = vacancy.areaName
            } else {
                companyCity.text = vacancy.address
            }

            jobTime.text = vacancy.scheduleName
            if (vacancy.experienceName.isEmpty()) {
                neededExperience.visibility = GONE
                yearsOfExperience.visibility = GONE
            } else {
                neededExperience.visibility = VISIBLE
                yearsOfExperience.visibility = VISIBLE
                yearsOfExperience.text = vacancy.experienceName

            }
            createDiscription(vacancy.description)
            createKeySkills(vacancy.keySkillsNames)
            createContacts(vacancy)
            binding.buttonShare.setOnClickListener {
                viewModel.shareVacancy(vacancy.urlVacancy)
            }
        }
    }

    fun createContacts(vacancy: DetailVacancy) {
        with(binding) {
            if (vacancy.contactsName.isNotEmpty()) {
                contactPersonData.text = vacancy.contactsName
                contactInformation.visibility = VISIBLE
                contactPerson.visibility = VISIBLE
                contactPersonData.visibility = VISIBLE
            }
            if (vacancy.contactsEmail.isNotEmpty()) {
                contactPersonEmailData.text = vacancy.contactsEmail
                contactInformation.visibility = VISIBLE
                contactPersonEmail.visibility = VISIBLE
                contactPersonEmailData.visibility = VISIBLE

            }
            if (vacancy.contactsPhones.isNotEmpty()) {
                var phones = ""
                vacancy.contactsPhones.forEach { phone ->
                    phones += phone.first +
                        isEmptyComment(phone.second)

                }

                contactPersonPhoneData.text =
                    HtmlCompat.fromHtml(phones, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM)
                contactInformation.visibility = VISIBLE
                contactPersonPhone.visibility = VISIBLE
                contactPersonPhoneData.visibility = VISIBLE
            }

        }
    }

    private fun isEmptyComment(comment: String?): String {
        return if (comment.isNullOrEmpty()) {
            "<br>"
        } else {
            "<br><br><b>${getString(R.string.contact_comment_text)}</b><br>" + comment + "<br><br>"
        }
    }

    private fun createDiscription(description: String?) {
        binding.tvDescription.text = HtmlCompat.fromHtml(
            description?.replace(Regex("<li>\\s<p>|<li>"), "<li>\u00A0") ?: "",
            HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
        )
    }

    private fun createKeySkills(keySkills: List<String?>) {
        with(binding) {
            if (keySkills.isEmpty()) {
                keySkillsRecyclerView.visibility = GONE
                binding.keySkills.visibility = GONE
            } else {
                var skills = ""
                keySkills.forEach { skill ->
                    skills += "• ${skill}\n"
                }
                keySkillsRecyclerView.text = skills
            }
        }

    }

    private fun loading() {
        with(binding) {
            progressBar.visibility = VISIBLE
            fragmentNotifications.visibility = GONE
            placeholderServerError.visibility = GONE
            noInternetPlaceholder.visibility = GONE
        }
    }

    private fun content(data: DetailVacancy) {
        initViews(data)
        with(binding) {
            progressBar.visibility = GONE
            fragmentNotifications.visibility = VISIBLE
            placeholderServerError.visibility = GONE
            noInternetPlaceholder.visibility = GONE
        }
    }

    private fun errorServer() {
        with(binding) {
            progressBar.visibility = GONE
            fragmentNotifications.visibility = GONE
            placeholderServerError.visibility = VISIBLE
            noInternetPlaceholder.visibility = GONE
        }
    }
    private fun connectionError() {
        with(binding) {
            progressBar.visibility = GONE
            fragmentNotifications.visibility = GONE
            placeholderServerError.visibility = GONE
            noInternetPlaceholder.visibility = VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARGS_VACANCY = "vacancyId"
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(ARGS_VACANCY to vacancyId)

    }
}
