package ru.practicum.android.diploma.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.databinding.VacancyPaginationLoadingBinding
import ru.practicum.android.diploma.di.viewModelModule
import ru.practicum.android.diploma.domain.model.VacancyModel
import ru.practicum.android.diploma.ui.search.viewholder.LoadingViewHolder
import ru.practicum.android.diploma.ui.search.viewholder.VacanciesViewHolder

class VacanciesAdapter(
    val vacancies: ArrayList<VacancyModel?>,
    private val itemClickListener: ((VacancyModel) -> Unit)
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> VacanciesViewHolder(VacancyItemBinding.inflate(layoutInflater, parent, false))
            else -> LoadingViewHolder(VacancyPaginationLoadingBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        when (holder.itemViewType) {
            1 -> {
                val vacancy = vacancies[position]
                (holder as VacanciesViewHolder).bind(vacancy!!)
                holder.itemView.setOnClickListener {
                    itemClickListener.invoke(vacancy)
                }
            }
            else -> {}
        }
    }

    override fun getItemViewType(
        position: Int
    ): Int {
        return if (vacancies[position] == null) 2 else 1
    }

}
