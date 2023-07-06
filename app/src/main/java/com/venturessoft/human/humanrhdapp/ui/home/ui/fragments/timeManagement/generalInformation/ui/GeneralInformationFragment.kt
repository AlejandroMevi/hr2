package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.databinding.DialogStationsBinding
import com.venturessoft.human.humanrhdapp.databinding.FragmentGeneralInformationBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.DateIndicator
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.ListaMaestroReloj
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui.vm.GeneralInformationVM
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.setformatYearMonthDay
import java.util.*
class GeneralInformationFragment : Fragment(){
    lateinit var binding: FragmentGeneralInformationBinding
    private val fromBottom: android.view.animation.Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: android.view.animation.Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
    private var mainInterface: MainInterface? = null
    private val welcomeVM: WelcomeFragmentViewModel by activityViewModels()
    private val generalInformationVM: GeneralInformationVM by activityViewModels()
    private var listMaster = mutableListOf<ListaMaestroReloj>()
    private val listMasterFilter = mutableListOf<List<ListaMaestroReloj>>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGeneralInformationBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateIndicator = DateIndicator(requireContext(), binding.btnDate, binding.tvBeforeMonth, binding.tvAfterMonth, binding.vpGeneralInformation)
        DateIndicator.dateSelected.value = setformatYearMonthDay(calendar.time)

        welcomeVM.idMenu.observeOnce(viewLifecycleOwner){user->
            generalInformationVM.getMR(user.numEmp.toLong())
            statusObserve()
        }
        binding.btnDate.setOnClickListener {
            dateIndicator.createDialogWithoutDateField()
        }
        binding.floating.setOnClickListener {
            onAddButton()
        }
        binding.floatingActionButton1.setOnClickListener {
            findNavController().navigate(R.id.to_generalInformationDetailsFragment)
        }
        binding.floatingActionButton2.setOnClickListener {
            showDialog()
        }
        generalInformationVM.dataMR.observeOnce(viewLifecycleOwner){dataResponce->
            if (dataResponce!=null){
                listMaster = dataResponce.listaMaestroReloj.toMutableList()
                filterData()
                binding.vpGeneralInformation.setCurrentItem(calendar.get(Calendar.MONTH), false)
            }
        }
        binding.vpGeneralInformation.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dateIndicator.setDate(position)
            }
        })
    }
    private fun filterData() {
        DateIndicator.dateSelected.observe(viewLifecycleOwner){date->
            listMasterFilter.clear()
            val listYear = mutableListOf<ListaMaestroReloj>()
            listMaster.forEach {maestroReloj ->
                if (Utilities.formatYear(maestroReloj.fechaAplicacion) == date){
                    listYear.add(maestroReloj)
                }
            }
            for (month in 1..12) {
                val listMonth = mutableListOf<ListaMaestroReloj>()
                listYear.forEach {
                    if (Utilities.formatMonth(it.fechaAplicacion) == month.toString()){
                        listMonth.add(it)
                    }
                }
                listMasterFilter.add(listMonth)
            }
            binding.vpGeneralInformation.adapter = GeneralInformationVPAdapter(this,this,listMasterFilter)
        }
    }
    private fun statusObserve() {
        generalInformationVM.status.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                    }
                    is ApiResponceStatus.Success -> {
                        clearService()
                    }
                    is ApiResponceStatus.Error -> {
                        clearService()
                    }
                }
            }
        }
    }
    private fun clearService() {
        generalInformationVM.status.removeObservers(viewLifecycleOwner)
        generalInformationVM.status.value = null
        mainInterface?.showLoading(false)
    }
    fun onAddButton() {
        if (!binding.clFloatingButton.isVisible) {
            binding.clFloatingButton.startAnimation(fromBottom)
        } else {
            binding.clFloatingButton.startAnimation(toBottom)
        }
        binding.clFloatingButton.isVisible = !binding.clFloatingButton.isVisible
    }
    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustom)
        val bind: DialogStationsBinding = DialogStationsBinding.inflate(layoutInflater)
        dialog.setView(bind.root)
        dialog.setCancelable(true)
        dialog.show()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInterface) {
            mainInterface = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}