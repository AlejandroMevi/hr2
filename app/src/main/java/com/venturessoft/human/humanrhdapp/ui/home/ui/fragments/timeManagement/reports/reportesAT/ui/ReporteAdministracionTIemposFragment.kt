package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.*
import com.venturessoft.human.humanrhdapp.databinding.FragmentReporteAdministracionTIemposBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.ListEmployeeAdapter
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.vm.ListEmployeeViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.SpinnerCatalog
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.SpinnerCatalog.Companion.codid
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.vm.TimeManagementVM
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.data.models.ReporteAdmiTiemposRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.ui.vm.ReporteAdministracionTiemposViewModel
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.PDF_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.TITULO_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce
import es.dmoral.toasty.Toasty
class ReporteAdministracionTIemposFragment : Fragment() {
    private lateinit var binding: FragmentReporteAdministracionTIemposBinding
    private val reporteAdministracionTiempos = ReporteAdministracionTiemposViewModel()
    private var ca: Calendario = Calendario()
    private var p: Pickers = Pickers()
    private var mainInterface: MainInterface? = null
    private var retardos: Boolean? = false
    private var empInac: Boolean? = false
    private var mrAct: Boolean? = false
    private var impXemp: Boolean? = false
    private var totDep: Boolean? = true
    private var descEmp: Boolean? = false
    private var textToolbar = ""
    private val listEmployeeViewModel = ListEmployeeViewModel()
    private val searchEmploye = SearchEmploye(listEmployeeViewModel, this)
    private val timeManagementVM: TimeManagementVM by activityViewModels()
    private lateinit var spinnerCatalog: SpinnerCatalog
    private lateinit var conceptsService: ConceptsService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            textToolbar = bundle.getString(Constants.STRING_KEY, "")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReporteAdministracionTIemposBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        conceptsService = ConceptsService(timeManagementVM, this)
        viewCustomization()
        val listSpinners = listOf(binding.spArea.spinner, binding.spCentro.spinner, binding.spLinea.spinner)
        spinnerCatalog = SpinnerCatalog(timeManagementVM, this)
        spinnerCatalog.loadServiceArea(listSpinners)
        val dialog = DialogListEmploye(this, searchEmploye, binding.btnListEmployee)
        binding.btnListEmployee.setOnClickListener {
            dialog.showDialogList()
        }
        setHintSpinner()
    }
    private fun setHintSpinner(){
        binding.spArea.inputLayout.hint = getString(R.string.vrpcaArea)
        binding.spCentro.inputLayout.hint = getString(R.string.vrpcaCentro)
        binding.spLinea.inputLayout.hint = getString(R.string.vrpcaLinea)
        binding.spDialogZona.inputLayout.hint = getString(R.string.label_zona)
        binding.spDialogCodid.inputLayout.hint = getString(R.string.codid)
        binding.spDialogTurno.inputLayout.hint = getString(R.string.ATTurno)
        binding.spDialogRol.inputLayout.hint = getString(R.string.ATRol)
        binding.spDialogTipoRep.inputLayout.hint = getString(R.string.ATTipo_de_reporte)
        binding.spSupervisor.inputLayout.hint = getString(R.string.supervisor)
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.submenu_7))
        loadService()
        showViews()
    }
    override fun onPause() {
        super.onPause()
        when (textToolbar) {
            getString(R.string.ATentradas_salidas) -> {
                binding.radioEI.isChecked = false
                binding.radioIPE.isChecked = false
                binding.radioMR.isChecked = false
                empInac = false
                impXemp = false
                mrAct = false
            }

            getString(R.string.ATentradas_salidas_x_concepto) -> {
                binding.ilConcepto.text = getString(R.string.conceptos)
                binding.radioDescripcionEmp.isChecked = false
                binding.radioTotal.isChecked = true
                descEmp = false
                totDep = true
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInterface) {
            mainInterface = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        ListEmployeeAdapter.listEmployeSelect.value = listOf()
        mainInterface = null
    }
    private fun loadService() {
        when (textToolbar) {
            getString(R.string.ATAsistencia_Organigrama) -> {
                spinnerCatalog.getCatalogoZonaService(binding.spDialogZona.spinner)
                spinnerCatalog.getCatalogoTurnoService(binding.spDialogTurno.spinner)
                spinnerCatalog.getCatalogoRolService(binding.spDialogRol.spinner)
            }
            getString(R.string.ATentradas_salidas_x_concepto) -> {
                conceptsService.catalogoConceptosService(binding.ilConcepto)
                spinnerCatalog.supervisorService(binding.spSupervisor.spinner)
            }
            getString(R.string.ATentradas_salidas) -> {
                spinnerCatalog.supervisorService(binding.spSupervisor.spinner)
            }
            getString(R.string.ATDias_por_Disfrutar) -> {
                spinnerCatalog.codidService(binding.spDialogCodid.spinner)
            }
        }
    }
    private fun viewCustomization() {
        with(binding) {
            titleReport.text = textToolbar
            dateFrom.setOnClickListener {
                p.dataPickerFrom(
                    this@ReporteAdministracionTIemposFragment,
                    binding.dateFrom, 1,"dd-MM-yyyy"
                )
            }
            dateUp.setOnClickListener {
                p.dataPickerFrom(
                    this@ReporteAdministracionTIemposFragment,
                    binding.dateUp, 2, "dd-MM-yyyy"
                )
            }
            radioButtonRetardos.setOnClickListener {
                retardos = binding.radioButtonRetardos.isChecked
            }
            radioEI.setOnClickListener {
                empInac = binding.radioEI.isChecked
            }
            radioIPE.setOnClickListener {
                impXemp = binding.radioIPE.isChecked
            }
            radioMR.setOnClickListener {
                mrAct = binding.radioMR.isChecked
            }
            radioTotal.setOnClickListener {
                binding.radioDescripcionEmp.isChecked = !binding.radioTotal.isChecked
                totDep = binding.radioTotal.isChecked
                descEmp = binding.radioDescripcionEmp.isChecked
            }
            radioDescripcionEmp.setOnClickListener {
                binding.radioTotal.isChecked = !binding.radioDescripcionEmp.isChecked
                totDep = binding.radioTotal.isChecked
                descEmp = binding.radioDescripcionEmp.isChecked
            }
            sendPdf.setOnClickListener {
                validate()
            }
            ilAnio.setOnClickListener {
                p.dialogYear(requireContext(), ilAnio)
            }
            p.selecDefaultDateMonday(binding.dateFrom)
            p.selecDefaultDateSunday(binding.dateUp)
        }
    }
    private fun showViews() {
        when (textToolbar) {
            getString(R.string.ATAsistencia_Organigrama) -> {
                with(binding) {
                    textView23.isVisible = true
                    radioGroup.isVisible = true
                    spDialogZona.root.isVisible = true
                    spDialogTurno.root.isVisible = true
                    spDialogRol.root.isVisible = true
                }
            }
            getString(R.string.ATControl_Asistencia) -> {
                with(binding) {
                    radioButtonRetardos.isVisible = false
                    captionFormato.isVisible = false
                    spDialogTipoRep.root.isVisible = true
                }
            }
            getString(R.string.ATDias_por_Disfrutar) -> {
                with(binding) {
                    radioButtonRetardos.isVisible = false
                    captionFormato.isVisible = false
                    captionReport.isVisible = false
                    textInicio.isVisible = false
                    textFin.isVisible = false
                    dateFrom.isVisible = false
                    dateUp.isVisible = false
                    layoutAniio.isVisible = true
                    spDialogCodid.root.isVisible = true
                }
            }
            getString(R.string.ATentradas_salidas) -> {
                with(binding) {
                    captionFormato.isVisible = false
                    radioButtonRetardos.isVisible = false
                    forma.isVisible = true
                    radioIPE.isVisible = true
                    radioEI.isVisible = true
                    radioMR.isVisible = true
                    spSupervisor.root.isVisible = true
                }
            }
            getString(R.string.ATentradas_salidas_x_concepto) -> {
                with(binding) {
                    radioButtonRetardos.isVisible = false
                    radioDescripcionEmp.isVisible = true
                    radioTotal.isVisible = true
                    captionConcepto.isVisible = true
                    ilConcepto.isVisible = true
                    spSupervisor.root.isVisible = true
                }
            }
            getString(R.string.ATHoras_laboradas) -> {
                with(binding) {
                    radioButtonCuenta.isVisible = true
                    radioButtonRetardos.isVisible = false
                }
            }
            getString(R.string.ATSeguridad_Vigilancia) -> {
                with(binding) {
                    radioNegoc.isVisible = true
                    radioTiempoExtra.isVisible = true
                    radioButtonRetardos.isVisible = false
                }
            }
        }
    }
    private fun String.formatoDiag(): String {
        return this.replace("-", "/")
    }
    private fun validate() {
        val validateDate = ca.validaFechaMenor(Pickers.fechaInicioValidate, Pickers.fechaFinValidate)
        if (validateDate == "correcto") {
            var listUser: List<Long> = listOf(0)
            val anioSelec: Int = if (binding.ilAnio.text.toString() == "Selecciona el año"
            ) ca.getAnio() else binding.ilAnio.text.toString().toInt()
            if (!ListEmployeeAdapter.listEmployeSelect.value.isNullOrEmpty()) {
                listUser = ListEmployeeAdapter.listEmployeSelect.value!!
            }
            val areaCentroLinea = spinnerCatalog.getListSelect()
            val reportRequest = ReporteAdmiTiemposRequest(
                User.token,
                if (!ListEmployeeAdapter.listEmployeSelect.value.isNullOrEmpty()) listUser else null,
                User.numCia,
                when (textToolbar) {
                    getString(R.string.ATentradas_salidas_x_concepto) -> Pickers.fechaFinService.formatoDiag()
                    getString(R.string.ATentradas_salidas) -> Pickers.fechaFinService.formatoDiag()
                    else -> {
                        Pickers.fechaFinService
                    }
                },
                when (textToolbar) {
                    getString(R.string.ATentradas_salidas_x_concepto) -> Pickers.fechaInicioService.formatoDiag()
                    getString(R.string.ATentradas_salidas) -> Pickers.fechaInicioService.formatoDiag()
                    else -> {
                        Pickers.fechaInicioService
                    }
                },
                User.usuario,
                when (textToolbar) {
                    getString(R.string.ATAusentismo) -> retardos
                    else -> {
                        null
                    }
                },
                areaCentroLinea[0].key,
                areaCentroLinea[1].key,
                areaCentroLinea[2].key,
                if (textToolbar == getString(R.string.ATControl_Asistencia)) "a" else null,
                if (textToolbar == getString(R.string.ATAsistencia_Organigrama)) SpinnerCatalog.zona else null,
                if (textToolbar == getString(R.string.ATAsistencia_Organigrama)) SpinnerCatalog.turno else null,
                if (textToolbar == getString(R.string.ATAsistencia_Organigrama)) SpinnerCatalog.rol else null,
                if (textToolbar == getString(R.string.ATentradas_salidas)) empInac else null,
                if (textToolbar == getString(R.string.ATentradas_salidas)) mrAct else null,
                if (textToolbar == getString(R.string.ATentradas_salidas)) impXemp else null,
                if (textToolbar == getString(R.string.ATentradas_salidas_x_concepto)) totDep else null,
                if (textToolbar == getString(R.string.ATentradas_salidas_x_concepto)) descEmp else null,
                if (textToolbar == getString(R.string.ATentradas_salidas_x_concepto)) Pickers.keySelect else null,
                when (textToolbar) {
                    getString(R.string.ATentradas_salidas_x_concepto) -> SpinnerCatalog.supervisor
                    getString(R.string.ATentradas_salidas) -> SpinnerCatalog.supervisor
                    else -> {
                        null
                    }
                },
                if (textToolbar == getString(R.string.ATDias_por_Disfrutar)) codid else null,
                if (textToolbar == getString(R.string.ATDias_por_Disfrutar)) anioSelec else null
            )
            corutineReportesService(textToolbar, reportRequest)
        } else {
            Toast.makeText(requireActivity(), getString(R.string.rangoDeFechas), Toast.LENGTH_SHORT).show()
        }
    }
    private fun corutineReportesService(typeReport: String, reportRequest: ReporteAdmiTiemposRequest) {
        reporteAdministracionTiempos.data.value = null
        reporteAdministracionTiempos.reportesAdmiTiempos(typeReport, reportRequest, requireContext())
        statusObserve()
        reporteAdministracionTiempos.data.observeOnce(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.pdfByte.isNotEmpty()) {
                    val bundle = Bundle()
                    bundle.putString(PDF_KEY, response.pdfByte)
                    bundle.putString(TITULO_KEY, textToolbar)
                    findNavController().navigate(R.id.to_p_d_f_VIewerFragment, bundle)
                    reporteAdministracionTiempos.data.value = null
                }
            }
        }
    }
    private fun statusObserve() {
        reporteAdministracionTiempos.status.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                    }
                    is ApiResponceStatus.Success -> {
                        clearService()
                        Pickers.keySelect.clear()
                    }
                    is ApiResponceStatus.Error -> {
                        clearService()
                        cleanView()
                        Pickers.keySelect.clear()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
                        if (textToolbar == getString(R.string.ATentradas_salidas_x_concepto)) {
                            Utilities.loadMessageError(errorCode, requireActivity())
                        } else {
                            Toasty.custom(
                                requireContext(),
                                errorCode,
                                ContextCompat.getDrawable(requireContext(), R.drawable.warning),
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
    }
    private fun clearService() {
        reporteAdministracionTiempos.status.value = null
        reporteAdministracionTiempos.status.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }
    private fun cleanView(){
        when (textToolbar) {
            getString(R.string.ATentradas_salidas) -> {
                binding.radioEI.isChecked = false
                binding.radioIPE.isChecked = false
                binding.radioMR.isChecked = false
                empInac = false
                impXemp = false
                mrAct = false
            }

            getString(R.string.ATentradas_salidas_x_concepto) -> {
                binding.ilConcepto.text = getString(R.string.conceptos)
                binding.radioDescripcionEmp.isChecked = false
                binding.radioTotal.isChecked = true
                descEmp = false
                totDep = true
            }
        }
    }
}