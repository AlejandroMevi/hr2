package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.databinding.FragmentPreautorizacionTiemposBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models.PreautorizacionRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.ui.vm.PreautorizacionVM
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.*

class PreautorizacionTiemposFragment : Fragment() {

    private lateinit var binding: FragmentPreautorizacionTiemposBinding
    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
    private var mainInterface: MainInterface? = null
    private val welcomeVM: WelcomeFragmentViewModel by activityViewModels()
    private val preautorizacionVM: PreautorizacionVM by activityViewModels()
    private val preautorizacionRequest = PreautorizacionRequest()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPreautorizacionTiemposBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preautorizacionRequest.cia = User.numCia.toInt()
        welcomeVM.idMenu.observeOnce(viewLifecycleOwner){employe->
            preautorizacionRequest.numEmp = employe.numEmp.toInt()
            setDate()
            statusObserve()
            binding.rvExtratime.layoutManager = LinearLayoutManager(requireContext())
            binding.rvExtratime.setHasFixedSize(true)
            preautorizacionVM.dataPreauthorization.observe(viewLifecycleOwner){preautorizacion->
                if (preautorizacion!= null){
                    if (preautorizacion.sTiempoExtra.isNotEmpty()){
                        binding.tvDataEmpty.isVisible = false
                        binding.rvExtratime.adapter = PreauthorizationAdapter(preautorizacion.sTiempoExtra)
                    }else{
                        binding.rvExtratime.adapter = PreauthorizationAdapter(listOf())
                        binding.tvDataEmpty.isVisible = true
                    }
                }else{
                    binding.rvExtratime.adapter = PreauthorizationAdapter(listOf())
                    binding.tvDataEmpty.isVisible = true
                }
            }
        }
        binding.btnDate.setOnClickListener {
            showCalendarMonthYear()
        }
    }

    private fun showCalendarMonthYear() {
        val builder = MonthPickerDialog.Builder(context, { _, _ -> }, Calendar.YEAR, Calendar.MONTH)
        builder
            .setActivatedMonth(calendar.get(Calendar.MONTH))
            .setMinYear(calendar.get(Calendar.YEAR) - 5)
            .setActivatedYear(calendar.get(Calendar.YEAR))
            .setMaxYear(calendar.get(Calendar.YEAR) + 5)
            .setMinMonth(Calendar.JANUARY)
            .setTitle("Seleccione una Fecha")
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
            .setOnMonthChangedListener { month ->
                calendar.set(Calendar.MONTH, month)
                setDate()
            }.setOnYearChangedListener { year ->
                calendar.set(Calendar.YEAR, year)
                setDate()
            }.build().show()
    }

    @SuppressLint("SetTextI18n")
    private fun setDate() {
        binding.btnDate.text = "${getMonthName(calendar.get(Calendar.MONTH))}/${calendar.get(Calendar.YEAR)}"
        preautorizacionRequest.anio = calendar.get(Calendar.YEAR)
        preautorizacionRequest.mes = calendar.get(Calendar.MONTH)+1
        preautorizacionVM.addPreauTiempos(User.token,preautorizacionRequest)
    }

    private fun statusObserve() {
        preautorizacionVM.status.observe(viewLifecycleOwner) { status ->
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
        preautorizacionVM.status.removeObservers(viewLifecycleOwner)
        preautorizacionVM.status.value = null
        mainInterface?.showLoading(false)
    }

    private fun getMonthName(index: Int): String? {
        val calendar = Calendar.getInstance(Locale.US)
        calendar[Calendar.MONTH] = index
        calendar[Calendar.DAY_OF_MONTH] = 1
        return java.lang.String.format(Locale.US, "%tb", calendar)
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