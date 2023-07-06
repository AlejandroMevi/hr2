package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.reportes.reportHolidayControlAusentismo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.*
import com.venturessoft.human.humanrhdapp.databinding.FragmentReportAbsenteeismControlBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.reportes.reportHolidayControlAusentismo.data.models.ReportRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.reportes.reportHolidayControlAusentismo.ui.vm.ReportHolidayCAViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.ListEmployeeAdapter
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.vm.ListEmployeeViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.SpinnerCatalog
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.vm.TimeManagementVM
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.PDF_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.STRING_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.TITULO_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce

class ReportAbsenteeismControlFragment : Fragment() {
    private lateinit var binding: FragmentReportAbsenteeismControlBinding
    private val reportHolidayCAViewModel: ReportHolidayCAViewModel by activityViewModels()
    private val timeManagementVM: TimeManagementVM by activityViewModels()
    private var ca: Calendario = Calendario()
    private var p: Pickers = Pickers()
    private var mainInterface: MainInterface? = null
    private var retardos: Boolean? = false
    private var programacion: Boolean = true
    private var salidas: Boolean = false
    private var origen: Boolean = false
    private var todosAnios: Boolean = false
    private var textToolbar = ""
    private lateinit var spinnerCatalog: SpinnerCatalog
    private val searchEmploye = SearchEmploye(ListEmployeeViewModel(), this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            textToolbar = bundle.getString(STRING_KEY, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportAbsenteeismControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCustomization()
        val listSpinners =
            listOf(binding.spArea.spinner, binding.spCentro.spinner, binding.spLinea.spinner)
        val dialog = DialogListEmploye(this, searchEmploye, binding.btnListEmployee)
        spinnerCatalog = SpinnerCatalog(timeManagementVM, this)
        spinnerCatalog.loadServiceArea(listSpinners)
        binding.btnListEmployee.setOnClickListener {
            dialog.showDialogList()
        }
        setHintSpinner()
    }

    private fun setHintSpinner() {
        binding.spArea.inputLayout.hint = getString(R.string.vrpcaArea)
        binding.spCentro.inputLayout.hint = getString(R.string.vrpcaCentro)
        binding.spLinea.inputLayout.hint = getString(R.string.vrpcaLinea)
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
            getString(R.string.menu_vacaciones) -> {
                binding.radioButtonProgramacion.isChecked = true
                binding.radioButtonSalidas.isChecked = false
                binding.radioAnios.isChecked = false
                programacion = true
                salidas = false
                todosAnios = false
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
        mainInterface = null
    }

    private fun loadService() {
        when (textToolbar) {
            getString(R.string.menu_incapacidades) -> {
                spinnerCatalog.supervisorService(binding.spSupervisor.spinner)
            }

            getString(R.string.menu_ausentismos) -> {
                spinnerCatalog.supervisorService(binding.spSupervisor.spinner)
            }

            getString(R.string.menu_permisos) -> {
                spinnerCatalog.supervisorService(binding.spSupervisor.spinner)
            }

            else -> {
                //
            }
        }
    }

    private fun viewCustomization() {
        with(binding) {
            binding.clRadioHolyday.isVisible = (textToolbar == getString(R.string.menu_vacaciones))
            titleReport.text = textToolbar
            dateFrom.setOnClickListener {
                p.dataPickerFrom(
                    this@ReportAbsenteeismControlFragment,
                    binding.dateFrom, 1, "dd/MM/yyyy"
                )
            }
            dateUp.setOnClickListener {
                p.dataPickerFrom(
                    this@ReportAbsenteeismControlFragment,
                    binding.dateUp, 2, "dd/MM/yyyy"
                )
            }
            radioButtonRetardos.setOnClickListener { radioButtonRetardos() }
            radioButtonProgramacion.setOnClickListener {
                binding.radioButtonSalidas.isChecked = !binding.radioButtonProgramacion.isChecked
                programacion = binding.radioButtonProgramacion.isChecked
                salidas = binding.radioButtonSalidas.isChecked
            }
            radioButtonSalidas.setOnClickListener {
                binding.radioButtonProgramacion.isChecked = !binding.radioButtonSalidas.isChecked
                programacion = binding.radioButtonProgramacion.isChecked
                salidas = binding.radioButtonSalidas.isChecked
            }
            radioAnios.setOnClickListener {
                todosAnios = binding.radioAnios.isChecked
                if (binding.radioAnios.isChecked) {
                    with(binding) {
                        textInicio.setBackgroundColor(requireContext().getColor(R.color.gris))
                        textFin.setBackgroundColor(requireContext().getColor(R.color.gris))
                        dateFrom.setBackgroundColor(requireContext().getColor(R.color.gris))
                        dateUp.setBackgroundColor(requireContext().getColor(R.color.gris))
                        captionFormatoDos.setBackgroundColor(requireContext().getColor(R.color.gris))
                        radioButtonSalidas.setBackgroundColor(requireContext().getColor(R.color.gris))
                        radioButtonProgramacion.setBackgroundColor(requireContext().getColor(R.color.gris))
                        dateFrom.isEnabled = false
                        dateUp.isEnabled = false
                        radioButtonSalidas.isClickable = false
                        radioButtonProgramacion.isClickable = false
                    }
                } else {
                    with(binding) {
                        textInicio.setBackgroundResource(0)
                        textFin.setBackgroundResource(0)
                        dateFrom.setBackgroundColor(requireContext().getColor(R.color.light_gray))
                        dateUp.setBackgroundColor(requireContext().getColor(R.color.light_gray))
                        captionFormatoDos.setBackgroundResource(0)
                        radioButtonSalidas.setBackgroundResource(0)
                        radioButtonProgramacion.setBackgroundResource(0)
                        dateFrom.isEnabled = true
                        dateUp.isEnabled = true
                        radioButtonSalidas.isClickable = true
                        radioButtonProgramacion.isClickable = true
                    }
                }
            }
            sendPdf.setOnClickListener {
                validate()
            }
            p.selecDefaultDateMonday(binding.dateFrom)
            p.selecDefaultDateSunday(binding.dateUp)
        }
    }

    private fun showViews() {
        when (textToolbar) {
            getString(R.string.menu_vacaciones) -> {
                with(binding) {
                    captionGeneral.isVisible = true
                    radioAnios.isVisible = true
                    captionFormatoDos.isVisible = true
                    radioButtonProgramacion.isVisible = true
                    radioButtonSalidas.isVisible = true
                }
            }

            getString(R.string.menu_incapacidades) -> {
                binding.spSupervisor.root.isVisible = true
            }

            getString(R.string.menu_ausentismos) -> {
                binding.spSupervisor.root.isVisible = true
            }

            getString(R.string.menu_permisos) -> {
                binding.spSupervisor.root.isVisible = true
            }
        }
    }

    private fun radioButtonRetardos() {
        retardos = binding.radioButtonRetardos.isChecked
    }


    private fun validate() {
        val validateDate =
            ca.validaFechaMenor(Pickers.fechaInicioValidate, Pickers.fechaFinValidate)
        if (validateDate == "correcto") {
            if (binding.radioButtonProgramacion.isChecked && binding.radioButtonSalidas.isChecked) {
                Utilities.loadMessageError(getString(R.string.rhca), requireActivity())
                binding.radioButtonProgramacion.isChecked = false
                binding.radioButtonSalidas.isChecked = false
            } else {
                var listUser: List<Long> = listOf(0)
                if (!ListEmployeeAdapter.listEmployeSelect.value.isNullOrEmpty()) {
                    listUser = ListEmployeeAdapter.listEmployeSelect.value!!
                }
                val areaCentroLinea = spinnerCatalog.getListSelect()
                val reportRequest = ReportRequest(
                    textToolbar,
                    User.token,
                    if (!ListEmployeeAdapter.listEmployeSelect.value.isNullOrEmpty()) listUser else null,
                    User.numCia,
                    Pickers.fechaFin,
                    Pickers.fechaInicio,
                    User.usuario,
                    if (textToolbar != getString(R.string.menu_ausentismos)) origen else null,
                    if (textToolbar == getString(R.string.menu_ausentismos)) retardos else null,
                    areaCentroLinea[0].key,
                    areaCentroLinea[1].key,
                    areaCentroLinea[2].key,
                    if (textToolbar == getString(R.string.menu_vacaciones)) todosAnios else null,
                    if (textToolbar == getString(R.string.menu_vacaciones)) programacion else null,
                    if (textToolbar == getString(R.string.menu_vacaciones)) salidas else null,
                    when (textToolbar) {
                        getString(R.string.menu_incapacidades) -> SpinnerCatalog.supervisor
                        getString(R.string.menu_ausentismos) -> SpinnerCatalog.supervisor
                        getString(R.string.menu_permisos) -> SpinnerCatalog.supervisor
                        else -> {
                            null
                        }
                    }
                )
                corutineReportHolidayService(reportRequest)
            }
        } else {
            Toast.makeText(requireActivity(), getString(R.string.rangoDeFechas), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun statusObserve() {
        reportHolidayCAViewModel.status.observe(viewLifecycleOwner) { status ->
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
                        Toast.makeText(requireActivity(), errorCode, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun corutineReportHolidayService(reportRequest: ReportRequest) {
        reportHolidayCAViewModel.dataReportVacacionesCa.value = null
        reportHolidayCAViewModel.getReportVacacionesCa(reportRequest, requireContext())
        statusObserve()
        reportHolidayCAViewModel.dataReportVacacionesCa.observeOnce(viewLifecycleOwner) { reportVacaciones ->
            if (reportVacaciones != null) {
                val bundle = Bundle()
                if (reportVacaciones.pdfByte.isNotEmpty()) {
                    bundle.putString(PDF_KEY, reportVacaciones.pdfByte)
                    bundle.putString(TITULO_KEY, textToolbar)
                } else if (reportVacaciones.pdf.isNotEmpty()) {
                    bundle.putString(PDF_KEY, reportVacaciones.pdf)
                    bundle.putString(TITULO_KEY, textToolbar)
                } else {
                    bundle.putString(PDF_KEY, "")
                }
                findNavController().navigate(R.id.to_p_d_f_VIewerFragment, bundle)
            }
        }
    }

    private fun clearService() {
        reportHolidayCAViewModel.status.value = null
        reportHolidayCAViewModel.status.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }
}