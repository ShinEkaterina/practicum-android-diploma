package ru.practicum.android.diploma.ui.filter.region

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.RegionItemBinding
import ru.practicum.android.diploma.domain.model.AreasModel

class RegionsViewHolder(private val binding: RegionItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AreasModel) {
        binding.tvRegionItem.text = item.name
    }
}
