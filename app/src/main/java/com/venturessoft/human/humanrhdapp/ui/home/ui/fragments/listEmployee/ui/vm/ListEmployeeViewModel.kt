package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.data.models.BusquedaUsuarioResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.data.net.BusquedaEmpleadoService
import kotlinx.coroutines.launch

class ListEmployeeViewModel : ViewModel() {

    val repository = BusquedaEmpleadoService()
    var statusBusquedaEmpleado = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataBusquedaEmpleado = MutableLiveData<BusquedaUsuarioResponse?>(null)
        private set

    fun busquedaEmpleado(token: String, numCia: Long, supervisor: String, size: Long, cadenaBusqueda: String, page: Long, area: String? = null, centro: String? = null, linea: String? = null) {
        viewModelScope.launch {
            statusBusquedaEmpleado.value = ApiResponceStatus.Loading()
            val responce = repository.busquedEmpleado(token, numCia, supervisor, size, cadenaBusqueda, page, area, centro, linea)
            if (responce is ApiResponceStatus.Success) {
                dataBusquedaEmpleado.value = responce.data
            }
            statusBusquedaEmpleado.value = responce as ApiResponceStatus<Any>
        }
    }
}