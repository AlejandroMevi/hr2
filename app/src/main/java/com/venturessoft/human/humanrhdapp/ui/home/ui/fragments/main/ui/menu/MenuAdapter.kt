package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ItemMenuBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.MenuModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel

class MenuAdapter(private val item: List<MenuModel>, private val itemClickListener: OnClickListener, ) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {

    private val w = WelcomeFragmentViewModel()
    interface OnClickListener{
        fun onClick(menuModel: MenuModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemholder = ViewHolder(itemBinding)
        itemBinding.root.setOnClickListener {
            val position =
                itemholder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
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
        val binding: ItemMenuBinding
    ) : ViewHolderGeneral<MenuModel>(binding.root) {
        override fun bind(item: MenuModel) {
            binding.ivIcon.setImageResource(item.resource)
            binding.tvElement.text = item.name
        }
    }
}