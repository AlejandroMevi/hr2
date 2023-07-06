package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.ui

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
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui.GeneralInformationFragment
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models.ScheduleNegotiation
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants

class ScheduleNegotiationVPAdapter(
    private val fragment: Fragment,
    private val generalInformationFragment: GeneralInformationFragment?=null,
    private var listMasterFilter: MutableList<List<ScheduleNegotiation>>,
) : RecyclerView.Adapter<ViewHolderGeneral<*>>(),ScheduleNegotiationAdapter.OnClickListener{

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
    ) : ViewHolderGeneral<List<ScheduleNegotiation>>(binding.root) {
        override fun bind(listaMaestroReloj: List<ScheduleNegotiation>) {
            if (listaMaestroReloj.isNotEmpty()){
                binding.tvDataEmpty.isVisible = false
                binding.rvGeneralInformation.adapter = ScheduleNegotiationAdapter(listaMaestroReloj,this@ScheduleNegotiationVPAdapter)
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
    override fun onClick(scheduleNegotiation: ScheduleNegotiation) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.DATA_KEY,scheduleNegotiation)
        fragment.findNavController().navigate(R.id.to_scheduleNegotiationDetailsFragment,bundle)
    }
}