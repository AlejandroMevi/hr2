package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ItemPruebasBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models.ScheduleNegotiation
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities

class ScheduleNegotiationAdapter(
    private val item: List<ScheduleNegotiation>,
    private val itemClickListener: ScheduleNegotiationVPAdapter,
) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {
    interface OnClickListener{
        fun onClick(scheduleNegotiation: ScheduleNegotiation)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ItemPruebasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemholder = ViewHolder(itemBinding)
        itemBinding.root.setOnClickListener {
            val position = itemholder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnClickListener
            itemClickListener.onClick(item[position])
        }
        return itemholder
    }
    override fun onBindViewHolder(holder: ViewHolderGeneral<*>, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(item[position])
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int = item.size
    private inner class ViewHolder(
        val binding: ItemPruebasBinding
    ) : ViewHolderGeneral<ScheduleNegotiation>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: ScheduleNegotiation) {
            binding.title.text = "${Utilities.formatYearMonthDay(item.fechaInicio)} - ${Utilities.formatYearMonthDay(item.fechaFin)}"
            binding.description.text = item.departamento
        }
    }
}