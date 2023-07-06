package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.databinding.FragmentNegociacionHorariosBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.DateIndicator
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models.ScheduleNegotiation
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.ui.vm.NegociacionHorarioViewModel
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce
import java.util.*

class ScheduleNegotiationFragment : Fragment() {

    lateinit var binding: FragmentNegociacionHorariosBinding
    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
    private var mainInterface: MainInterface? = null
    private val welcomeVM: WelcomeFragmentViewModel by activityViewModels()
    private val negociacionHorarioViewModel: NegociacionHorarioViewModel by activityViewModels()
    private var listSchedule = mutableListOf<ScheduleNegotiation>()
    private val listScheduleFilter = mutableListOf<List<ScheduleNegotiation>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNegociacionHorariosBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateIndicator = DateIndicator(requireContext(), binding.btnDate, binding.tvBeforeMonth, binding.tvAfterMonth, binding.vpGeneralInformation)
        DateIndicator.dateSelected.value = Utilities.setformatYearMonthDay(calendar.time)

        welcomeVM.idMenu.observeOnce(viewLifecycleOwner){user->
            negociacionHorarioViewModel.getScheduleNegotiation(user.numEmp.toLong(),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR))
            statusObserve()
        }
        binding.btnDate.setOnClickListener {
            dateIndicator.createDialogWithoutDateField()
        }
        binding.floating.setOnClickListener {
            findNavController().navigate(R.id.to_scheduleNegotiationDetailsFragment)
        }
        negociacionHorarioViewModel.dataScheduleNegotiation.observeOnce(viewLifecycleOwner){dataResponce->
            if (dataResponce!=null){
                listSchedule = dataResponce.sLista.toMutableList()
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
            listScheduleFilter.clear()
            val listYear = mutableListOf<ScheduleNegotiation>()
            listSchedule.forEach {maestroReloj ->
                if (Utilities.formatYear(maestroReloj.fechaInicio) == date){
                    listYear.add(maestroReloj)
                }
            }
            for (month in 1..12) {
                val listMonth = mutableListOf<ScheduleNegotiation>()
                listYear.forEach {
                    if (Utilities.formatMonth(it.fechaInicio) == month.toString()){
                        listMonth.add(it)
                    }
                }
                listScheduleFilter.add(listMonth)
            }
            binding.vpGeneralInformation.adapter = ScheduleNegotiationVPAdapter(this,null,listScheduleFilter)
        }
    }
    private fun statusObserve() {
        negociacionHorarioViewModel.status.observe(viewLifecycleOwner) { status ->
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
        negociacionHorarioViewModel.status.removeObservers(viewLifecycleOwner)
        negociacionHorarioViewModel.status.value = null
        mainInterface?.showLoading(false)
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