package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.ProgAnualVacaciones.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.Calendario
import com.venturessoft.human.humanrhdapp.databinding.FragmentListaSolicitudesBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.ProgAnualVacaciones.ui.data.models.VacacionesProgramadasItem
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.ProgAnualVacaciones.ui.vm.ProgAnualVacacionesViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities

class ListaSolicitudesFragment : Fragment() {

    private lateinit var binding: FragmentListaSolicitudesBinding
    private lateinit var listaSolicitud: ArrayList<VacacionesProgramadasItem>
    private val welcomeFragmentViewModel : WelcomeFragmentViewModel by activityViewModels()
    private val progAnualVacacionesViewModel: ProgAnualVacacionesViewModel by viewModels()
    private var mainInterface: MainInterface? = null
    private var ca: Calendario = Calendario()
    private var names: String? = null
    private var numEmp: String? = null

    companion object {
        lateinit var usuariosListaSolicitudesArrayResponse: java.util.ArrayList<VacacionesProgramadasItem>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListaSolicitudesBinding.inflate(inflater, container, false)
        welcomeFragmentViewModel.idMenu.observe(viewLifecycleOwner) {
            names = it.nombreCompleto
            numEmp = it.numEmp
            listaUsuariosService()
            viewCustomization()
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
        binding.txtNombre.text = names
        binding.idtxt.text = numEmp
        binding.btnSave.button.text = getString(R.string.aurotizar)
        binding.btnSave.button.setOnClickListener {
            var listUser: List<Long> = listOf()
            if (!ListSolicitudesAdapter.listSolicitudSelect.value.isNullOrEmpty()) {
                listUser = ListSolicitudesAdapter.listSolicitudSelect.value!!
            }
            println(listUser)
        }
    }
    private fun listaUsuariosService() {
        progAnualVacacionesViewModel.dataListaSolicitudes.value = null
        progAnualVacacionesViewModel.listaSolicitudes(User.token, User.numCia, numEmp!!.toLong(), ca.getAnio().toLong())
        progAnualVacacionesViewModel.dataListaSolicitudes.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val list = java.util.ArrayList<VacacionesProgramadasItem>()
                    for (i in response.vacacionesProgramadas!!.indices) {
                        val dataModel = VacacionesProgramadasItem()
                        dataModel.grupo = response.vacacionesProgramadas[i]?.grupo
                        dataModel.secuencia = response.vacacionesProgramadas[i]?.secuencia
                        dataModel.fechaInicio = response.vacacionesProgramadas[i]?.fechaInicio
                        dataModel.fechaTermino = response.vacacionesProgramadas[i]?.fechaTermino
                        dataModel.diasTomados = response.vacacionesProgramadas[i]?.diasTomados
                        list.add(dataModel)
                        usuariosListaSolicitudesArrayResponse = list
                        listaSolicitud = usuariosListaSolicitudesArrayResponse
                        setDataKardex(listaSolicitud)
                    }
                }
            }
        }
        statusObserveAddVacaciones()
    }
    private fun setDataKardex(listaUsuarios: ArrayList<VacacionesProgramadasItem>) {
        binding.listaSolicitudes.adapter = ListSolicitudesAdapter(listaUsuarios)
    }
    private fun statusObserveAddVacaciones() {
        progAnualVacacionesViewModel.statusListaSolicitudes.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
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
        progAnualVacacionesViewModel.statusListaSolicitudes.value = null
        progAnualVacacionesViewModel.statusListaSolicitudes.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }

}