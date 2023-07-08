package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.KardexMensual.ui

import android.content.Context
import android.graphics.Color
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
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.Calendario
import com.venturessoft.human.humanrhdapp.databinding.FragmentKardexMenualBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.MonthViewContainer
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.anualKardex.data.models.KardexAnualRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.anualKardex.ui.vm.KardexAnualViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.KardexMensual.data.models.DiasDescansosRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.KardexMensual.data.models.DiasFestivosRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.KardexMensual.ui.vm.KardexMensualViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.calendar.CalendarioCustom
import com.venturessoft.human.humanrhdapp.utilis.calendar.CustomDay
import com.venturessoft.human.humanrhdapp.utilis.calendar.DayViewContainerKardexMensual
import com.venturessoft.human.humanrhdapp.utilis.calendar.GeneraMarcas
import es.dmoral.toasty.Toasty
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class KardexMensualFragment : Fragment() {
    private lateinit var binding: FragmentKardexMenualBinding
    private val welcomeFragmentViewModel : WelcomeFragmentViewModel by activityViewModels()
    private val kardexMensualViewModel = KardexMensualViewModel()
    private val kardexAnualViewModel = KardexAnualViewModel()
    private var caC: CalendarioCustom = CalendarioCustom()
    private var g: GeneraMarcas = GeneraMarcas()
    private var ca: Calendario = Calendario()
    private var names: String? = null
    private var numEmp: String? = null
    private val currentMonth = YearMonth.now()!!
    private var selectedDate: LocalDate? = null
    private var kardexMensualAusentismos: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualRetardos: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualVacaciones: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualFaltas: Map<LocalDate, List<CustomDay>>? = null
    private var kardexMensualOthers: Map<LocalDate, List<CustomDay>>? = null
    private var descansos: Map<LocalDate, List<CustomDay>>? = null
    private var festivos: Map<LocalDate, List<CustomDay>>? = null
    private var mainInterface: MainInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKardexMenualBinding.inflate(inflater, container, false)
        welcomeFragmentViewModel.idMenu.observe(viewLifecycleOwner) {
            names = it.nombreCompleto
            numEmp = it.numEmp
            loadService()
        }
        return binding.root
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

    private fun viewCustomization() {
        binding.idtxt.text = "$numEmp"
        binding.txtNombre.text = "$names"
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
                    val marcas = ArrayList<String>()
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
        val marcasOthers = caC.aMarcasOthers(fechas, marcas)
        if (diaVaciones.isNotEmpty()) g.generateVacaciones(diaVaciones, mesVacaciones)
        if (diaAusentismos.isNotEmpty()) g.generateAusentismos(diaAusentismos, mesAusentismos)
        if (diaRetardos.isNotEmpty()) g.generateRetardos(diaRetardos, mesRetardos)
        if (diaOthers.isNotEmpty()) g.generateOthersPruebas(diaOthers, mesOthers, marcasOthers)
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
            g.generateOthersPruebas(
                g.listaKardex(diaOthers), g.listaKardexMeses(mesOthers), g.listaKardexMarcas(marcasOthers)
            ).groupBy { it.time.toLocalDate() }

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
        festivos =
            g.generateFestivos(
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
                        clearServiceAddPermisos()
                    }

                    is ApiResponceStatus.Error -> {
                        viewCustomization()
                        customCalendar()
                        clearServiceAddPermisos()
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

    private fun clearServiceAddPermisos() {
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

    private fun customCalendar() {
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        val titleContainer = binding.titlesContainer

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainerKardexMensual> {
            override fun create(view: View) = DayViewContainerKardexMensual(view, requireContext())
            override fun bind(container: DayViewContainerKardexMensual, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }
        }

        binding.calendarView.setup(
            currentMonth.withMonth(1),
            currentMonth.withMonth(12), daysOfWeek.first()
        )
        binding.calendarView.scrollToMonth(currentMonth)

        binding.calendarView.monthScrollListener = {
            ca.setMes(it.yearMonth.monthValue)
            val mes = ca.kardexTitleCustom(requireContext(), ca.getMes())
            titleContainer.root.text = mes
        }
        titleContainer.root.setTextColor(Color.BLACK)

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainerKardexMensual> {
            override fun create(view: View) = DayViewContainerKardexMensual(view, requireContext())
            override fun bind(container: DayViewContainerKardexMensual, data: CalendarDay) {
                container.day = data
                container.textView.text = data.date.dayOfMonth.toString()
                container.texto.text = data.date.dayOfMonth.toString()
                container.binding.calendarKardexMensual.background = null
                container.binding.calendarDayText.background = null

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
                    container.texto.setBackgroundColor(container.binding.root.context.getColor(R.color.fondovacaciones))
                    container.textView.setTextColor(Color.BLACK)
                    container.textView.setTextColor(Color.WHITE)
                }
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
}