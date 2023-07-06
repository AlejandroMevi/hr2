package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ItemVpTimeBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.ListaMaestroReloj
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.DATA_KEY

class GeneralInformationVPAdapter(
    private val fragment: Fragment,
    private val generalInformationFragment: GeneralInformationFragment?=null,
    private var listMasterFilter: List<List<ListaMaestroReloj>>,

    ) : RecyclerView.Adapter<ViewHolderGeneral<*>>(),GeneralInformationAdapter.OnClickListener {
    interface OnClickListener{
        fun onClick(item: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ItemVpTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }
    override fun onBindViewHolder(holder: ViewHolderGeneral<*>, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(listMasterFilter[position])
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int = listMasterFilter.size
    private inner class ViewHolder(
        val binding: ItemVpTimeBinding
    ) : ViewHolderGeneral<List<ListaMaestroReloj>>(binding.root) {
        override fun bind(listaMaestroReloj: List<ListaMaestroReloj>) {
            if (listaMaestroReloj.isNotEmpty()){
                binding.tvDataEmpty.isVisible = false
                binding.rvGeneralInformation.adapter = GeneralInformationAdapter(listaMaestroReloj,this@GeneralInformationVPAdapter)
                if (generalInformationFragment != null){
                    binding.rvGeneralInformation.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            if (dy > 0){
                                generalInformationFragment.binding.floating.shrink()
                                if(generalInformationFragment.binding.clFloatingButton.isVisible){
                                    generalInformationFragment.onAddButton()
                                }
                            } else {
                                generalInformationFragment.binding.floating.extend()
                            }
                        }
                    })
                }
            }else{
                binding.tvDataEmpty.isVisible = true
            }
        }
    }
    override fun onClick(listaMaestroReloj: ListaMaestroReloj) {
        val bundle = Bundle()
        bundle.putSerializable(DATA_KEY,listaMaestroReloj)
        fragment.findNavController().navigate(R.id.to_generalInformationDetailsFragment,bundle)
    }
}