package ru.practicum.android.diploma.ui.search.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.util.Constant

class VacanciesViewHolder(
    private val binding: VacancyItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(
        model: VacancyModel
    ) {
        binding.vacancyName.text = model.vacancyName + ", ${model.city}"
        binding.companyName.text = model.companyName
        binding.companySalary.text = model.salary.ifEmpty { itemView.context.getString(R.string.salary_not_specified) }

        if (model.logoUrls?.isNotEmpty() == true) {
            Glide.with(itemView)
                .load(model.logoUrls[0])
                .centerCrop()
                .transform(RoundedCorners(Constant.COMPANY_LOGO_RADIUS_12_PX))
                .placeholder(R.drawable.ic_logo)
                .into(binding.companyLogo)
        } else {
            binding.companyLogo.setImageResource(R.drawable.ic_logo)
        }
    }

}
