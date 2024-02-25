package ru.practicum.android.diploma.ui.filter.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.domain.model.IndustriesModel

class IndustriesAdapter(
    private val data: ArrayList<IndustriesModel>,
    private val onClick: (IndustriesModel) -> Unit
) : RecyclerView.Adapter<IndustriesViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndustriesViewHolder {
        val view = IndustryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustriesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: IndustriesViewHolder,
        position: Int
    ) {
        val industry = data[position]
        holder.bind(industry)
        holder.itemView.setOnClickListener {
            onClick.invoke(industry)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
