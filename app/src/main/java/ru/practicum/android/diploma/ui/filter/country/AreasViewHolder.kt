package ru.practicum.android.diploma.ui.filter.country

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.CountryItemBinding
import ru.practicum.android.diploma.domain.model.AreasModel

class AreasViewHolder(private val binding: CountryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AreasModel) {
        binding.industryCountryText.text = item.name
    }
}
