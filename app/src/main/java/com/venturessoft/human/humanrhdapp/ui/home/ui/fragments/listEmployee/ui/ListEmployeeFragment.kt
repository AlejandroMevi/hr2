package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.venturessoft.human.humanrhdapp.network.Response.ItemItem
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.databinding.FragmentListEmployeeBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.HomeActivity.Companion.showButtonBar
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.vm.ListEmployeeViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.MenuModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import es.dmoral.toasty.Toasty

class ListEmployeeFragment : Fragment(), ListEmployeeAdapter.OnClickListener {
    private lateinit var binding: FragmentListEmployeeBinding
    private lateinit var searchList: ArrayList<ItemItem>
    private val listEmployeeViewModel = ListEmployeeViewModel()
    private val welcomeVM : WelcomeFragmentViewModel by activityViewModels()
    private var mainInterface: MainInterface? = null
    private var id: Int = 0

    companion object {
        lateinit var usuariosListaArrayResponse: java.util.ArrayList<ItemItem>
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        User.listUsu?.let { setDataKardex(it) }
        searchEmploye()
    }
    override fun onResume() {
        super.onResume()
        showButtonBar.value = true
        welcomeVM.idMenu.observe(viewLifecycleOwner){
            mainInterface?.setTextToolbar(it.name)
            id = it.id
        }
    }
    private fun searchEmploye() {
        binding.etFilter.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                corutineEmployListService(text.toString())
            } else {
                User.listUsu?.let { setDataKardex(it) }
            }
        }
    }
    private fun corutineEmployListService(newText: String) {
        listEmployeeViewModel.busquedaEmpleado(User.token, User.numCia, User.usuario, size = 30, newText, 1)
        statusObserve()
        listEmployeeViewModel.dataBusquedaEmpleado.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val list = java.util.ArrayList<ItemItem>()
                    for (i in response.items.item.indices) {
                        val dataModel = ItemItem()
                        dataModel.nombreCompleto = response.items.item[i]?.nombreCompleto.toString()
                        dataModel.puesto = response.items.item[i]?.puesto.toString()
                        dataModel.fotografia = response.items.item[i]?.fotografia.toString()
                        dataModel.numEmp = response.items.item[i]?.numEmp.toString()
                        list.add(dataModel)
                        usuariosListaArrayResponse = list
                        searchList = usuariosListaArrayResponse
                        setDataKardex(searchList)
                    }
                }
                listEmployeeViewModel.dataBusquedaEmpleado.removeObservers(viewLifecycleOwner)
            }
        }
    }
    private fun setDataKardex(listaUsuarios: ArrayList<ItemItem>) {
        binding.listEmployee.adapter = ListEmployeeAdapter(listaUsuarios, viewLifecycleOwner, this)
    }
    private fun statusObserve() {
        listEmployeeViewModel.statusBusquedaEmpleado.observe(viewLifecycleOwner) { status ->
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
        listEmployeeViewModel.statusBusquedaEmpleado.value = null
        listEmployeeViewModel.statusBusquedaEmpleado.removeObservers(viewLifecycleOwner)
        mainInterface?.showLoading(false)
    }
    override fun onClick(item: ItemItem, position: Int, cardviewlista: MaterialCardView) {
        val name = item.nombreCompleto
        val numEmp = item.numEmp.toString()
        val preferences = requireContext().getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("nameUser", name)
        editor.putString("numEmp", numEmp)
        editor.apply()
        welcomeVM.idMenu.value = MenuModel(
            welcomeVM.idMenu.value?.id?:0,
            welcomeVM.idMenu.value?.name?:"",
            welcomeVM.idMenu.value?.resource?:0,
            item.puesto?:"",
            item.numEmp?:"0",
            item.nombreCompleto?:"",
            item.fotografia?:""
        )
        when(id){
            1 -> findNavController().navigate(R.id.to_alta_vacaciones)
            2 -> findNavController().navigate(R.id.to_progAnualVacFragment)
            3 -> findNavController().navigate(R.id.to_ausentismosFragment)
            4 -> findNavController().navigate(R.id.to_permisos)
            5 -> findNavController().navigate(R.id.to_kardexAnualFragment)
            6 -> findNavController().navigate(R.id.to_kardexMensualFragment)
            8 -> findNavController().navigate(R.id.to_informacionGeneralFragment)
            9 -> findNavController().navigate(R.id.to_negociacionHorariosFragment)
            10 -> findNavController().navigate(R.id.to_entradasSalidasFragment)
            11 -> findNavController().navigate(R.id.to_preautorizacionTiemposFragment)
            12 -> findNavController().navigate(R.id.to_preautorizacionTiemposFragment)
        }
        showButtonBar.value = false
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