package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.welcome

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.models.KardexMensualItem
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.Calendario
import com.venturessoft.human.humanrhdapp.databinding.FragmentWelcomeBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.models.kardexReporteRequest
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

class WelcomeFragment : Fragment(), OnChartValueSelectedListener {
    private lateinit var binding: FragmentWelcomeBinding
    private val welcomeFragmentViewModel: WelcomeFragmentViewModel by activityViewModels()
    private var mainInterface: MainInterface? = null
    private var ca: Calendario = Calendario()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCustomization()
        customViewKardexDaysHeader()
        scrollListView()
        mainInterface?.setTextToolbar(User.razonSocial)
        Utilities.validateAndRefreshJWT()
        Utilities.jwt()
        flechas()
        corutineKardexService(ca.getAnio(), ca.getMes())
        corutineDashBoardService()
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

    @SuppressLint("SetTextI18n")
    private fun viewCustomization() {
        val mes = ca.kardexTitleCustom(requireContext(), Calendar.getInstance().get(Calendar.MONTH) + 1)
        binding.welcomeName.text = User.nombreSupervisor
        binding.welcomeEstadistica.text = "${getString(R.string.welcome_est)} $mes"
        binding.btnRefresh.setOnClickListener {
            val animation = RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            animation.duration = 1000
            animation.repeatCount = Animation.INFINITE
            binding.btnRefresh.startAnimation(animation)
            corutineDashBoardService()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun kardexTitulo() {
        val month = ca.kardexTitleCustom(requireContext(), ca.getMes())
        binding.kardexTv.animate().alpha(0f).setDuration(300).withEndAction {
            binding.kardexTv.text = "${getString(R.string.kardex_title1)} $month ${getString(R.string.kardex_title2)} ${ca.getAnio()}"
            binding.kardexTv.animate().alpha(1f).setDuration(300).start()
        }.start()
    }
    private fun flechas() {
        binding.leftArrow.setOnClickListener {
            if (ca.getMes() == 1) {
                ca.setAnio(ca.getAnio() - 1)
                ca.setMes(12)
            } else {
                ca.setMes(ca.getMes() - 1)
            }
            kardexTitulo()
            corutineKardexService(ca.getAnio(), ca.getMes())
        }
        binding.rightArrow.setOnClickListener {
            if (ca.getMes() == 12) {
                ca.setAnio(ca.getAnio() + 1)
                ca.setMes(1)
            } else {
                ca.setMes(ca.getMes() + 1)
            }
            kardexTitulo()
            corutineKardexService(ca.getAnio(), ca.getMes())
        }
    }
    private fun customViewKardexDaysHeader() {
        val diaA = listOf(
            binding.dayOne,
            binding.dayTwo,
            binding.dayThree,
            binding.dayFour,
            binding.dayFive,
            binding.daySix,
            binding.daySeven,
            binding.dayEight,
            binding.dayNine,
            binding.dayTen,
            binding.dayEleven,
            binding.dayTwelve,
            binding.dayThirteen,
            binding.dayFourteen,
            binding.dayFiveteen,
            binding.daySixteen,
            binding.daySeventeen,
            binding.dayEighteen,
            binding.dayNineteen,
            binding.dayTwenty,
            binding.dayTwentyOne,
            binding.dayTwentyTwo,
            binding.dayTwentyThree,
            binding.dayTwentyFour,
            binding.dayTwentyFive,
            binding.dayTwentySix,
            binding.dayTwentySeven,
            binding.dayTwentyEight,
            binding.dayTwentyNine,
            binding.dayThirty,
            binding.dayThirtyOne
        )
        diaA[ca.getDia() - 1].setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.principal
            )
        )
        diaA[ca.getDia() - 1].setTextColor(Color.WHITE)
    }
    private fun corutineKardexService(year: Int, mes: Int) {

        welcomeFragmentViewModel.dataKardex.value = null
        val addKardexReporteRequest = kardexReporteRequest(User.numCia.toString(), numEmp = "1", year.toString(), mes.toString(), motivo = "todos", User.usuario)
        welcomeFragmentViewModel.kardexReporte(User.token, addKardexReporteRequest)
        welcomeFragmentViewModel.dataKardex.observeOnce(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.kardexMensual.isNullOrEmpty()) {
                    binding.listView.visibility = View.GONE
                    binding.txtMessage.visibility = View.VISIBLE
                    binding.listViewLeft.adapter = WelcomeUserAdapter(listOf())
                    Toasty.custom(requireContext(), getString(R.string.welcome_kardex), ContextCompat.getDrawable(requireContext(), R.drawable.warning), requireContext().getColor(R.color.principal), Color.WHITE, Toast.LENGTH_SHORT, true, true).show()
                } else {
                    binding.txtMessage.visibility = View.GONE
                    binding.listView.visibility = View.VISIBLE
                    val list = ArrayList<KardexMensualItem>()
                    for (i in response.kardexMensual.indices) {
                        val dataModel = KardexMensualItem()
                        dataModel.numEmp = response.kardexMensual[i]!!.numEmp
                        dataModel.nomEmp = response.kardexMensual[i]!!.nomEmp
                        dataModel.fotoEmp = response.kardexMensual[i]!!.fotoEmp
                        dataModel.marcas = response.kardexMensual[i]!!.marcas
                        list.add(dataModel)
                        binding.listView.adapter = WelcomeAdapter(list, requireActivity())
                        binding.listViewLeft.adapter = WelcomeUserAdapter(list)
                        binding.listViewLeft.isClickable = false
                        binding.listView.isClickable = false
                    }
                }
            }
        }
        statusObserveKardex()
    }
    private fun statusObserveKardex() {
        welcomeFragmentViewModel.statusKardex.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        /*mainInterface?.showLoading(true)*/
                    }
                    is ApiResponceStatus.Success -> {
                        clearServiceKardex()
                    }
                    is ApiResponceStatus.Error -> {
                        clearServiceKardex()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
                        Toasty.custom(requireContext(), errorCode, ContextCompat.getDrawable(requireContext(), R.drawable.warning), requireContext().getColor(R.color.principal), Color.WHITE, Toast.LENGTH_SHORT, true, true).show()
                    }
                }
            }
        }
    }
    private fun clearServiceKardex() {
        mainInterface?.showLoading(false)
        welcomeFragmentViewModel.statusKardex.removeObservers(viewLifecycleOwner)
        welcomeFragmentViewModel.statusKardex.value = null
    }
    private fun corutineDashBoardService() {
        welcomeFragmentViewModel.dataDashBoard.value = null
        welcomeFragmentViewModel.dashBoard(User.token, User.usuario, 7, User.numCia)
        welcomeFragmentViewModel.dataDashBoard.observeOnce(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val dias = ArrayList<String>()
                    val asistencia = ArrayList<Float>()
                    val asistenciaEmp = ArrayList<Double>()
                    for (i in response.entradasSalidas!!.indices) {
                        response.entradasSalidas[i]?.fecha?.let { dias.add(it) }
                        response.entradasSalidas[i]?.asistencia?.let { asistencia.add(it) }
                        asistenciaEmp.add(
                            response.entradasSalidas[i]?.asistencia.toString().toDouble()
                        )
                    }
                    loadBarChart(dias, asistencia, asistenciaEmp)
                }
            }
        }
        statusObserveDashBoard()
    }
    private fun statusObserveDashBoard() {
        welcomeFragmentViewModel.statusDashBoard.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        //binding.btnRefresh.clearAnimation()
                    }
                    is ApiResponceStatus.Success -> {
                        clearServiceDashBoard()
                        binding.btnRefresh.clearAnimation()
                    }
                    is ApiResponceStatus.Error -> {
                        clearServiceDashBoard()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
                        Toasty.custom(requireContext(), errorCode, ContextCompat.getDrawable(requireContext(), R.drawable.warning), requireContext().getColor(R.color.principal), Color.WHITE, Toast.LENGTH_SHORT, true, true).show()
                    }
                }
            }
        }
    }
    private fun clearServiceDashBoard() {
        mainInterface?.showLoading(false)
        binding.btnRefresh.clearAnimation()
        welcomeFragmentViewModel.statusDashBoard.removeObservers(viewLifecycleOwner)
        welcomeFragmentViewModel.statusDashBoard.value = null
    }
    private fun loadBarChart(dias: ArrayList<String>, asistencia: ArrayList<Float>, asistenciaEmp: ArrayList<Double>) {
        val noOfEmp = ArrayList<BarEntry>()
        val year = ArrayList<String>()
        val dateFormatOrigin = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormatTarget = SimpleDateFormat("dd/MM", Locale.getDefault())
        for (i in 0..6) {
            noOfEmp.add(BarEntry(asistencia[i], i, asistenciaEmp[i]))
            val dtStart = dias[i]
            val date = dateFormatOrigin.parse(dtStart)
            date?.let { dateFormatTarget.format(it) }?.let { year.add(it) }
        }
        val bardataset = BarDataSet(noOfEmp, getString(R.string.asistencia))
        binding.graficChard.animateY(5000)
        binding.graficChard.setVisibleXRangeMaximum(7f)
        binding.graficChard.setVisibleYRangeMaximum(100f, YAxis.AxisDependency.LEFT)
        binding.graficChard.axisLeft.setLabelCount(5, true)
        binding.graficChard.axisLeft.mAxisRange = 25f
        binding.graficChard.axisLeft.setAxisMinValue(0f)
        binding.graficChard.axisLeft.setAxisMaxValue(100f)
        binding.graficChard.axisRight.setLabelCount(5, true)
        binding.graficChard.axisRight.mAxisRange = 25f
        binding.graficChard.axisRight.setAxisMinValue(0f)
        binding.graficChard.axisRight.setAxisMaxValue(100f)
        binding.graficChard.xAxis.setLabelsToSkip(0)
        binding.graficChard.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.graficChard.isScaleXEnabled = false
        binding.graficChard.isScaleYEnabled = false
        binding.graficChard.axisRight.isEnabled = false
        binding.graficChard.setDescription("")
        val data = BarData(year, bardataset)
        val color = ContextCompat.getColor(requireActivity().applicationContext, R.color.chart_color)
        binding.graficChard.notifyDataSetChanged()
        bardataset.color = color
        binding.graficChard.data = data
        binding.graficChard.setOnChartValueSelectedListener(this)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun scrollListView() {
        binding.listView.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.listViewLeft.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.horizontalEncabezado.setOnScrollChangeListener { v, _, _, _, _ ->
            binding.hsDays.smoothScrollTo(v.scrollX, 0)
        }

        binding.hsDays.setOnScrollChangeListener { v, _, _, _, _ ->
            binding.horizontalEncabezado.smoothScrollTo(v.scrollX, 0)
        }
    }
    override fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {
        binding.graficChard.highlightValue(h)
        if (e != null) {
            Utilities.showErrorDialog(
                getString(R.string.hay) + " " + e.data.toString() + "%" + " " + getString(
                    R.string.empleados_asistencia
                ),
                requireActivity()
            )
        }
    }
    override fun onNothingSelected() {}
}