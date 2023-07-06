package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.Pickers
import com.venturessoft.human.humanrhdapp.databinding.FragmentScheduleNegotiationDetailsBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.SpinnerCatalog
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.vm.TimeManagementVM
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.InfoGenralRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui.vm.GeneralInformationVM
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models.ScheduleNegotiation
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import es.dmoral.toasty.Toasty

class ScheduleNegotiationDetailsFragment : Fragment() {

    private lateinit var binding:FragmentScheduleNegotiationDetailsBinding
    private val welcomeVM: WelcomeFragmentViewModel by activityViewModels()
    private val timeManagementVM: TimeManagementVM by activityViewModels()
    private val generalInformationVM: GeneralInformationVM by activityViewModels()
    private lateinit var spinnerCatalog: SpinnerCatalog
    private var scheduleNegotiation: ScheduleNegotiation? = null
    private var pickers: Pickers = Pickers()
    private val dataRequest = InfoGenralRequest()
    private var mainInterface: MainInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            scheduleNegotiation = bundle.getSerializable(Constants.DATA_KEY) as ScheduleNegotiation
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentScheduleNegotiationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHintSpinner()
        spinnerCatalog = SpinnerCatalog(timeManagementVM, this)
        welcomeVM.idMenu.observe(viewLifecycleOwner) { dataUser ->
            binding.tvNameEmployee.text = dataUser.nombreCompleto
            dataRequest.numEmp = dataUser.numEmp
            if (scheduleNegotiation !=null){
                try {
                    spinnerCatalog.getCatalogoDepartament(binding.spDep.spinner,scheduleNegotiation?.departamento?.toInt())
                }catch (_:Exception){
                    spinnerCatalog.getCatalogoDepartament(binding.spDep.spinner)
                }
                spinnerCatalog.getCatalogoCategory(binding.spCategory.spinner,scheduleNegotiation?.categoria)
                spinnerCatalog.getCatalogoZonaService(binding.spZone.spinner,scheduleNegotiation?.zona.toString())
                if(scheduleNegotiation?.rolTurno != 0){
                    binding.rgType.check(R.id.rb_role)
                    spinnerCatalog.getCatalogoRolService(binding.spRol.spinner,scheduleNegotiation?.rolTurno)
                }
                if(scheduleNegotiation?.turno != 0){
                    binding.rgType.check(R.id.rb_shift)
                    spinnerCatalog.getCatalogoRolService(binding.spRol.spinner,scheduleNegotiation?.turno)
                }
                binding.tvTitle.text = getString(R.string.label_edit_negociacion)
                binding.btnSave.button.text = getString(R.string.edit)
                binding.btnDateStart.text =  Utilities.formatYearMonthDay(scheduleNegotiation?.fechaInicio ?: "").toString()
                binding.btnDateEnd.text =  Utilities.formatYearMonthDay(scheduleNegotiation?.fechaFin ?: "").toString()
                binding.cbLun.isChecked = scheduleNegotiation?.lun == "S"
                binding.cbMar.isChecked = scheduleNegotiation?.mar == "S"
                binding.cbMie.isChecked = scheduleNegotiation?.mie == "S"
                binding.cbJue.isChecked = scheduleNegotiation?.jue == "S"
                binding.cbVie.isChecked = scheduleNegotiation?.vie == "S"
                binding.cbSab.isChecked = scheduleNegotiation?.sab == "S"
                binding.cbDom.isChecked = scheduleNegotiation?.dom == "S"
            }else{
                spinnerCatalog.getCatalogoDepartament(binding.spDep.spinner)
                spinnerCatalog.getCatalogoCategory(binding.spCategory.spinner)
                spinnerCatalog.getCatalogoZonaService(binding.spZone.spinner)
                spinnerCatalog.getCatalogoRolService(binding.spRol.spinner)
                binding.tvTitle.text = getString(R.string.label_negociacion)
                binding.btnSave.button.text = getString(R.string.save)
                binding.btnDateStart.text = Utilities.getDateLocal()
                binding.btnDateEnd.text = Utilities.getDateLocal()
            }
        }
        binding.rgType.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.rb_role ->{
                    spinnerCatalog.getCatalogoRolService(binding.spRol.spinner)
                    binding.spRol.inputLayout.hint = getString(R.string.label_rol)
                }
                R.id.rb_shift ->{
                    spinnerCatalog.getCatalogoTurnoService(binding.spRol.spinner)
                    binding.spRol.inputLayout.hint = getString(R.string.label_turno)
                }
            }
        }
        binding.btnDateStart.setOnClickListener {
            pickers.dataPickerFrom(
                this,
                binding.btnDateStart, 1,"yyyy-MM-dd"
            )
        }
        binding.btnDateEnd.setOnClickListener {
            pickers.dataPickerFrom(
                this,
                binding.btnDateEnd,0, "yyyy-MM-dd"
            )
        }
        binding.btnSave.button.setOnClickListener {
            statusObserve()
        }
    }
    private fun setHintSpinner(){
        binding.spDep.inputLayout.hint = getString(R.string.label_departamento)
        binding.spCategory.inputLayout.hint = getString(R.string.label_categorias)
        binding.spZone.inputLayout.hint = getString(R.string.label_zona)
        binding.spRol.inputLayout.hint = getString(R.string.label_rol)
    }
    private fun getData(): InfoGenralRequest {
        dataRequest.cia = User.numCia.toString()
/*        dataRequest.fechaAplicacion = binding.bt.text.toString()
        dataRequest.gafete = binding.etBadge.text.toString()*/
        dataRequest.calendario = SpinnerCatalog.calendar.toString()
        dataRequest.rolHorario = SpinnerCatalog.rol.toString()
        dataRequest.turno = SpinnerCatalog.turno.toString()
        dataRequest.dom = if(binding.cbDom.isChecked)"S" else "N"
        dataRequest.lun = if(binding.cbLun.isChecked)"S" else "N"
        dataRequest.mar = if(binding.cbMar.isChecked)"S" else "N"
        dataRequest.mie = if(binding.cbMie.isChecked)"S" else "N"
        dataRequest.jue = if(binding.cbJue.isChecked)"S" else "N"
        dataRequest.vie = if(binding.cbVie.isChecked)"S" else "N"
        dataRequest.sab = if(binding.cbSab.isChecked)"S" else "N"
        dataRequest.usuario = User.usuario
        return dataRequest
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
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
                        Toasty.custom(requireContext(), errorCode, ContextCompat.getDrawable(requireContext(), R.drawable.warning),
                            requireContext().getColor(R.color.principal),
                            Color.WHITE,
                            Toast.LENGTH_SHORT,
                            true,
                            true
                        ).show()
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