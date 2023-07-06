package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.venturessoft.human.humanrhdapp.network.Response.ItemItem
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ListItemEmployeeReportsBinding

class ListEmployeeAdapter(
    private val item: List<ItemItem>,
    private val lifecycleOwner: LifecycleOwner,
    private val itemClickListener: OnClickListener,
    ) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {
    companion object{
        var listEmployeSelect = MutableLiveData<List<Long>>()
    }

    interface OnClickListener{
        fun onClick(item: ItemItem, position: Int, cardviewlista: MaterialCardView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ListItemEmployeeReportsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemholder = ViewHolder(itemBinding,parent.context)
        itemBinding.root.setOnClickListener {
            val position = itemholder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnClickListener
            itemClickListener.onClick(item[position], position, itemBinding.root)
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
        val binding: ListItemEmployeeReportsBinding,
        val context: Context
    ) : ViewHolderGeneral<ItemItem>(binding.root) {
        override fun bind(item: ItemItem) {
            val numero = item.numEmp.toString().toLong()
            val numeroFormateado = String.format("%010d", numero)
            val numeroConCeros = numeroFormateado.padStart(10, '0')
            binding.listName.text = item.nombreCompleto
            binding.listNumEmployee.text = numeroConCeros
            binding.listPuesto.text = if (item.puesto == "null") "" else item.puesto
            binding.cardviewlista.setBackgroundColor(context.getColor(R.color.white))
            if (item.fotografia!!.contains("File not foundjava", ignoreCase = true) || item.fotografia!!.isEmpty()) {
                binding.listImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_icon_big))
            } else {
                val imagenBase64 = Base64.decode(item.fotografia, Base64.DEFAULT)
                val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
                binding.listImage.setImageBitmap(imagenconverBitmap)
            }
            listEmployeSelect.observe(lifecycleOwner){
                try {
                    if (it.contains(item.numEmp?.toLong())){
                        binding.cardviewlista.setBackgroundColor(context.getColor(R.color.light_gray))
                    }
                }catch (_:Exception){ }
            }
        }
    }
}