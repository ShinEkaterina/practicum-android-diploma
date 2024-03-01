package ru.practicum.android.diploma.ui.filter.industry

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.domain.model.IndustriesModel

class IndustriesAdapter(
    private val data: ArrayList<IndustriesModel>,
    private val onClick: (IndustriesModel) -> Unit
) : RecyclerView.Adapter<IndustriesViewHolder> () {
    private var checkedIndustry: IndustriesModel? = null
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: IndustriesViewHolder,
        position: Int
    ) {
        val industry = data[position]
        holder.bind(industry)

        if (checkedIndustry == industry) {
            holder.setCheckedImage(true)
        } else {
            holder.setCheckedImage(false)
        }

        holder.itemView.setOnClickListener {
            checkedIndustry = industry
            holder.setCheckedImage(true)
            onClick.invoke(industry)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
