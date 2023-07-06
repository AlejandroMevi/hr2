package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.ProgAnualVacaciones.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ListaSolicitudesProgAnualVacBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.ProgAnualVacaciones.ui.data.models.VacacionesProgramadasItem

class ListSolicitudesAdapter(
    private val item: List<VacacionesProgramadasItem>
) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {
    companion object {
        var listSolicitudSelect = MutableLiveData<List<Long>>()
        private val solicitudesSelect = mutableListOf<Long>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ListaSolicitudesProgAnualVacBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
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
        val binding: ListaSolicitudesProgAnualVacBinding,
        val context: Context
    ) : ViewHolderGeneral<VacacionesProgramadasItem>(binding.root) {
        override fun bind(item: VacacionesProgramadasItem) {
            val grupo = item.grupo.toString()
            val secuencia = item.secuencia.toString()
            val fechaInicio = item.fechaInicio
            val fechaTermino = item.fechaTermino
            val diasTomados = item.diasTomados.toString()
            if (item.secuencia == 0) binding.cbAutorizar.visibility = View.VISIBLE
            binding.tvGrupo.text = grupo
            binding.tvSecuencia.text = secuencia
            binding.tvFechaInicio.text = fechaInicio
            binding.tvFechaTermino.text = fechaTermino
            binding.tvDiasTomados.text = diasTomados

            binding.cbAutorizar.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    solicitudesSelect.add(item.grupo.toString().toLong())
                } else {
                    if (solicitudesSelect.contains(item.grupo.toString().toLong())) {
                        solicitudesSelect.remove(item.grupo.toString().toLong())
                    }
                }
                listSolicitudSelect.value = solicitudesSelect
            }
        }
    }
}