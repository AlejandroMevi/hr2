package com.venturessoft.human.humanrhdapp.core

import android.graphics.Color
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.humanrhdapp.network.Response.ItemItem
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.ui.home.ui.HomeActivity
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.ListEmployeeFragment
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.vm.ListEmployeeViewModel
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce
import es.dmoral.toasty.Toasty

class SearchEmploye(private val viewModel: ListEmployeeViewModel, private val fragment: Fragment) {
    companion object{
        var listUser = MutableLiveData<List<ItemItem>>(listOf())
    }
    init {
        User.listUsu?.let { listUser.value = it }
    }
    fun searchEmploye(etFilter: EditText){
        etFilter.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                corutineEmployListService(text.toString())
            } else {
                User.listUsu?.let { listUser.value = it }
            }
        }
    }
    private fun corutineEmployListService(newText: String) {
        viewModel.busquedaEmpleado(User.token, User.numCia, User.usuario, size = 30, newText, 1)
        statusObserve()
        viewModel.dataBusquedaEmpleado.observeOnce(fragment) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val list = ArrayList<ItemItem>()
                    for (i in response.items.item.indices) {
                        val dataModel = ItemItem()
                        dataModel.nombreCompleto = response.items.item[i]?.nombreCompleto.toString()
                        dataModel.puesto = response.items.item[i]?.puesto.toString()
                        dataModel.fotografia = response.items.item[i]?.fotografia.toString()
                        dataModel.numEmp = response.items.item[i]?.numEmp.toString()
                        list.add(dataModel)
                        ListEmployeeFragment.usuariosListaArrayResponse = list
                        listUser.value = ListEmployeeFragment.usuariosListaArrayResponse
                    }
                }
            }
        }
    }
    private fun clearService() {
        (fragment.activity as HomeActivity).showLoading(false)
        viewModel.statusBusquedaEmpleado.removeObservers(fragment)
        viewModel.statusBusquedaEmpleado.value = null
    }
    private fun statusObserve() {
        viewModel.statusBusquedaEmpleado.observe(fragment) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        (fragment.activity as HomeActivity).showLoading(true)
                    }
                    is ApiResponceStatus.Success -> {
                        clearService()
                    }
                    is ApiResponceStatus.Error -> {
                        clearService()
                        val errorCode = Utilities.textcode(status.messageId, fragment.requireContext())
                        Toasty.custom(
                            fragment.requireContext(),
                            errorCode,
                            ContextCompat.getDrawable(fragment.requireContext(), R.drawable.warning),
                            fragment.requireContext().getColor(R.color.principal),
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