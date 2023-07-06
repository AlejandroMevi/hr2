package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.core.AnimationExpanded
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ItemExtratimeBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models.TiempoExtra
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.formatHourMin
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.formatYearMonthDay
import java.util.*


class PreauthorizationAdapter(
    private val preauthorizationList: List<TiempoExtra>
): RecyclerView.Adapter<ViewHolderGeneral<*>>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ItemExtratimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: ViewHolderGeneral<*>, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(preauthorizationList[position])
        }
    }
    override fun getItemCount(): Int {
        return preauthorizationList.size
    }
    private inner class ViewHolder(
        val binding: ItemExtratimeBinding
    ) : ViewHolderGeneral<TiempoExtra>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: TiempoExtra) {
            binding.date.text = formatYearMonthDay(item.fecha).toString()
            binding.category.text = item.categoria
            binding.timeIn.text = formatHourMin(item.horaEntrada).toString()
            binding.timeOut.text = formatHourMin(item.horaSalida).toString()
            binding.timeExtra.text = "Horas Extras: ${item.horas} hrs."
            binding.root.setOnClickListener {
                val show: Boolean = toggleLayout(!item.isExpanded, binding.viewMoreBtn, binding.layoutExpand)
                item.isExpanded = show
            }
        }
    }

    internal fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: LinearLayoutCompat): Boolean {
        AnimationExpanded().toggleArrow(v, isExpanded)
        if (isExpanded) {
            AnimationExpanded().expand(layoutExpand)
        } else {
            AnimationExpanded().collapse(layoutExpand)
        }
        return isExpanded
    }
}