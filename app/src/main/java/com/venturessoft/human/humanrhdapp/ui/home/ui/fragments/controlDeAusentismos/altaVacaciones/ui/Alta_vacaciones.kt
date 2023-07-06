package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.venturessoft.human.humanrhdapp.utilis.complements.getColorCompat
import com.venturessoft.human.humanrhdapp.utilis.complements.getDrawableCompat
import com.venturessoft.human.humanrhdapp.utilis.complements.makeVisible
import com.venturessoft.human.humanrhdapp.utilis.complements.setTextColorRes
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.Calendario
import com.venturessoft.human.humanrhdapp.databinding.CalendarDayLayoutBinding
import com.venturessoft.human.humanrhdapp.databinding.FragmentAltaVacacionesBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.MonthViewContainer
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.ui.vm.VacacionesFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.anualKardex.data.models.KardexAnualRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.anualKardex.ui.vm.KardexAnualViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.KardexMensual.data.models.DiasDescansosRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.KardexMensual.data.models.DiasFestivosRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.KardexMensual.ui.vm.KardexMensualViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.calendar.*
import es.dmoral.toasty.Toasty
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class alta_vacaciones : Fragment() {
    private lateinit var binding: FragmentAltaVacacionesBinding
    private val welcomeFragmentViewModel : WelcomeFragmentViewModel by activityViewModels()
    private var vacacionesFragmentViewModel = VacacionesFragmentViewModel()
    private val kardexMensualViewModel = KardexMensualViewModel()
    private val kardexAnualViewModel = KardexAnualViewModel()
    private var caC: CalendarioCustom = CalendarioCustom()
    private var g: GeneraMarcas = GeneraMarcas()
    private var ca: Calendario = Calendario()
    private var selection = DateSelection()
    private var names: String? = null
    private var numEmp: String? = null
    private val currentMonth = YearMonth.now()
    private val today = LocalDate.now()
    private var selectedDate: LocalDate? = null
    private var kardexMensualAusentismos: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualRetardos: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualVacaciones: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualFaltas: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualOthers: Map<LocalDate, List<CustomDay>>? = null
    private var descansos: Map<LocalDate, List<CustomDay>>? = null
    private var festivos: Map<LocalDate, List<CustomDay>>? = null
    private val formatoVista = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val formatoEnviado = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private var fechaInicio: String = ""
    private var fechaFin: String = ""
    private var mainInterface: MainInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAltaVacacionesBinding.inflate(inflater, container, false)
        welcomeFragmentViewModel.idMenu.observe(viewLifecycleOwner) {
            names = it.nombreCompleto
            numEmp = it.numEmp
            loadService()
            corutineGetVacacionesService()
        }
        return binding.root
    }

    private fun viewCustomization() {
        binding.txtDias2.text = getString(R.string.faDia)
        binding.txtNombre.text = names
        binding.idtxt.text = numEmp
        binding.btnSave.button.setOnClickListener { addVacacionesService() }
    }

    private fun loadService() {
        corutineGetKardexAnual(ca.getAnio().toString())
    }

    private fun corutineGetKardexAnual(anio: String) {
        kardexAnualViewModel.data.value = null
        val kardexAnualRequest = KardexAnualRequest(User.numCia.toString(), numEmp!!, anio, motivo = "Todos")
        kardexAnualViewModel.kardexAnual(User.token, kardexAnualRequest)
        kardexAnualViewModel.data.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val fecha = ArrayList<String>()
                    val marcas: ArrayList<String> = ArrayList()
                    for (i in 0 until response.skardexAnual.marca.size) {
                        response.skardexAnual.marca[i].fecha.let { fecha.add(it) }
                        response.skardexAnual.marca[i].marca.let { marcas.add(it) }
                    }
                    separateDayKardexMensual(fecha, marcas)
                    getDiasFestivosService(anio)
                }
            }
        }
        statusObserve()
    }

    private fun statusObserve() {
        kardexAnualViewModel.status.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                        Log.i("Progress", requireContext().javaClass.name)
                    }

                    is ApiResponceStatus.Success -> {
                        clearService()
                    }

                    is ApiResponceStatus.Error -> {
                        getDiasFestivosService(ca.getAnio().toString())
                        clearService()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
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

    private fun clearService() {
        kardexAnualViewModel.status.value = null
        kardexAnualViewModel.status.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }

    private fun separateDayKardexMensual(fechas: ArrayList<String>, marcas: ArrayList<String>) {
        val diaVaciones = caC.aDiaVacaciones(fechas, marcas)
        val mesVacaciones = caC.aMesesVacaciones(fechas, marcas)
        val diaAusentismos = caC.aDiaAusentismos(fechas, marcas)
        val mesAusentismos = caC.aMesesAusentismos(fechas, marcas)
        val diaRetardos = caC.aDiaRetardos(fechas, marcas)
        val mesRetardos = caC.aMesesRetardos(fechas, marcas)
        val diaOthers = caC.aDiaOthers(fechas, marcas)
        val mesOthers = caC.aMesesOthers(fechas, marcas)
        val diaFaltas = caC.aDiaFaltas(fechas, marcas)
        val mesFaltas = caC.aMesesFaltas(fechas, marcas)
        if (diaVaciones.isNotEmpty()) g.generateVacaciones(diaVaciones, mesVacaciones)
        if (diaAusentismos.isNotEmpty()) g.generateAusentismos(diaAusentismos, mesAusentismos)
        if (diaRetardos.isNotEmpty()) g.generateRetardos(diaRetardos, mesRetardos)
        if (diaOthers.isNotEmpty()) g.generateOthers(diaOthers, mesOthers)
        if (diaFaltas.isNotEmpty()) g.generateFaltas(diaFaltas, mesFaltas)

        kardexMensualVacaciones =
            g.generateVacaciones(
                g.listaKardex(diaVaciones), g.listaKardexMeses(mesVacaciones)
            ).groupBy { it.time.toLocalDate() }

        kardexMensualAusentismos =
            g.generateAusentismos(
                g.listaKardex(diaAusentismos), g.listaKardexMeses(mesAusentismos)
            ).groupBy { it.time.toLocalDate() }
        kardexMensualRetardos =
            g.generateRetardos(
                g.listaKardex(diaRetardos), g.listaKardexMeses(mesRetardos)
            ).groupBy { it.time.toLocalDate() }

        kardexMensualFaltas =
            g.generateFaltas(
                g.listaKardex(diaFaltas), g.listaKardexMeses(mesFaltas)
            ).groupBy { it.time.toLocalDate() }
        kardexMensualOthers =
            g.generateOthers(
                g.listaKardex(diaOthers), g.listaKardexMeses(mesOthers)
            ).groupBy { it.time.toLocalDate() }

    }

    private fun corutineGetVacacionesService() {
        vacacionesFragmentViewModel.dataGetVacaciones.value = null
        vacacionesFragmentViewModel.getVacaciones(
            User.token,
            User.numCia,
            numEmp!!.toLong(),
            ca.getAnio()
        )
        vacacionesFragmentViewModel.dataGetVacaciones.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    binding.txtFactor2.text = response.vacFactor
                    binding.txtNumberDiasDisfrutar.text = response.totPorDisf
                    binding.txtNumberDiasTomados.text = response.diasTot
                }
            }
        }
        statusObserveGetVacaciones()
    }

    private fun statusObserveGetVacaciones() {
        vacacionesFragmentViewModel.statusGetVacaciones.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                        Log.i("Progress", requireContext().javaClass.name)
                    }

                    is ApiResponceStatus.Success -> {
                        clearServiceGetVacaciones()
                    }

                    is ApiResponceStatus.Error -> {
                        clearServiceGetVacaciones()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
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

    private fun clearServiceGetVacaciones() {
        vacacionesFragmentViewModel.statusGetVacaciones.value = null
        vacacionesFragmentViewModel.statusGetVacaciones.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }

    private fun getDiasFestivosService(anio: String) {
        kardexMensualViewModel.dataGetDiasFestivos.value = null
        val diasFestivosRequest = DiasFestivosRequest(User.numCia.toString(), anio)
        kardexMensualViewModel.getDiasFestivos(User.token, diasFestivosRequest)
        statusObserveDiasFestivos()
        kardexMensualViewModel.dataGetDiasFestivos.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val fecha = ArrayList<String>()
                    val marca = ArrayList<String>()
                    for (i in 0 until response.diasFestivos.size) {
                        response.diasFestivos[i].fecha.let { fecha.add(it) }
                        response.diasFestivos[i].marca.let { marca.add(it) }
                    }
                    separateDayFestivos(fecha)
                    getDiasDescansosService(anio)

                }
            }
        }
    }

    private fun statusObserveDiasFestivos() {
        kardexMensualViewModel.statusGetDiasFestivos.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                        Log.i("Progress", requireContext().javaClass.name)
                    }

                    is ApiResponceStatus.Success -> {
                        clearServiceDiasFestivos()
                    }

                    is ApiResponceStatus.Error -> {
                        clearServiceDiasFestivos()
                        getDiasDescansosService(ca.getAnio().toString())
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
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

    private fun clearServiceDiasFestivos() {
        kardexMensualViewModel.statusGetDiasFestivos.value = null
        kardexMensualViewModel.statusGetDiasFestivos.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }

    private fun separateDayFestivos(fechas: ArrayList<String>) {
        g.generateFestivos(caC.aDiasFestivos(fechas), caC.aMesesFestivos(fechas))
        festivos = g.generateFestivos(
            g.listaKardex(caC.aDiasFestivos(fechas)),
            g.listaKardexMeses(caC.aMesesFestivos(fechas))
        ).groupBy { it.time.toLocalDate() }
    }

    private fun getDiasDescansosService(anio: String) {
        kardexMensualViewModel.dataGetDiasDescanso.value = null
        val diasDescansosRequest = DiasDescansosRequest(User.numCia.toString(), anio, null, numEmp.toString())
        kardexMensualViewModel.getDiasDescansos(User.token, diasDescansosRequest)
        kardexMensualViewModel.dataGetDiasDescanso.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val fecha = ArrayList<String>()
                    val marca = ArrayList<String>()
                    for (i in 0 until response.diasDescanso.size) {
                        response.diasDescanso[i].fecha.let { fecha.add(it) }
                        response.diasDescanso[i].marca.let { marca.add(it) }
                        separateDayDescansos(fecha)
                        viewCustomization()
                        customCalendar()
                        dateNow()
                    }
                }
            }
        }
        statusObserveDiasDescansos()
    }

    private fun statusObserveDiasDescansos() {
        kardexMensualViewModel.statusGetDiasDescanso.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                        Log.i("Progress", requireContext().javaClass.name)
                    }

                    is ApiResponceStatus.Success -> {
                        clearServiceDiasDescansos()
                    }

                    is ApiResponceStatus.Error -> {
                        viewCustomization()
                        customCalendar()
                        dateNow()
                        clearServiceDiasDescansos()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
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

    private fun clearServiceDiasDescansos() {
        kardexMensualViewModel.statusGetDiasDescanso.value = null
        kardexMensualViewModel.statusGetDiasDescanso.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }

    private fun separateDayDescansos(fechas: ArrayList<String>) {
        g.generateDescansos(caC.aDiasDescansos(fechas), caC.aMesesDescansos(fechas))
        descansos =
            g.generateDescansos(
                g.listaKardex(caC.aDiasDescansos(fechas)),
                g.listaKardexMeses(caC.aMesesDescansos(fechas))
            )
                .groupBy { it.time.toLocalDate() }
    }

    private fun dateNow() {
        val txtfecha = caC.mostrarFecha(ca.getAnio(), ca.getMes(), ca.getDia())
        binding.FechaInicio.text = txtfecha
        binding.FechaFinal.text = txtfecha
        val fechaFormato = caC.asignarFecha(ca.getAnio(), ca.getMes(), ca.getDia())
        fechaInicio = fechaFormato
        fechaFin = fechaFormato
    }

    private fun addVacacionesService() {
        vacacionesFragmentViewModel.dataAddVacaciones.value = null
        val addVacacionesRequest =
            com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.data.models.AddVacacionesRequest(
                User.numCia.toString(),
                numEmp!!.toString(),
                fechaInicio,
                fechaFin
            )
        vacacionesFragmentViewModel.addVacaciones(User.token, addVacacionesRequest)
        vacacionesFragmentViewModel.dataAddVacaciones.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    findNavController().navigate(R.id.to_succesFragment)
                }
            }
        }
        statusObserveAddVacaciones()
    }

    private fun statusObserveAddVacaciones() {
        vacacionesFragmentViewModel.statusAddVacaciones.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                        Log.i("Progress", requireContext().javaClass.name)
                    }

                    is ApiResponceStatus.Success -> {
                        clearServiceAddVacaciones()
                    }

                    is ApiResponceStatus.Error -> {
                        clearServiceAddVacaciones()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
                        Utilities.loadMessageError(errorCode, requireActivity())
                    }
                }
            }
        }
    }

    private fun clearServiceAddVacaciones() {
        vacacionesFragmentViewModel.statusAddVacaciones.value = null
        vacacionesFragmentViewModel.statusAddVacaciones.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }

    private fun customCalendar() {
        val clipLevelHalf = 5000
        val ctx = requireContext()
        val rangeStartBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_start)
                .also { it.level = clipLevelHalf }
        val rangeEndBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_end)
                .also { it.level = clipLevelHalf }
        val rangeMiddleBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_middle)
        val singleBackground = ctx.getDrawableCompat(R.drawable.example_4_single_selected_bg)

        @SuppressLint("ClickableViewAccessibility")
        class DayViewContainerAltaVacaciones(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            var textView = CalendarDayLayoutBinding.bind(view).calendarDayText
            var texto = CalendarDayLayoutBinding.bind(view).calendarKardexMensual
            val binding = CalendarDayLayoutBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate && (day.date == today || day.date.isAfter(
                            today
                        ))
                    ) {
                        val text = caC.validaTxt(textView.text as String, requireContext(), 2)
                        if (text == "else") {
                            selection = ContinuousSelectionHelper.getSelection(
                                clickedDate = day.date,
                                dateSelection = selection,
                            )
                            this@alta_vacaciones.binding.calendarView.notifyCalendarChanged()
                            bindSummaryViews()
                        } else {
                            Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainerAltaVacaciones> {
            override fun create(view: View) = DayViewContainerAltaVacaciones(view)
            override fun bind(container: DayViewContainerAltaVacaciones, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }
        }

        binding.calendarView.setup(
            currentMonth.withMonth(1),
            currentMonth.withMonth(12),
            daysOfWeek.first()
        )
        binding.calendarView.scrollToMonth(currentMonth)

        val titleContainer = binding.titlesContainer
        binding.calendarView.monthScrollListener = {
            ca.setMes(it.yearMonth.monthValue)
            val mes = ca.kardexTitleCustom(requireContext(), ca.getMes())
            titleContainer.root.text = mes
        }
        titleContainer.root.setTextColor(Color.BLACK)

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainerAltaVacaciones> {
            override fun create(view: View) = DayViewContainerAltaVacaciones(view)

            @SuppressLint("SetTextI18n")
            override fun bind(container: DayViewContainerAltaVacaciones, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
                container.texto.text = data.date.dayOfMonth.toString()
                container.binding.calendarDayText.background = null
                container.day = data
                val context = container.binding.root.context
                val customKardexMensual = container.binding.calendarKardexMensual
                val (startDate, endDate) = selection
                customKardexMensual.background = null
                if (data.position == DayPosition.MonthDate) {
                    container.textView.setTextColor(Color.WHITE)
                    container.binding.dayLayout.setBackgroundResource(if (selectedDate == data.date) R.color.black else 0)

                    val kardexMensualVacaciones = kardexMensualVacaciones?.get(data.date)
                    val kardexMensualFaltas = kardexMensualFaltas?.get(data.date)
                    val kardexMensualOthers = kardexMensualOthers?.get(data.date)
                    val descansos = descansos?.get(data.date)
                    val festivos = festivos?.get(data.date)
                    val ausentismos = kardexMensualAusentismos?.get(data.date)
                    val retardos = kardexMensualRetardos?.get(data.date)

                    if (data.date.isBefore(today)) {
                        container.texto.setTextColor(context.getColor(R.color.proganual_4_grey))
                        container.texto.isClickable = false
                    } else {
                        when {
                            startDate == data.date && endDate == null -> {
                                customKardexMensual.setTextColorRes(R.color.white)
                                customKardexMensual.applyBackground(singleBackground)
                                binding.FechaFinal.text = getString(R.string.fvSeleccionarFecha)
                                binding.txtDias2.text = "1 ${getString(R.string.faDia)}"
                                fechaFin = fechaInicio
                            }

                            data.date == startDate -> {
                                customKardexMensual.setTextColorRes(R.color.black)
                                customKardexMensual.applyBackground(rangeStartBackground)
                                customKardexMensual.applyBackground(singleBackground)
                            }

                            startDate != null && endDate != null && (data.date > startDate && data.date < endDate) -> {
                                customKardexMensual.setTextColorRes(R.color.white)
                                customKardexMensual.applyBackground(rangeMiddleBackground)
                            }

                            data.date == endDate -> {
                                customKardexMensual.setTextColorRes(R.color.white)
                                customKardexMensual.applyBackground(rangeEndBackground)
                                customKardexMensual.applyBackground(singleBackground)
                            }

                            data.date == today -> {
                                container.texto.setTextColor(context.getColor(R.color.black))
                                container.texto.setBackgroundResource(R.drawable.example_3_selected_bg)
                                container.binding.dayLayout.setBackgroundColor(
                                    context.getColorCompat(
                                        R.color.white
                                    )
                                )
                            }

                            else -> customKardexMensual.setTextColorRes(R.color.black)
                        }
                    }
                    if (kardexMensualVacaciones != null) {
                        caC.pintaVacaciones(container.binding.calendarDayText, requireContext())
                    }
                    if (kardexMensualFaltas != null) {
                        caC.pintaFaltas(container.binding.calendarDayText, requireContext())
                    }
                    if (kardexMensualOthers != null) {
                        caC.pintaOthers(
                            container.binding.calendarDayText,
                            requireContext(),
                            kardexMensualOthers
                        )
                    }

                    if (descansos != null) {
                        caC.pintaDescansos(
                            container.binding.calendarDayText,
                            requireContext(),
                            descansos
                        )
                    }
                    if (festivos != null) {
                        caC.pintaFestivos(
                            container.binding.calendarDayText,
                            requireContext(),
                            festivos
                        )
                    }
                    if (ausentismos != null) {
                        caC.pintaAusentismos(
                            container.binding.calendarDayText,
                            requireContext(),
                            ausentismos
                        )
                    }
                    if (retardos != null) {
                        caC.pintaRetardos(
                            container.binding.calendarDayText,
                            requireContext()
                        )
                    }
                } else {
                    container.texto.setBackgroundColor(context.getColor(R.color.fondovacaciones))
                    container.textView.setTextColor(Color.BLACK)
                    container.textView.setTextColor(Color.WHITE)
                }
            }

            private fun View.applyBackground(drawable: Drawable) {
                makeVisible()
                background = drawable
            }
        }
        binding.calendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) =
                MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.titlesContianer.tag == null) {
                    container.titlesContianer.tag = data.yearMonth
                    container.titlesContianer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title =
                                dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                        }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    internal fun bindSummaryViews() {
        binding.FechaInicio.apply {
            if (selection.startDate != null) {
                text = formatoVista.format(selection.startDate)
                fechaInicio = formatoEnviado.format(selection.startDate)
            }
        }
        binding.FechaFinal.apply {
            if (selection.endDate != null) {
                text = formatoVista.format(selection.endDate)
                fechaFin = formatoEnviado.format(selection.endDate)
            }
        }

        if (binding.FechaInicio.text.isNotEmpty() && binding.FechaFinal.text.isNotEmpty() && binding.FechaFinal.text != getString(
                R.string.fvSeleccionarFecha
            )
        ) {
            val validateDate = ca.validaFechaMenor(
                binding.FechaInicio.text as String,
                binding.FechaFinal.text as String
            )
            if (validateDate == "correcto") {
                val d = caC.diferenciaFechas(fechaInicio, fechaFin)
                binding.txtDias2.text = "$d ${getString(R.string.faDias)}"
            } else {
                binding.txtDias2.text = "1 ${getString(R.string.faDia)}"
            }
        } else if (fechaInicio.isNotEmpty() && fechaFin.isNotEmpty()) {
            binding.txtDias2.text = "1 ${getString(R.string.faDia)}"
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
}
