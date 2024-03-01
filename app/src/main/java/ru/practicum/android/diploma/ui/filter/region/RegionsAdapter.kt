package ru.practicum.android.diploma.ui.filter.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.RegionItemBinding
import ru.practicum.android.diploma.domain.model.AreasModel

class RegionsAdapter(
    private val data: ArrayList<AreasModel>,
    private val onClick: (AreasModel) -> Unit
) : RecyclerView.Adapter<RegionsViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionsViewHolder {
        val view = RegionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RegionsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RegionsViewHolder,
        position: Int
    ) {
        val region = data[position]
        holder.bind(region)
        holder.itemView.setOnClickListener {
            onClick.invoke(region)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
