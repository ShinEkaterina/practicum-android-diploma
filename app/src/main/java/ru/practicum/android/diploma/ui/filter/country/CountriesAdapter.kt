package ru.practicum.android.diploma.ui.filter.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.CountryItemBinding
import ru.practicum.android.diploma.domain.model.AreasModel

class CountriesAdapter(
    private val data: ArrayList<AreasModel>,
    private val onClick: (AreasModel) -> Unit
) : RecyclerView.Adapter<CountriesViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountriesViewHolder {
        val view = CountryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val area = data[position]
        holder.bind(area)
        holder.itemView.setOnClickListener {
            onClick.invoke(area)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
