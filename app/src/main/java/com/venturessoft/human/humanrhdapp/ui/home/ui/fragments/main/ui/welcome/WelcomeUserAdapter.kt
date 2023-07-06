package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.welcome

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.models.KardexMensualItem
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ListItemLeftBinding

class WelcomeUserAdapter(
    private val item: List<KardexMensualItem>
    ) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ListItemLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, parent.context)
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
        val binding: ListItemLeftBinding,
        val context: Context
    ) : ViewHolderGeneral<KardexMensualItem>(binding.root) {
        override fun bind(item: KardexMensualItem) {
            if (item.fotoEmp!!.contains("File not foundjava", ignoreCase = true) || item.fotoEmp!!.isEmpty()) {
                binding.listImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_icon_big))
            } else {
                val imagenBase64 = Base64.decode(item.fotoEmp.toString(), Base64.DEFAULT)
                val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
                binding.listImage.setImageBitmap(imagenconverBitmap)
            }
            binding.listName.text = item.nomEmp
        }
    }
}