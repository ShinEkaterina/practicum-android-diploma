package ru.practicum.android.diploma.ui.filter.industry

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.domain.model.IndustriesModel

class IndustriesViewHolder(private val binding: IndustryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: IndustriesModel) {
        binding.tvIndustryItem.text = item.name
    }

    fun setCheckedImage(isChecked: Boolean) {
        if (isChecked) {
            binding.ivSelectedIndustryItem.setImageResource(R.drawable.ic_selected_circle)
        } else {
            binding.ivSelectedIndustryItem.setImageResource(R.drawable.ic_unselected_circle)
        }
    }
}
