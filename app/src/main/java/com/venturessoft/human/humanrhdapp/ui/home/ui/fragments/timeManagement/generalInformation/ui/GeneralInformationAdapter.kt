package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ItemPruebasBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.ListaMaestroReloj
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.formatYearMonthDay

class GeneralInformationAdapter(
    private val item: List<ListaMaestroReloj>,
    private val itemClickListener: OnClickListener,
    ) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {
    interface OnClickListener{
        fun onClick(listaMaestroReloj: ListaMaestroReloj)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ItemPruebasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemholder = ViewHolder(itemBinding)
        itemBinding.root.setOnClickListener {
            val position = itemholder.bindingAdapterPosition.takeIf {
                it != DiffUtil.DiffResult.NO_POSITION
            }?: return@setOnClickListener
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
    ) : ViewHolderGeneral<ListaMaestroReloj>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: ListaMaestroReloj) {
            binding.title.text = formatYearMonthDay(item.fechaAplicacion)
            binding.description.text = item.gafete.toString()
        }
    }
}