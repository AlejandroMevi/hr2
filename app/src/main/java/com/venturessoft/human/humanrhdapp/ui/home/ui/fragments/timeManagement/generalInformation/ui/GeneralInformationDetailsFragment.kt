package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui

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
import com.venturessoft.human.humanrhdapp.databinding.FragmentTimeManagmentBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.SpinnerCatalog
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.vm.TimeManagementVM
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.InfoGenralRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.ListaMaestroReloj
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui.vm.GeneralInformationVM
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.DATA_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.formatYearMonthDay
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.getDateLocal
import es.dmoral.toasty.Toasty
class GeneralInformationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTimeManagmentBinding
    private val welcomeVM: WelcomeFragmentViewModel by activityViewModels()
    private val timeManagementVM: TimeManagementVM by activityViewModels()
    private val generalInformationVM: GeneralInformationVM by activityViewModels()
    private lateinit var spinnerCatalog: SpinnerCatalog
    private var listaMaestroReloj: ListaMaestroReloj? = null
    private var pickers: Pickers = Pickers()
    private val dataRequest = InfoGenralRequest()
    private var mainInterface: MainInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            listaMaestroReloj = bundle.getSerializable(DATA_KEY) as ListaMaestroReloj
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTimeManagmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHintSpinner()
        spinnerCatalog = SpinnerCatalog(timeManagementVM, this)
        welcomeVM.idMenu.observe(viewLifecycleOwner) { dataUser ->
            binding.tvNameEmployee.text = dataUser.nombreCompleto
            dataRequest.numEmp = dataUser.numEmp
            if (listaMaestroReloj !=null){
                spinnerCatalog.getCatalogoCalendar(binding.spCalendar.spinner,listaMaestroReloj?.calendario)
                if(listaMaestroReloj?.rolHorario != 0){
                    binding.rgType.check(R.id.rb_role)
                    spinnerCatalog.getCatalogoRolService(binding.spRol.spinner,listaMaestroReloj?.rolHorario)
                }
                if(listaMaestroReloj?.turno != 0){
                    binding.rgType.check(R.id.rb_shift)
                    spinnerCatalog.getCatalogoRolService(binding.spRol.spinner,listaMaestroReloj?.turno)
                }
                binding.tvTitle.text = getString(R.string.label_edit_solicitud)
                binding.btnSave.button.text = getString(R.string.edit)
                binding.btnDate.text =  formatYearMonthDay(listaMaestroReloj?.fechaAplicacion?:"").toString()
                listaMaestroReloj?.gafete?.let { binding.etBadge.setText(it.toString()) }
                binding.cbLun.isChecked = listaMaestroReloj?.lun == "S"
                binding.cbMar.isChecked = listaMaestroReloj?.mar == "S"
                binding.cbMie.isChecked = listaMaestroReloj?.mie == "S"
                binding.cbJue.isChecked = listaMaestroReloj?.jue == "S"
                binding.cbVie.isChecked = listaMaestroReloj?.vie == "S"
                binding.cbSab.isChecked = listaMaestroReloj?.sab == "S"
                binding.cbDom.isChecked = listaMaestroReloj?.dom == "S"
            }else{
                spinnerCatalog.getCatalogoCalendar(binding.spCalendar.spinner)
                spinnerCatalog.getCatalogoRolService(binding.spRol.spinner)
                binding.tvTitle.text = getString(R.string.label_solicitud)
                binding.btnSave.button.text = getString(R.string.save)
                binding.btnDate.text = getDateLocal()
            }
        }
        binding.rgType.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.rb_role ->{
                    spinnerCatalog.getCatalogoRolService(binding.spRol.spinner)
                    binding.spRol.inputLayout.hint = getString(R.string.label_rol)
                }
                R.id.rb_shift -> {
                    spinnerCatalog.getCatalogoTurnoService(binding.spRol.spinner)
                    binding.spRol.inputLayout.hint = getString(R.string.label_turno)
                }
            }
        }
        binding.btnDate.setOnClickListener {
            pickers.dataPickerFrom(
                this,
                binding.btnDate, 1, "yyyy-MM-dd"
            )
        }
        binding.btnSave.button.setOnClickListener {
            statusObserve()
            if (listaMaestroReloj !=null){
                generalInformationVM.editAdministrarMR(dataRequest.numEmp.toLong(),binding.btnDate.text.toString(),getData())
            }else{
                generalInformationVM.addAdministrarMR(getData())
            }
        }
    }
    private fun setHintSpinner(){
        binding.spCalendar.inputLayout.hint = getString(R.string.label_calendario)
        binding.spRol.inputLayout.hint = getString(R.string.label_rol)
    }
    private fun getData(): InfoGenralRequest {
        dataRequest.cia = User.numCia.toString()
        dataRequest.fechaAplicacion = binding.btnDate.text.toString()
        dataRequest.gafete = binding.etBadge.text.toString()
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